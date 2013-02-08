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
package org.sosy_lab.cpachecker.util.predicates.interfaces.basicimpl;

import static com.google.common.collect.FluentIterable.from;

import java.util.List;

import org.sosy_lab.cpachecker.util.predicates.interfaces.Formula;
import org.sosy_lab.cpachecker.util.predicates.interfaces.FormulaType;
import org.sosy_lab.cpachecker.util.predicates.interfaces.FunctionFormulaManager;
import org.sosy_lab.cpachecker.util.predicates.interfaces.FunctionFormulaType;
import org.sosy_lab.cpachecker.util.predicates.interfaces.FunctionFormulaTypeImpl;

import com.google.common.base.Function;

/**
 * This class simplifies the implementation of the FunctionFormulaManager by converting the types to the solver specific type.
 * It depends on UnsafeFormulaManager to make clear that the UnsafeFormulaManager should not depend on FunktionFormulaManager.
 * @param <TFormulaInfo> The solver specific type.
 */
public abstract class AbstractFunctionFormulaManager<TFormulaInfo>
    extends AbstractBaseFormulaManager<TFormulaInfo>
    implements FunctionFormulaManager {

  private AbstractUnsafeFormulaManager<TFormulaInfo> unsafeManager;

  public AbstractFunctionFormulaManager(
      FormulaCreator<TFormulaInfo> pCreator,
      AbstractUnsafeFormulaManager<TFormulaInfo> unsafeManager) {
    super(pCreator);
    this.unsafeManager = unsafeManager;
  }

  @SuppressWarnings("unchecked")
  protected <T extends AbstractUnsafeFormulaManager<TFormulaInfo>> T getUnsafeManager(){
    return (T)unsafeManager;
  }

  @Override
  public <T extends Formula> FunctionFormulaType<T> createFunction(String pName, FormulaType<T> pReturnType,
      List<FormulaType<? extends Formula>> pArgs) {
    return new FunctionFormulaTypeImpl<>(pReturnType, pArgs);
  }

  protected abstract <TFormula extends Formula> TFormulaInfo
    createUninterpretedFunctionCallImpl(FunctionFormulaType<TFormula> pFuncType, List<TFormulaInfo> pArgs);

  @SuppressWarnings("unchecked")
  @Override
  public final <T extends Formula> T createUninterpretedFunctionCall(FunctionFormulaType<T> pFuncType, List<? extends Formula> pArgs) {
    FormulaType<T> retType = pFuncType.getReturnType();
    List<TFormulaInfo> list =
      from(pArgs)
        .transform(new Function<Formula, TFormulaInfo>() {
          @SuppressWarnings("unchecked")
          @Override
          public TFormulaInfo apply(Formula pArg0) {
            return
                getFormulaCreator().extractInfo(pArg0);
          }})
        .toImmutableList();
    TFormulaInfo formulaInfo = createUninterpretedFunctionCallImpl(pFuncType, list);
    return unsafeManager.typeFormula(retType, formulaInfo);
  }

  public abstract <T extends Formula> boolean isUninterpretedFunctionCall(FunctionFormulaType<T> pFuncType, TFormulaInfo pF);

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Formula> boolean isUninterpretedFunctionCall(FunctionFormulaType<T> pFuncType, T pF) {
    return isUninterpretedFunctionCall(pFuncType, getFormulaCreator().extractInfo(pF));
  }


}
