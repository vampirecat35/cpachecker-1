/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2012  Dirk Beyer
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
 *
 *
 *  CPAchecker web page:
 *    http://cpachecker.sosy-lab.org
 */
package org.sosy_lab.cpachecker.cpa.predicate;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sosy_lab.cpachecker.util.AbstractStates.extractStateByType;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.Nullable;

import org.sosy_lab.common.collect.PersistentMap;
import org.sosy_lab.cpachecker.cfa.model.CFANode;
import org.sosy_lab.cpachecker.core.interfaces.AbstractState;
import org.sosy_lab.cpachecker.core.interfaces.NonMergeableAbstractState;
import org.sosy_lab.cpachecker.core.interfaces.Partitionable;
import org.sosy_lab.cpachecker.core.interfaces.Targetable;
import org.sosy_lab.cpachecker.util.predicates.AbstractionFormula;
import org.sosy_lab.cpachecker.util.predicates.interfaces.BooleanFormulaManager;
import org.sosy_lab.cpachecker.util.predicates.pathformula.PathFormula;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * AbstractState for Symbolic Predicate Abstraction CPA
 */
public abstract class PredicateAbstractState implements AbstractState, Partitionable, Targetable, Serializable {

  private static final long serialVersionUID = -265763837277453447L;

  public final static Predicate<PredicateAbstractState> FILTER_ABSTRACTION_STATES = new Predicate<PredicateAbstractState>() {
    @Override
    public boolean apply(PredicateAbstractState ae) {
      return ae.isAbstractionState();
    }
  };

  public static PredicateAbstractState getPredicateState(AbstractState pState) {
    return checkNotNull(extractStateByType(pState, PredicateAbstractState.class));
  }

  /**
   * Marker type for abstract states that were generated by computing an
   * abstraction.
   */
  private static class AbstractionState extends PredicateAbstractState implements NonMergeableAbstractState {

    private static final long serialVersionUID = 8341054099315063986L;

    private AbstractionState(BooleanFormulaManager bfmgr, PathFormula pf,
        AbstractionFormula pA, PersistentMap<CFANode, Integer> pAbstractionLocations,
        @Nullable ViolatedProperty pViolatedProperty,
		Set<Integer> pReuseStateId) {
      super(pf, pA, pAbstractionLocations, pViolatedProperty, pReuseStateId);
      // Check whether the pathFormula of an abstraction element is just "true".
      // partialOrder relies on this for optimization.
      //Preconditions.checkArgument(bfmgr.isTrue(pf.getFormula()));
      // Check uncommented because we may pre-initialize the path formula
      // with an invariant.
      // This is no problem for the partial order because the invariant
      // is always the same when the location is the same.
    }

    @Override
    public Object getPartitionKey() {
      if (super.abstractionFormula.isFalse()) {
        // put unreachable states in a separate partition to avoid merging
        // them with any reachable states
        return Boolean.FALSE;
      } else {
        return null;
      }
    }

    @Override
    public boolean isAbstractionState() {
      return true;
    }

    @Override
    public String toString() {
      return "Abstraction location: true, Abstraction: " + super.abstractionFormula;
    }
  }

  private static class NonAbstractionState extends PredicateAbstractState {
    private static final long serialVersionUID = -6912172362012773999L;
    /**
     * The abstract state this element was merged into.
     * Used for fast coverage checks.
     */
    private transient PredicateAbstractState mergedInto = null;

    private NonAbstractionState(PathFormula pF, AbstractionFormula pA,
        PersistentMap<CFANode, Integer> pAbstractionLocations,
        @Nullable ViolatedProperty pViolatedProperty,
		Set<Integer> pReuseStateId) {
      super(pF, pA, pAbstractionLocations, pViolatedProperty, pReuseStateId);
    }

    @Override
    public boolean isAbstractionState() {
      return false;
    }

    @Override
    PredicateAbstractState getMergedInto() {
      return mergedInto;
    }

    @Override
    void setMergedInto(PredicateAbstractState pMergedInto) {
      Preconditions.checkNotNull(pMergedInto);
      mergedInto = pMergedInto;
    }

    @Override
    public Object getPartitionKey() {
      return getAbstractionFormula();
    }

    @Override
    public String toString() {
      return "Abstraction location: false";
    }
  }

  static class ComputeAbstractionState extends PredicateAbstractState {

    private static final long serialVersionUID = -3961784113582993743L;
    private transient final CFANode location;

