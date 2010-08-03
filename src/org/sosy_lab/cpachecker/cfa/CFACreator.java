/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2010  Dirk Beyer
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
package org.sosy_lab.cpachecker.cfa;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.sosy_lab.common.Files;
import org.sosy_lab.common.LogManager;
import org.sosy_lab.common.Pair;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.configuration.Option;
import org.sosy_lab.common.configuration.Options;
import org.sosy_lab.cpachecker.cfa.objectmodel.BlankEdge;
import org.sosy_lab.cpachecker.cfa.objectmodel.CFAEdge;
import org.sosy_lab.cpachecker.cfa.objectmodel.CFAFunctionDefinitionNode;
import org.sosy_lab.cpachecker.cfa.objectmodel.CFANode;
import org.sosy_lab.cpachecker.cfa.objectmodel.c.GlobalDeclarationEdge;
import org.sosy_lab.cpachecker.exceptions.CFAGenerationRuntimeException;

import com.google.common.collect.ImmutableMap;

/**
 * Class that encapsulates the whole CFA creation process.
 */
@Options
public class CFACreator {

  @Option(name="analysis.entryFunction", regexp="^[_a-zA-Z][_a-zA-Z0-9]*$")
  private String mainFunctionName = "main";

  @Option(name="cfa.combineBlockStatements")
  private boolean combineBlockStatements = false;

  @Option(name="cfa.removeDeclarations")
  private boolean removeDeclarations = false;

  @Option(name="analysis.noExternalCalls")
  private boolean noExternalCalls = true;

  @Option(name="analysis.interprocedural")
  private boolean interprocedural = true;

  @Option(name="analysis.useGlobalVars")
  private boolean useGlobalVars = true;

  @Option(name="cfa.removeIrrelevantForErrorLocations")
  private boolean removeIrrelevantForErrorLocations = false;

  @Option(name="cfa.export")
  private boolean exportCfa = true;

  @Option(name="cfa.file", type=Option.Type.OUTPUT_FILE)
  private File exportCfaFile = new File("cfa.dot");

  private final LogManager logger;
  
  public CFACreator(Configuration config, LogManager logger) throws InvalidConfigurationException {
    config.inject(this);
    
    this.logger = logger;
  }

  public Pair<Map<String, CFAFunctionDefinitionNode>, CFAFunctionDefinitionNode> createCFA(IASTTranslationUnit ast) throws InvalidConfigurationException, CFAGenerationRuntimeException {
  
    // Build CFA
    final CFABuilder builder = new CFABuilder(logger);
    ast.accept(builder);
  
    final Map<String, CFAFunctionDefinitionNode> cfas = builder.getCFAs();
    final CFAFunctionDefinitionNode mainFunction = cfas.get(mainFunctionName);
    
    if (mainFunction == null) {
      throw new InvalidConfigurationException("Function " + mainFunctionName + " not found!");
    }
    
    // annotate CFA nodes with topological information for later use
    for(CFAFunctionDefinitionNode cfa : cfas.values()){
      CFATopologicalSort topSort = new CFATopologicalSort();
      topSort.topologicalSort(cfa);
    }
  
    // Insert call and return edges and build the supergraph
    if (interprocedural) {
      logger.log(Level.FINE, "Analysis is interprocedural, adding super edges");
  
      CPASecondPassBuilder spbuilder = new CPASecondPassBuilder(cfas, noExternalCalls);
      Set<String> calledFunctions = spbuilder.insertCallEdgesRecursively(mainFunctionName);
  
      // remove all functions which are never reached from cfas
      cfas.keySet().retainAll(calledFunctions);
    }
  
    if (useGlobalVars){
      // add global variables at the beginning of main
      insertGlobalDeclarations(mainFunction, builder.getGlobalDeclarations());
    }
  
    // simplify CFA
    if (combineBlockStatements || removeDeclarations) {
      CFASimplifier simplifier = new CFASimplifier(combineBlockStatements, removeDeclarations);
      simplifier.simplify(mainFunction);
    }

    // check the CFA of each function
    for (CFAFunctionDefinitionNode cfa : cfas.values()) {
      assert CFACheck.check(cfa);
    }
    
    // remove irrelevant locations
    if (removeIrrelevantForErrorLocations) {
      CFAReduction coi =  new CFAReduction();
      coi.removeIrrelevantForErrorLocations(mainFunction);
  
      if (mainFunction.getNumLeavingEdges() == 0) {
        logger.log(Level.INFO, "No error locations reachable from " + mainFunction.getFunctionName()
              + ", analysis not necessary. "
              + "If the code contains no error location named ERROR, set the option cfa.removeIrrelevantForErrorLocations to false.");
        return null;
      }
    }
  
    // check the super CFA starting at the main function
    assert CFACheck.check(mainFunction);
  
    // write CFA to file
    if (exportCfa) {
      try {
        Files.writeFile(exportCfaFile,
            DOTBuilder.generateDOT(cfas.values(), mainFunction), false);
      } catch (IOException e) {
        logger.log(Level.WARNING,
          "Could not write CFA to dot file, check configuration option cfa.file! (",
          e.getMessage() + ")");
        // continue with analysis
      }
    }
  
    logger.log(Level.FINE, "DONE, CFA for", cfas.size(), "functions created");
  
    return new Pair<Map<String, CFAFunctionDefinitionNode>, CFAFunctionDefinitionNode>(
        ImmutableMap.copyOf(cfas), mainFunction);
  }

