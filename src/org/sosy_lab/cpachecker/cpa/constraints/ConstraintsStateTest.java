/*
 * CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2015  Dirk Beyer
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
package org.sosy_lab.cpachecker.cpa.constraints;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.sosy_lab.cpachecker.cfa.types.Type;
import org.sosy_lab.cpachecker.cfa.types.c.CNumericTypes;
import org.sosy_lab.cpachecker.cpa.constraints.constraint.Constraint;
import org.sosy_lab.cpachecker.cpa.constraints.constraint.IdentifierAssignment;
import org.sosy_lab.cpachecker.cpa.value.symbolic.type.SymbolicExpression;
import org.sosy_lab.cpachecker.cpa.value.symbolic.type.SymbolicIdentifier;
import org.sosy_lab.cpachecker.cpa.value.symbolic.type.SymbolicValueFactory;
import org.sosy_lab.cpachecker.cpa.value.type.NumericValue;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

/**
 * Unit tests for {@link ConstraintsState}.
 */
public class ConstraintsStateTest {

  private final SymbolicValueFactory factory = SymbolicValueFactory.getInstance();

  private final IdentifierAssignment emptyDefiniteAssignment = new IdentifierAssignment();

  private final Type defType = CNumericTypes.INT;

  private final SymbolicIdentifier id1 = factory.newIdentifier();
  private final SymbolicIdentifier id2 = factory.newIdentifier();
  private final SymbolicIdentifier alias1 = factory.newIdentifier();
  private final SymbolicIdentifier alias2 = factory.newIdentifier();

  private final SymbolicExpression idExp1 = factory.asConstant(id1,
                                                               defType);
  private final SymbolicExpression idExp2 = factory.asConstant(id2,
                                                               defType);
  private final SymbolicExpression aliasExp1 = factory.asConstant(alias1,
                                                                  defType);
  private final SymbolicExpression aliasExp2 = factory.asConstant(alias2,
                                                                  defType);

  private final NumericValue num1 = new NumericValue(5);
  private final SymbolicExpression numExp1 = factory.asConstant(num1, defType);

  private final Constraint c1 = (Constraint) factory.lessThan(idExp1, numExp1, defType, defType);
  private final Constraint c2 = (Constraint) factory.equal(idExp2, idExp1, defType, defType);
  private final Constraint cAlias1 = (Constraint) factory.lessThan(aliasExp1, numExp1, defType, defType);
  private final Constraint cAlias2 = (Constraint) factory.equal(aliasExp2, aliasExp1, defType, defType);


  @Test
  public void testIsLessOrEqual_reflexive() {
    ConstraintsState state = new ConstraintsState(getConstraintSet(), emptyDefiniteAssignment);

    Assert.assertTrue(state.isLessOrEqual(state));
  }

  @Test
  public void testIsLessOrEqual_BiggerOneEmpty() {
    ConstraintsState emptyState = new ConstraintsState();
    ConstraintsState state = new ConstraintsState(getConstraintSet(), emptyDefiniteAssignment);

    Assert.assertTrue(state.isLessOrEqual(emptyState));
  }

  @Test
  public void testIsLessOrEqual_BiggerOneSubsetWithSameIdentifierIds() {
    ConstraintsState state = new ConstraintsState(getConstraintSet(), emptyDefiniteAssignment);
    Set<Constraint> subset = getConstraintSet();
    subset.remove(Iterables.get(subset, 0));
    ConstraintsState subsetState = new ConstraintsState(subset, emptyDefiniteAssignment);

    Assert.assertTrue(state.isLessOrEqual(subsetState));
  }

  @Test
  public void testIsLessOrEqual_BiggerOneSubsetWithOtherIdentifierIds() {
    ConstraintsState state = new ConstraintsState(getConstraintSet(), emptyDefiniteAssignment);
    Set<Constraint> subset = getAliasConstraintSet();
    subset.remove(Iterables.get(subset, 0));
    ConstraintsState subsetState = new ConstraintsState(subset, emptyDefiniteAssignment);

    Assert.assertTrue(state.isLessOrEqual(subsetState));
  }

  @Test
  public void testIsLessOrEqual_DefiniteAssignmentVsEqualsExpression() {
    Constraint lesserConstraint = factory.equal(idExp1, numExp1, defType, defType);
    Set<Constraint> constraints = ImmutableSet.of(lesserConstraint);

    ConstraintsState biggerState = new ConstraintsState(constraints, emptyDefiniteAssignment);

    IdentifierAssignment definiteAssignment = new IdentifierAssignment();
    definiteAssignment.put(id1, num1);

    ConstraintsState lesserState = new ConstraintsState(Collections.<Constraint>emptySet(),
                                                        definiteAssignment);
    Assert.assertTrue(lesserState.isLessOrEqual(biggerState));
  }

  private Set<Constraint> getConstraintSet() {
    Set<Constraint> set = new HashSet<>();

    set.add(c1);
    set.add(c2);

    return set;
  }

  private Set<Constraint> getAliasConstraintSet() {
    Set<Constraint> set = new HashSet<>();

    set.add(cAlias1);
    set.add(cAlias2);

    return set;
  }
}
