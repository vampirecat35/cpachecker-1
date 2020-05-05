/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2020  Dirk Beyer
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
package org.sosy_lab.cpachecker.cfa.model.timedautomata;

import java.util.Set;
import org.sosy_lab.cpachecker.cfa.ast.FileLocation;
import org.sosy_lab.cpachecker.cfa.ast.c.CAssignment;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpression;
import org.sosy_lab.cpachecker.cfa.model.AbstractCFAEdge;
import org.sosy_lab.cpachecker.cfa.model.CFAEdgeType;
import org.sosy_lab.cpachecker.cfa.model.CFANode;

public class TCFAEdge extends AbstractCFAEdge {

  private final Set<CAssignment> resetStatements;
  private final CExpression guard;

  private static final long serialVersionUID = 5472749446453717391L;

  public TCFAEdge(
      FileLocation pFileLocation,
      CFANode pPredecessor,
      CFANode pSuccessor,
      CExpression pGuard,
      Set<CAssignment> pResetStatements) {
    super(getStatement(pGuard), pFileLocation, pPredecessor, pSuccessor);
    resetStatements = pResetStatements;
    guard = pGuard;
  }

  private static String getStatement(CExpression pGuard) {
    return pGuard.toASTString();
  }

  public Set<CAssignment> getResetStatements() {
    return resetStatements;
  }

  public CExpression getGuard() {
    return guard;
  }

  @Override
  public CFAEdgeType getEdgeType() {
    return CFAEdgeType.TimedAutomatonEdge;
  }

  @Override
  public String getCode() {
    return guard.toASTString();
  }
}
