// This file is part of CPAchecker,
// a tool for configurable software verification:
// https://cpachecker.sosy-lab.org
//
// SPDX-FileCopyrightText: 2007-2020 Dirk Beyer <https://www.sosy-lab.org>
//
// SPDX-License-Identifier: Apache-2.0

package org.sosy_lab.cpachecker.util.predicates.pathformula.tatoformula.encodings;

import static com.google.common.collect.FluentIterable.from;

import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Map;
import org.sosy_lab.cpachecker.cfa.ast.timedautomata.TaDeclaration;
import org.sosy_lab.cpachecker.cfa.model.timedautomata.TCFAEdge;
import org.sosy_lab.cpachecker.util.predicates.pathformula.tatoformula.TimedAutomatonView;
import org.sosy_lab.cpachecker.util.predicates.pathformula.tatoformula.extensions.TAEncodingExtension;
import org.sosy_lab.cpachecker.util.predicates.pathformula.tatoformula.featureencodings.TABooleanVarFeatureEncoding;
import org.sosy_lab.cpachecker.util.predicates.pathformula.tatoformula.featureencodings.TADiscreteFeatureEncoding;
import org.sosy_lab.cpachecker.util.predicates.pathformula.tatoformula.featureencodings.locations.TALocations;
import org.sosy_lab.cpachecker.util.predicates.pathformula.tatoformula.featureencodings.time.TATime;
import org.sosy_lab.cpachecker.util.predicates.smt.FormulaManagerView;
import org.sosy_lab.java_smt.api.BooleanFormula;

public class TATransitionUnrolling extends TAEncodingBase {
  private final TADiscreteFeatureEncoding<TCFAEdge> transitions;
  private final TCFAEdge delayEdge;
  private final Map<TaDeclaration, TCFAEdge> idleEdgesByAutomaton;

  public TATransitionUnrolling(
      FormulaManagerView pFmgr,
      TimedAutomatonView pAutomata,
      TATime pTime,
      TALocations pLocations,
      TAEncodingExtension pExtensions) {
    super(pFmgr, pAutomata, pTime, pLocations, pExtensions);

    idleEdgesByAutomaton = new HashMap<>();
    delayEdge = TCFAEdge.createDummyEdge();
    transitions = new TABooleanVarFeatureEncoding<>(pFmgr);

    automata.getAllAutomata().forEach(this::addTransitionVariablesForAutomaton);
  }

  private void addTransitionVariablesForAutomaton(TaDeclaration pAutomaton) {
    assert (transitions instanceof TABooleanVarFeatureEncoding<?>);
    var variableEncoding = (TABooleanVarFeatureEncoding<TCFAEdge>) transitions;

    variableEncoding.addVariableForFeature(pAutomaton, delayEdge, "delay_edge");

    var idleEdge = TCFAEdge.createDummyEdge();
    variableEncoding.addVariableForFeature(pAutomaton, idleEdge, "idle_" + pAutomaton.getName());
    idleEdgesByAutomaton.put(pAutomaton, idleEdge);

    var automatonEdges = automata.getEdgesByAutomaton(pAutomaton);
    automatonEdges.forEach(
        edge ->
            variableEncoding.addVariableForFeature(pAutomaton, edge, "edge_" + edge.hashCode()));
  }

  @Override
  protected BooleanFormula makeAutomatonTransitionsFormula(
      TaDeclaration pAutomaton, int pLastReachedIndex) {
    var transitionContraints = makeConstraintsFormula(pAutomaton, pLastReachedIndex);

    var idleEdge = idleEdgesByAutomaton.get(pAutomaton);
    var idleTransitionVariable =
        transitions.makeEqualsFormula(pAutomaton, pLastReachedIndex, idleEdge);
    var delayTransitionVariable =
        transitions.makeEqualsFormula(pAutomaton, pLastReachedIndex, delayEdge);
    var discreteTransitionVariables =
        from(automata.getEdgesByAutomaton(pAutomaton))
            .transform(edge -> transitions.makeEqualsFormula(pAutomaton, pLastReachedIndex, edge));
    var transitionVariables =
        discreteTransitionVariables.append(delayTransitionVariable).append(idleTransitionVariable);

    var atLeastOneTransition = bFmgr.or(transitionVariables.toSet());

    var transitionNotTakenFormulas = transitionVariables.transform(bFmgr::not).toSet();
    var transitionNotTakenPairs =
        Sets.cartesianProduct(transitionNotTakenFormulas, transitionNotTakenFormulas);
    var atLeastOneInPairNotTakenFormulas =
        from(transitionNotTakenPairs)
            .filter(pair -> !pair.get(0).equals(pair.get(1)))
            .transform(bFmgr::or);
    var noTwoTransitionsFormula = bFmgr.and(atLeastOneInPairNotTakenFormulas.toSet());

    return bFmgr.and(transitionContraints, atLeastOneTransition, noTwoTransitionsFormula);
  }

  private BooleanFormula makeConstraintsFormula(TaDeclaration pAutomaton, int pLastReachedIndex) {
    var delayTransitionVariable =
        transitions.makeEqualsFormula(pAutomaton, pLastReachedIndex, delayEdge);
    var delayTransitionConstraint = makeDelayTransition(pAutomaton, pLastReachedIndex);
    var delayTransitionFormula =
        bFmgr.implication(delayTransitionVariable, delayTransitionConstraint);

    var idleEdge = idleEdgesByAutomaton.get(pAutomaton);
    var idleTransitionVariable =
        transitions.makeEqualsFormula(pAutomaton, pLastReachedIndex, idleEdge);
    var idleTransitionConstraint = makeIdleTransition(pAutomaton, pLastReachedIndex);
    var idleTransitionFormula = bFmgr.implication(idleTransitionVariable, idleTransitionConstraint);

    var discreteTransitionsFormula = bFmgr.makeTrue();
    for (var edge : automata.getEdgesByAutomaton(pAutomaton)) {
      var discreteTransitionVariable =
          transitions.makeEqualsFormula(pAutomaton, pLastReachedIndex, edge);
      var discreteTransitionConstraint = makeDiscreteStep(pAutomaton, pLastReachedIndex, edge);
      var discreteTransitionFormula =
          bFmgr.implication(discreteTransitionVariable, discreteTransitionConstraint);
      discreteTransitionsFormula = bFmgr.and(discreteTransitionFormula, discreteTransitionsFormula);
    }

    return bFmgr.and(delayTransitionFormula, idleTransitionFormula, discreteTransitionsFormula);
  }
}
