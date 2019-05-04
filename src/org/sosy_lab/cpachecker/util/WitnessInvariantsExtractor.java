/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2019  Dirk Beyer
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.sosy_lab.cpachecker.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.ConfigurationBuilder;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.cpachecker.cfa.CFA;
import org.sosy_lab.cpachecker.cfa.ast.AExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CBinaryExpressionBuilder;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CIdExpression;
import org.sosy_lab.cpachecker.cfa.model.CFANode;
import org.sosy_lab.cpachecker.cfa.model.FunctionReturnEdge;
import org.sosy_lab.cpachecker.core.CPABuilder;
import org.sosy_lab.cpachecker.core.Specification;
import org.sosy_lab.cpachecker.core.algorithm.CPAAlgorithm;
import org.sosy_lab.cpachecker.core.algorithm.bmc.candidateinvariants.CandidateInvariant;
import org.sosy_lab.cpachecker.core.algorithm.bmc.candidateinvariants.ExpressionTreeLocationInvariant;
import org.sosy_lab.cpachecker.core.algorithm.bmc.candidateinvariants.ExpressionTreeLocationInvariant.ManagerKey;
import org.sosy_lab.cpachecker.core.interfaces.AbstractState;
import org.sosy_lab.cpachecker.core.interfaces.ConfigurableProgramAnalysis;
import org.sosy_lab.cpachecker.core.interfaces.StateSpacePartition;
import org.sosy_lab.cpachecker.core.reachedset.AggregatedReachedSets;
import org.sosy_lab.cpachecker.core.reachedset.ReachedSet;
import org.sosy_lab.cpachecker.core.reachedset.ReachedSetFactory;
import org.sosy_lab.cpachecker.cpa.automaton.Automaton;
import org.sosy_lab.cpachecker.cpa.automaton.AutomatonState;
import org.sosy_lab.cpachecker.cpa.automaton.WitnessInvariantsAutomaton;
import org.sosy_lab.cpachecker.cpa.automaton.WitnessLocationInvariantsAutomaton;
import org.sosy_lab.cpachecker.cpa.callstack.CallstackState;
import org.sosy_lab.cpachecker.exceptions.CPAException;
import org.sosy_lab.cpachecker.util.expressions.And;
import org.sosy_lab.cpachecker.util.expressions.ExpressionTree;
import org.sosy_lab.cpachecker.util.expressions.ExpressionTrees;
import org.sosy_lab.cpachecker.util.expressions.LeafExpression;
import org.sosy_lab.cpachecker.util.expressions.Or;
import org.sosy_lab.cpachecker.util.expressions.ToCExpressionVisitor;
import org.sosy_lab.cpachecker.util.expressions.ToFormulaVisitor;
import org.sosy_lab.cpachecker.util.states.MemoryLocation;
/** Utility class to extract candidates from witness file */
public class WitnessInvariantsExtractor {

  private Configuration config;
  private Specification specification;
  private LogManager logger;
  private CFA cfa;
  private ShutdownNotifier shutdownNotifier;
  private Path pathToWitnessFile;
  private ReachedSet reachedSet;

  public WitnessInvariantsExtractor(
      Configuration pConfig,
      Specification pSpecification,
      LogManager pLogger,
      CFA pCFA,
      ShutdownNotifier shutdownNotifier,
      Path pPathToWitnessFile)
      throws InvalidConfigurationException, CPAException {
    this.config = generateLocalConfiguration(pConfig);
    this.specification = pSpecification;
    this.logger = pLogger;
    this.cfa = pCFA;
    this.shutdownNotifier = shutdownNotifier;
    this.pathToWitnessFile = pPathToWitnessFile;
    analyzeWitness();
  }

