package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.integers.IntegerElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
abstract class IntegerReactionPipelineStep implements ReactionPipelineStep {
  /**
   * @param inputElements
   * @param cell
   * @return
   */
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    List<IntegerElement> integerElements = new ArrayList<>();
    for (Element element : inputElements) {
      if (!(element instanceof IntegerElement)) {
        return null;
      }
      integerElements.add((IntegerElement) element);
    }
    return this.integerTest(integerElements, cell);
  }

  /**
   * @param elements
   * @param cell
   * @return
   */
  abstract List<Element> integerTest(List<IntegerElement> elements, Cell cell);
}
