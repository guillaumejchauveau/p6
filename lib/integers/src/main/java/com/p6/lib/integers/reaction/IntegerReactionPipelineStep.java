package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Element;
import com.p6.core.solution.Cell;
import com.p6.lib.integers.IntegerElement;

abstract class IntegerReactionPipelineStep implements ReactionPipelineStep {
  public void handle(List<Element> elements, Cell cell) {
    boolean allIntegers = true;
    for (Element element : elements) {
      if (!(element instanceof IntegerElement)) {
        allIntegers = false;
        break;
      }
    }
    if (allIntegers) {
      this.integerTest((List<IntegerElement>) elements, Cell cell);
    }
    return false;
  }

  abstract void integerTest(List<IntegerElement> elements, Cell cell);
}
