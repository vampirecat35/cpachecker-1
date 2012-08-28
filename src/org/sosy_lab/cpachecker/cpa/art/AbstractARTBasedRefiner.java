/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2011  Dirk Beyer
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
package org.sosy_lab.cpachecker.cpa.art;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.sosy_lab.common.LogManager;
import org.sosy_lab.common.Pair;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.cpachecker.cfa.objectmodel.CFAEdge;
import org.sosy_lab.cpachecker.cfa.objectmodel.c.FunctionCallEdge;
import org.sosy_lab.cpachecker.core.CounterexampleInfo;
import org.sosy_lab.cpachecker.core.interfaces.AbstractElement;
import org.sosy_lab.cpachecker.core.interfaces.ConfigurableProgramAnalysis;
import org.sosy_lab.cpachecker.core.interfaces.Refiner;
import org.sosy_lab.cpachecker.core.interfaces.WrapperCPA;
import org.sosy_lab.cpachecker.core.reachedset.ReachedSet;
import org.sosy_lab.cpachecker.exceptions.CPAException;
import org.sosy_lab.cpachecker.exceptions.RefinementFailedException;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

public abstract class AbstractARTBasedRefiner implements Refiner {

  private final ARTCPA mArtCpa;
  private final LogManager logger;

  protected AbstractARTBasedRefiner(ConfigurableProgramAnalysis pCpa) throws InvalidConfigurationException {
    if (pCpa instanceof WrapperCPA) {
      mArtCpa = ((WrapperCPA) pCpa).retrieveWrappedCpa(ARTCPA.class);
    } else {
      throw new InvalidConfigurationException("ART CPA needed for refinement");
    }
    if (mArtCpa == null) {
      throw new InvalidConfigurationException("ART CPA needed for refinement");
    }
    this.logger = mArtCpa.getLogger();
  }

  protected final ARTCPA getArtCpa() {
    return mArtCpa;
  }

  private static final Function<Pair<ARTElement, CFAEdge>, String> pathToFunctionCalls
        = new Function<Pair<ARTElement, CFAEdge>, String>() {
    @Override
    public String apply(Pair<ARTElement,CFAEdge> arg) {

      if (arg.getSecond() instanceof FunctionCallEdge) {
        FunctionCallEdge funcEdge = (FunctionCallEdge)arg.getSecond();
        return "line " + funcEdge.getLineNumber() + ":\t" + funcEdge.getRawStatement();
      } else {
        return null;
      }
    }
  };

  @Override
  public final boolean performRefinement(ReachedSet pReached) throws CPAException, InterruptedException {
    return performRefinementWithInfo(pReached).isSpurious();
  }

  /**
   * This method does the same as {@link #performRefinement(ReachedSet)},
   * but it returns some more information about the refinement.
   */
  public final CounterexampleInfo performRefinementWithInfo(ReachedSet pReached) throws CPAException, InterruptedException {
    logger.log(Level.FINEST, "Starting ART based refinement");
    mArtCpa.clearCounterexample();

    assert checkART(pReached) : "ART and reached set do not match before refinement";

    AbstractElement lastElement = pReached.getLastElement();
    assert lastElement instanceof ARTElement : "Element in reached set which is not an ARTElement";
    assert ((ARTElement)lastElement).isTarget() : "Last element in reached is not a target element before refinement";
    ARTReachedSet reached = new ARTReachedSet(pReached, mArtCpa);

    Path path = computePath((ARTElement)lastElement, reached);

    if (logger.wouldBeLogged(Level.ALL) && path != null) {
      logger.log(Level.ALL, "Error path:\n", path);
      logger.log(Level.ALL, "Function calls on Error path:\n",
          Joiner.on("\n ").skipNulls().join(Collections2.transform(path, pathToFunctionCalls)));
    }

    CounterexampleInfo counterexample;
    try {
      counterexample = performRefinement(reached, path);
    } catch (RefinementFailedException e) {
      if (e.getErrorPath() == null) {
        e.setErrorPath(path);
      }

      // set the path from the exception as the target path
      // so it can be used for debugging
      mArtCpa.setCounterexample(CounterexampleInfo.feasible(e.getErrorPath(), null));
      throw e;
    }

    assert checkART(pReached) : "ART and reached set do not match after refinement";

    if (!counterexample.isSpurious()) {
      Path targetPath = counterexample.getTargetPath();

      // new targetPath must contain root and error node
      assert targetPath.getFirst().getFirst() == path.getFirst().getFirst() : "Target path from refiner does not contain root node";
      assert targetPath.getLast().getFirst()  == path.getLast().getFirst() : "Target path from refiner does not contain target state";

      mArtCpa.setCounterexample(counterexample);
    }

    logger.log(Level.FINEST, "ART based refinement finished, result is", counterexample.isSpurious());

    return counterexample;
  }


  /**
   * Perform refinement.
   * @param pReached
   * @param pPath
   * @return Information about the counterexample.
   * @throws InterruptedException
   */
  protected abstract CounterexampleInfo performRefinement(ARTReachedSet pReached, Path pPath)
            throws CPAException, InterruptedException;

  /**
   * This method may be overwritten if the standard behavior of <code>ARTUtils.getOnePathTo()</code> is not
   * appropriate in the implementations context.
   *
   * TODO: Currently this function may return null.
   *
   * @param pLastElement Last ARTElement of the given reached set
   * @param pReached ReachedSet
   * @see org.sosy_lab.cpachecker.cpa.art.ARTUtils
   * @return
   * @throws InterruptedException
   */
  protected Path computePath(ARTElement pLastElement, ARTReachedSet pReached) throws InterruptedException, CPAException {
    return ARTUtils.getOnePathTo(pLastElement);
  }

  public static boolean checkART(ReachedSet pReached) {

    Deque<AbstractElement> workList = new ArrayDeque<AbstractElement>();
    Set<ARTElement> art = new HashSet<ARTElement>();

    workList.add(pReached.getFirstElement());
    while (!workList.isEmpty()) {
      ARTElement currentElement = (ARTElement)workList.removeFirst();
      assert !currentElement.isDestroyed();

      for (ARTElement parent : currentElement.getParents()) {
        assert parent.getChildren().contains(currentElement) : "Reference from parent to child is missing in ART";
      }
      for (ARTElement child : currentElement.getChildren()) {
        assert child.getParents().contains(currentElement) : "Reference from child to parent is missing in ART";
      }

      // check if (e \in ART) => (e \in Reached ^ e.isCovered())
      if (currentElement.isCovered()) {
        assert !pReached.contains(currentElement) : "Reached set contains covered element";

      } else {
        // There is a special case here:
        // If the element is the sibling of the target element, it might have not
        // been added to the reached set if CPAAlgorithm stopped before.
        // But in this case its parent is in the waitlist.

        assert pReached.contains(currentElement)
            || pReached.getWaitlist().containsAll(currentElement.getParents())
            : "Element in ART but not in reached set: " + currentElement;
      }

      if (art.add(currentElement)) {
        workList.addAll(currentElement.getChildren());
      }
    }

    // check if (e \in Reached) => (e \in ART)
    assert art.containsAll(pReached.getReached()) : "Element in reached set but not in ART";

    return true;
  }
}