    public ComputeAbstractionState(PathFormula pf, AbstractionFormula pA,
        CFANode pLoc, PersistentMap<CFANode, Integer> pAbstractionLocations,
        @Nullable ViolatedProperty pViolatedProperty,
        Set<Integer> pReuseStateId) {
      super(pf, pA, pAbstractionLocations, pViolatedProperty, pReuseStateId);
      location = pLoc;
    }

    @Override
    public boolean isAbstractionState() {
      return false;
    }

    @Override
    public Object getPartitionKey() {
      return this;
    }

    @Override
    public String toString() {
      return "Abstraction location: true, Abstraction: <TO COMPUTE>";
    }

    public CFANode getLocation() {
      return location;
    }
  }

  static PredicateAbstractState mkAbstractionState(BooleanFormulaManager bfmgr,
      PathFormula pF, AbstractionFormula pA,
      PersistentMap<CFANode, Integer> pAbstractionLocations,
      @Nullable ViolatedProperty pViolatedProperty,
      Set<Integer> pReusedStateIds) {
    return new AbstractionState(bfmgr, pF, pA, pAbstractionLocations, pViolatedProperty, pReusedStateIds);
  }

  static PredicateAbstractState mkNonAbstractionStateWithNewPathFormula(
      PathFormula pF,
      @Nullable ViolatedProperty pViolatedProperty,
      PredicateAbstractState oldState,
      Set<Integer> pReusedStateIds) {
    return new NonAbstractionState(pF, oldState.getAbstractionFormula(),
        oldState.getAbstractionLocationsOnPath(), pViolatedProperty, pReusedStateIds);
  }

  /** The path formula for the path from the last abstraction node to this node.
   * it is set to true on a new abstraction location and updated with a new
   * non-abstraction location */
  private PathFormula pathFormula;

  /** The abstraction which is updated only on abstraction locations */
  private AbstractionFormula abstractionFormula;

  /** How often each abstraction location was visited on the path to the current state. */
  private final PersistentMap<CFANode, Integer> abstractionLocations;

  private final Set<Integer> positionInReuseGraph;

  private final @Nullable ViolatedProperty violatedProperty;

  private PredicateAbstractState(PathFormula pf, AbstractionFormula a,
      PersistentMap<CFANode, Integer> pAbstractionLocations,
      @Nullable ViolatedProperty pViolatedProperty,
      Set<Integer> pReuseStateId) {
    this.pathFormula = pf;
    this.abstractionFormula = a;
    this.abstractionLocations = pAbstractionLocations;
    this.violatedProperty = pViolatedProperty;
    this.positionInReuseGraph = pReuseStateId;
  }

  public abstract boolean isAbstractionState();

  PredicateAbstractState getMergedInto() {
    throw new UnsupportedOperationException("Assuming wrong PredicateAbstractStates were merged!");
  }

  void setMergedInto(PredicateAbstractState pMergedInto) {
    throw new UnsupportedOperationException("Merging wrong PredicateAbstractStates!");
  }

  @Override
  public boolean isTarget() {
    return violatedProperty != null;
  }

  @Override
  public @Nullable ViolatedProperty getViolatedProperty() throws IllegalStateException {
    // for simplicity, a violation of the contract of this method:
    // may return null
    return violatedProperty;
  }

  public PersistentMap<CFANode, Integer> getAbstractionLocationsOnPath() {
    return abstractionLocations;
  }



  public AbstractionFormula getAbstractionFormula() {
    return abstractionFormula;
  }

  /**
   * Replace the abstraction formula part of this element.
   * THIS IS POTENTIALLY UNSOUND!
   *
   * Call this function only during refinement if you also change all successor
   * elements and consider the coverage relation.
   */
  void setAbstraction(AbstractionFormula pAbstractionFormula) {
    if (isAbstractionState()) {
      abstractionFormula = checkNotNull(pAbstractionFormula);
    } else {
      throw new UnsupportedOperationException("Changing abstraction formula is only supported for abstraction elements");
    }
  }

  /**
   * Replace the path formula part of this element.
   * THIS IS POTENTIALLY UNSOUND!
   */
  public void setPathFormula(PathFormula pPathFormula) {
    pathFormula = checkNotNull(pPathFormula);
  }

  public PathFormula getPathFormula() {
    return pathFormula;
  }


  public Set<Integer> getPositionInReuseGraph() {
    return positionInReuseGraph;
  }

}