  private Configuration generateLocalConfiguration(Configuration pConfig)
      throws InvalidConfigurationException {
    ConfigurationBuilder configBuilder =
        Configuration.builder()
            .loadFromResource(WitnessInvariantsExtractor.class, "witness-analysis.properties");
    List<String> copyOptions =
        Arrays.asList(
            "analysis.machineModel",
            "analysis.programNames",
            "cpa.callstack.skipRecursion",
            "cpa.callstack.skipVoidRecursion",
            "cpa.callstack.skipFunctionPointerRecursion",
            "witness.strictChecking",
            "witness.checkProgramHash");
    for (String copyOption : copyOptions) {
      configBuilder.copyOptionFromIfPresent(pConfig, copyOption);
    }
    return configBuilder.build();
  }

  private void analyzeWitness() throws InvalidConfigurationException, CPAException {
    ReachedSetFactory reachedSetFactory = new ReachedSetFactory(config, logger);
    reachedSet = reachedSetFactory.create();
    CPABuilder builder = new CPABuilder(config, logger, shutdownNotifier, reachedSetFactory);
    Specification automatonAsSpec =
        Specification.fromFiles(
            specification.getProperties(),
            ImmutableList.of(pathToWitnessFile),
            cfa,
            config,
            logger);
    ConfigurableProgramAnalysis cpa =
        builder.buildCPAs(cfa, automatonAsSpec, new AggregatedReachedSets());
    CPAAlgorithm algorithm = CPAAlgorithm.create(cpa, logger, config, shutdownNotifier);
    CFANode rootNode = cfa.getMainFunction();
    StateSpacePartition partition = StateSpacePartition.getDefaultPartition();
    try {
      reachedSet.add(
          cpa.getInitialState(rootNode, partition), cpa.getInitialPrecision(rootNode, partition));
      algorithm.run(reachedSet);
    } catch (InterruptedException e) {
      // Candidate collection was interrupted,
      // but instead of throwing the exception here,
      // let it be thrown by the invariant generator.
    }
  }

  public Automaton buildInvariantsAutomatonFromWitness(
      ToCExpressionVisitor visitor, CBinaryExpressionBuilder builder) {
    final Set<ExpressionTreeLocationInvariant> invariants = Sets.newLinkedHashSet();
    extractInvariantsFromReachedSet(invariants);
    return WitnessInvariantsAutomaton.buildWitnessInvariantsAutomaton(invariants, visitor, builder);
  }

  public Automaton buildLocationInvariantsAutomatonFromWitness() {
    WitnessLocationInvariantsAutomaton automaton =
        new WitnessLocationInvariantsAutomaton(cfa, logger);
    final Set<ExpressionTreeLocationInvariant> invariants = Sets.newLinkedHashSet();
    extractInvariantsFromReachedSet(invariants);
    return automaton.buildWitnessLocationInvariantsAutomaton(invariants);
  }

  public void extractInvariantsFromReachedSet(
      final Set<ExpressionTreeLocationInvariant> invariants) {
    Map<ManagerKey, ToFormulaVisitor> toCodeVisitorCache = Maps.newConcurrentMap();
    Map<String, ExpressionTree<AExpression>> expressionTrees = Maps.newHashMap();
    Set<ExpressionTreeLocationInvariant> expressionTreeLocationInvariants = Sets.newHashSet();
    for (AbstractState abstractState : reachedSet) {
      if (shutdownNotifier.shouldShutdown()) {
        return;
      }
      Iterable<CFANode> locations = AbstractStates.extractLocations(abstractState);
      for (AutomatonState automatonState :
          AbstractStates.asIterable(abstractState).filter(AutomatonState.class)) {
        ExpressionTree<AExpression> candidate = automatonState.getCandidateInvariants();
        String groupId = automatonState.getInternalStateName();
        if (!candidate.equals(ExpressionTrees.getTrue())) {
          ExpressionTree<AExpression> previous = expressionTrees.get(groupId);
          if (previous == null) {
            previous = ExpressionTrees.getTrue();
          }
          expressionTrees.put(groupId, And.of(previous, candidate));
          for (CFANode location : locations) {
            ExpressionTreeLocationInvariant invariant =
                new ExpressionTreeLocationInvariant(
                    groupId, location, candidate, toCodeVisitorCache);
            expressionTreeLocationInvariants.add(invariant);
          }
        }
      }
    }
    for (ExpressionTreeLocationInvariant expressionTreeLocationInvariant :
        expressionTreeLocationInvariants) {
      invariants.add(
          new ExpressionTreeLocationInvariant(
              expressionTreeLocationInvariant.getGroupId(),
              expressionTreeLocationInvariant.getLocation(),
              expressionTrees.get(expressionTreeLocationInvariant.getGroupId()),
              toCodeVisitorCache));
    }
  }

