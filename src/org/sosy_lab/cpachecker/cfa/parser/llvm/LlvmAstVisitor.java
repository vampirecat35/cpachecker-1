/*
 * CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2017  Dirk Beyer
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
package org.sosy_lab.cpachecker.cfa.parser.llvm;

import org.llvm.BasicBlock;
import org.llvm.Module;
import org.llvm.Value;
import org.sosy_lab.cpachecker.cfa.ast.c.CStatement;

/**
 * Visitor for the AST generated by our LLVM parser
 *
 * @see LlvmParser
 */
public abstract class LlvmAstVisitor {

  public enum Behavior { CONTINUE, STOP }

  public void visit(final Module pItem) {
    /* no-op at this moment */
    Behavior behavior = visitModule(pItem);
    if (behavior == Behavior.STOP) {
      return;
    } else {
      assert behavior == Behavior.CONTINUE : "Unhandled behavior type " + behavior;
    }

    /* create globals */
    iterateOverGlobals(pItem);

    /* create CFA for all functions */
    iterateOverFunctions(pItem);
  }

  private void iterateOverGlobals(final Module pItem) {
    Value globalItem = pItem.getFirstGlobal();
    /* no globals? */
    if (globalItem == null)
      return;

    Value globalItemLast = pItem.getLastGlobal();
    assert globalItemLast != null;

    while (true) {
      Behavior behavior = visitGlobalItem(globalItem);
      if (behavior == Behavior.CONTINUE) {
        /* we processed the last global variable? */
        if (globalItem.equals(globalItemLast))
          break;

        globalItem = globalItem.getNextGlobal();

      } else {
        assert behavior == Behavior.STOP : "Unhandled behavior type " + behavior;
        return;
      }
    }
  }

  private void iterateOverFunctions(final Module pItem) {
    Value func = pItem.getFirstFunction();
    if (func == null)
      return;

    Value funcLast = pItem.getFirstFunction();
    assert funcLast != null;

    while (true) {
      /* skip declarations */
      if (func.isDeclaration()) {
        if (func.equals(funcLast))
          break;

        func = func.getNextFunction();
        continue;
      }

      Behavior behavior = visitInFunction(func);
      if (behavior == Behavior.CONTINUE) {
        handleBasicBlocks(func);

        if (func.equals(funcLast))
          break;

        func = func.getNextFunction();
      } else {
        assert behavior == Behavior.STOP : "Unhandled behavior type " + behavior;
        return;
      }
    }
  }

  private void handleBasicBlocks(final Value pItem) {
    assert pItem.isFunction();

    BasicBlock BB = pItem.getFirstBasicBlock();
    if (BB == null)
      return;

    BasicBlock lastBB = pItem.getLastBasicBlock();
    assert lastBB != null;

    while (true) {
      /* process this basic block */
      Behavior behavior = visitBasicBlock(BB);
      if (behavior == Behavior.CONTINUE) {
        handleInstructions(BB);

        /* did we processed all basic blocks? */
        if (BB.equals(lastBB))
          break;

        BB = BB.getNextBasicBlock();
      } else {
        assert behavior == Behavior.STOP : "Unhandled behavior type " + behavior;
        return;
      }
    }
  }

  private void handleInstructions(final BasicBlock pItem) {
    Value I = pItem.getFirstInstruction();
    if (I == null)
      return;

    Value lastI = pItem.getLastInstruction();
    assert lastI != null;

    while (true) {
      /* process this basic block */
      Behavior behavior = visitInstruction(I);
      if (behavior == Behavior.CONTINUE) {
        /* did we processed all basic blocks? */
        if (I.equals(lastI))
          break;

        I = I.getNextInstruction();
      } else {
        assert behavior == Behavior.STOP : "Unhandled behavior type " + behavior;
        return;
      }
    }
  }

  protected abstract Behavior visitModule(final Module pItem);
  protected abstract Behavior visitInFunction(final Value pItem);
  protected abstract Behavior visitBasicBlock(final BasicBlock pItem);
  protected abstract CStatement visitInstruction(final Value pItem);
  protected abstract Behavior visitGlobalItem(final Value pItem);
}
