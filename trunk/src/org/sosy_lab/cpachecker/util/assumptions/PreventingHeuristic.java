/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2014  Dirk Beyer
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
package org.sosy_lab.cpachecker.util.assumptions;

import org.sosy_lab.cpachecker.util.predicates.smt.FormulaManagerView;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.IntegerFormulaManager;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;

/**
 * Enum listing several possible reasons for giving up analysis at a certain point.
 */
public enum PreventingHeuristic {
  PATHLENGTH("PL"),
  SUCCESSORCOMPTIME("SCT"),
  PATHCOMPTIME("PCT"),
  ASSUMEEDGESINPATH("AEIP"),
  ASSIGNMENTSINPATH("ASIP"),
  REPETITIONSINPATH("RIP"),
  MEMORYUSED("MU"),
  MEMORYOUT("MO"),
  TIMEOUT("TO"),
  LOOPITERATIONS("LI");

  private final String predicateString;

  PreventingHeuristic(String predicateStr) {
    predicateString = predicateStr;
  }

  /**
   * Returns a formula of this reason, which includes the
   * threshold value which was exceeded.
   */
  public BooleanFormula getFormula(FormulaManagerView fmgr, long thresholdValue) {
    IntegerFormulaManager nfmgr = fmgr.getIntegerFormulaManager();
    final IntegerFormula number = nfmgr.makeNumber(thresholdValue);
    final IntegerFormula var = nfmgr.makeVariable(predicateString);
    // TODO better idea?
    return nfmgr.equal(var, number);
  }
}