  public void extractCandidatesFromReachedSet(
      final Set<CandidateInvariant> candidates,
      final Multimap<String, CFANode> candidateGroupLocations) {
    Set<ExpressionTreeLocationInvariant> expressionTreeLocationInvariants = Sets.newHashSet();
    Map<String, ExpressionTree<AExpression>> expressionTrees = Maps.newHashMap();
    Set<CFANode> visited = Sets.newHashSet();
    Multimap<CFANode, ExpressionTreeLocationInvariant> potentialAdditionalCandidates =
        HashMultimap.create();
    Map<ManagerKey, ToFormulaVisitor> toCodeVisitorCache = Maps.newConcurrentMap();
    for (AbstractState abstractState : reachedSet) {
      if (shutdownNotifier.shouldShutdown()) {
        return;
      }
      Iterable<CFANode> locations = AbstractStates.extractLocations(abstractState);
      Iterables.addAll(visited, locations);
      for (AutomatonState automatonState :
          AbstractStates.asIterable(abstractState).filter(AutomatonState.class)) {
        ExpressionTree<AExpression> candidate = automatonState.getCandidateInvariants();
        String groupId = automatonState.getInternalStateName();
        candidateGroupLocations.putAll(groupId, locations);
        if (!candidate.equals(ExpressionTrees.getTrue())) {
          ExpressionTree<AExpression> previous = expressionTrees.get(groupId);
          if (previous == null) {
            previous = ExpressionTrees.getTrue();
          }
          expressionTrees.put(groupId, And.of(previous, candidate));
          for (CFANode location : locations) {
            potentialAdditionalCandidates.removeAll(location);
            ExpressionTreeLocationInvariant candidateInvariant =
                new ExpressionTreeLocationInvariant(
                    groupId, location, candidate, toCodeVisitorCache);
            expressionTreeLocationInvariants.add(candidateInvariant);
            // Check if there are any leaving return edges:
            // The predecessors are also potential matches for the invariant
            for (FunctionReturnEdge returnEdge :
                CFAUtils.leavingEdges(location).filter(FunctionReturnEdge.class)) {
              CFANode successor = returnEdge.getSuccessor();
              if (!candidateGroupLocations.containsEntry(groupId, successor)
                  && !visited.contains(successor)) {
                potentialAdditionalCandidates.put(
                    successor,
                    new ExpressionTreeLocationInvariant(
                        groupId, successor, candidate, toCodeVisitorCache));
              }
            }
          }
        }
      }
    }
    for (Map.Entry<CFANode, Collection<ExpressionTreeLocationInvariant>> potentialCandidates :
        potentialAdditionalCandidates.asMap().entrySet()) {
      if (!visited.contains(potentialCandidates.getKey())) {
        for (ExpressionTreeLocationInvariant candidateInvariant : potentialCandidates.getValue()) {
          candidateGroupLocations.put(
              candidateInvariant.getGroupId(), potentialCandidates.getKey());
          expressionTreeLocationInvariants.add(candidateInvariant);
        }
      }
    }
    for (ExpressionTreeLocationInvariant expressionTreeLocationInvariant :
        expressionTreeLocationInvariants) {
      for (CFANode location :
          candidateGroupLocations.get(expressionTreeLocationInvariant.getGroupId())) {
        candidates.add(
            new ExpressionTreeLocationInvariant(
                expressionTreeLocationInvariant.getGroupId(),
                location,
                expressionTrees.get(expressionTreeLocationInvariant.getGroupId()),
                toCodeVisitorCache));
      }
    }
  }