  /**
   * Insert nodes for global declarations after first node of CFA.
   */
  private void insertGlobalDeclarations(final CFAFunctionDefinitionNode cfa, List<IASTDeclaration> globalVars) {
    if (globalVars.isEmpty()) {
      return;
    }
    // create a series of GlobalDeclarationEdges, one for each declaration,
    // and add them as successors of the input node
    List<CFANode> decls = new LinkedList<CFANode>();
    CFANode cur = new CFANode(0, cfa.getFunctionName());
    decls.add(cur);

    for (IASTDeclaration d : globalVars) {
      assert(d instanceof IASTSimpleDeclaration);
      IASTSimpleDeclaration sd = (IASTSimpleDeclaration)d;
      // TODO refactor this
//      if (sd.getDeclarators().length == 1 &&
//          sd.getDeclarators()[0] instanceof IASTFunctionDeclarator) {
//        if (cpaConfig.getBooleanValue("analysis.useFunctionDeclarations")) {
//          // do nothing
//        }
//        else {
//          System.out.println(d.getRawSignature());
//          continue;
//        }
//      }
      CFANode n = new CFANode(sd.getFileLocation().getStartingLineNumber(), cur.getFunctionName());
      GlobalDeclarationEdge e = new GlobalDeclarationEdge(sd,
          sd.getFileLocation().getStartingLineNumber(), cur, n);
      e.addToCFA(logger);
      decls.add(n);
      cur = n;
    }

    // split off first node of CFA
    assert cfa.getNumLeavingEdges() == 1;
    assert cfa.getLeavingSummaryEdge() == null;
    CFAEdge firstEdge = cfa.getLeavingEdge(0);
    assert firstEdge instanceof BlankEdge && !firstEdge.isJumpEdge();
    CFANode secondNode = firstEdge.getSuccessor();

    cfa.removeLeavingEdge(firstEdge);
    secondNode.removeEnteringEdge(firstEdge);

    // and add a blank edge connecting the first node of CFA with declarations
    BlankEdge be = new BlankEdge("INIT GLOBAL VARS", 0, cfa, decls.get(0));
    be.addToCFA(logger);

    // and a blank edge connecting the declarations with the second node of CFA
    be = new BlankEdge(firstEdge.getRawStatement(), firstEdge.getLineNumber(), cur, secondNode);
    be.addToCFA(logger);

    return;
  }
  
}