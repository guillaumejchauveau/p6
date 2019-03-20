package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.integers.IntegerElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A base class that checks if all elements are integer elements and casts automatically.
 */
abstract class IntegerReactionPipelineStep implements ReactionPipelineStep {
  /**
   * {@inheritDoc}
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
   * The method that actually handles the integer elements.
   *
   * @param elements The integer elements to handle
   * @param cell     The current cell
   * @return The output elements
   */
  abstract List<Element> integerTest(List<IntegerElement> elements, Cell cell);
}