  public void extractCandidateVariablesFromReachedSet(
      final Multimap<CFANode, MemoryLocation> candidates,
      final Multimap<String, CFANode> candidateGroupLocations) {
    Set<CFANode> visited = Sets.newHashSet();
    Multimap<String, MemoryLocation> groupIDToMemoryLocation = HashMultimap.create();
    Map<String, ExpressionTree<AExpression>> expressionTrees = Maps.newHashMap();
    // TODO: considering potential Candidates because of FunctionReturnEdges
    for (AbstractState abstractState : reachedSet) {
      if (shutdownNotifier.shouldShutdown()) {
        return;
      }
      Iterable<CFANode> locations = AbstractStates.extractLocations(abstractState);
      Iterables.addAll(visited, locations);
      for (AutomatonState automatonState :
          AbstractStates.asIterable(abstractState).filter(AutomatonState.class)) {
        ExpressionTree<AExpression> candidate = automatonState.getCandidateInvariants();
        String groupId = automatonState.getInternalStateName();
        candidateGroupLocations.putAll(groupId, locations);
        if (!candidate.equals(ExpressionTrees.getTrue())) {
          ExpressionTree<AExpression> previous = expressionTrees.get(groupId);
          if (previous == null) {
            previous = ExpressionTrees.getTrue();
          }
          ExpressionTree<AExpression> candidateAnd = And.of(previous, candidate);
          groupIDToMemoryLocation.removeAll(groupId);
          Set<CExpression> variableNames = new HashSet<>();
          findVariables(candidateAnd, variableNames);
          CallstackState callstackState =
              AbstractStates.extractStateByType(abstractState, CallstackState.class);
          for (CExpression variableName : variableNames) {
            groupIDToMemoryLocation.put(
                groupId,
                MemoryLocation.valueOf(
                    callstackState.getCurrentFunction(), variableName.toString()));
          }
        }
      }
    }
    for (String groupID : candidateGroupLocations.keySet()) {
      for (MemoryLocation m : groupIDToMemoryLocation.get(groupID)) {
        for (CFANode n : candidateGroupLocations.get(groupID)) {
          String function = m.getFunctionName();
          String variable = m.getIdentifier();
          candidates.put(n, MemoryLocation.valueOf(function, variable));
        }
      }
    }
  }

  private static void findVariables(
      ExpressionTree<AExpression> expression, Set<CExpression> variableNames) {
    if (expression instanceof Or<?>) {
      Or<AExpression> expressionOr = (Or<AExpression>) expression;
      Iterator<ExpressionTree<AExpression>> operands = expressionOr.iterator();
      while (operands.hasNext()) {
        ExpressionTree<AExpression> next = operands.next();
        findVariables(next, variableNames);
      }
    } else if (expression instanceof And<?>) {
      And<AExpression> expressionAnd = (And<AExpression>) expression;
      Iterator<ExpressionTree<AExpression>> operands = expressionAnd.iterator();
      while (operands.hasNext()) {
        ExpressionTree<AExpression> next = operands.next();
        findVariables(next, variableNames);
      }
    } else if (expression instanceof LeafExpression<?>) {
      Object expressionC = ((LeafExpression<?>) expression).getExpression();
      extractCIdExpressionsfromCExpression((CExpression) expressionC, variableNames);
    }
  }

  private static void extractCIdExpressionsfromCExpression(
      CExpression expressionC, Set<CExpression> variableNames) {
    Iterable<CIdExpression> filteredExpressionsC =
        CFAUtils.getIdExpressionsOfExpression(expressionC);
    Iterator<CIdExpression> iteratorIdExpressions = filteredExpressionsC.iterator();
    while (iteratorIdExpressions.hasNext()) {
      variableNames.add(iteratorIdExpressions.next());
    }
  }

}
