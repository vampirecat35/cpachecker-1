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
package org.sosy_lab.cpachecker.cfa.ast.js;

import org.sosy_lab.cpachecker.cfa.ast.FileLocation;
import org.sosy_lab.cpachecker.cfa.types.js.JSAnyType;
import org.sosy_lab.cpachecker.cfa.types.js.JSType;

public class JSDeclaredByExpression implements JSExpression {
  private static final long serialVersionUID = 5603190731092695447L;
  private final JSIdExpression idExpression;
  private final JSFunctionDeclaration jsFunctionDeclaration;

  public JSDeclaredByExpression(
      final JSIdExpression pIdExpression, final JSFunctionDeclaration pJsFunctionDeclaration) {
    idExpression = pIdExpression;
    jsFunctionDeclaration = pJsFunctionDeclaration;
  }

  @Override
  public <R, X extends Exception> R accept(final JSExpressionVisitor<R, X> v) throws X {
    return v.visit(this);
  }

  @Override
  public JSType getExpressionType() {
    return JSAnyType.ANY;
  }

  @Override
  public FileLocation getFileLocation() {
    return FileLocation.DUMMY;
  }

  @Override
  public String toASTString(final boolean pQualified) {
    return pQualified
        ? idExpression.getName()
            + " declaredBy "
            + jsFunctionDeclaration.getQualifiedName()
        : idExpression.getName() + " declaredBy " + jsFunctionDeclaration.getName();
  }

  @Override
  public String toParenthesizedASTString(final boolean pQualified) {
    return "\"" + toASTString(pQualified) + "\"";
  }

  public JSIdExpression getIdExpression() {
    return idExpression;
  }

  public JSFunctionDeclaration getJsFunctionDeclaration() {
    return jsFunctionDeclaration;
  }
}
