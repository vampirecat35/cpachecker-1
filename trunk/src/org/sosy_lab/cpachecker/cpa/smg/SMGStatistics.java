/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2018  Dirk Beyer
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
package org.sosy_lab.cpachecker.cpa.smg;

import java.io.PrintStream;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.sosy_lab.cpachecker.core.CPAcheckerResult.Result;
import org.sosy_lab.cpachecker.core.interfaces.Statistics;
import org.sosy_lab.cpachecker.core.reachedset.UnmodifiableReachedSet;
import org.sosy_lab.cpachecker.cpa.smg.join.SMGIsLessOrEqual;
import org.sosy_lab.cpachecker.util.statistics.StatCounter;
import org.sosy_lab.cpachecker.util.statistics.StatTimer;

public class SMGStatistics implements Statistics {

  final StatCounter abstractions = new StatCounter("Number of abstraction computations");
  final StatTimer totalAbstraction = new StatTimer("Total time for abstraction computation");

  @Override
  public void printStatistics(PrintStream pOut, Result pResult, UnmodifiableReachedSet pReached) {
    put(pOut, 0, SMGIsLessOrEqual.isLEQTimer);
    put(pOut, 1, SMGIsLessOrEqual.globalsTimer);
    put(pOut, 1, SMGIsLessOrEqual.stackTimer);
    put(pOut, 1, SMGIsLessOrEqual.heapTimer);
    put(pOut, 0, abstractions);
    put(pOut, 0, totalAbstraction);
  }

  @Override
  public @Nullable String getName() {
    return "SMGCPA";
  }
}
