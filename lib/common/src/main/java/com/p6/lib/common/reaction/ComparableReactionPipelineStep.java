package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.common.solution.ComparableElement;
import java.util.ArrayList;
import java.util.List;

public abstract class ComparableReactionPipelineStep<T extends Comparable<T>>
    implements ReactionPipelineStep {
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    var comparableElements = new ArrayList<ComparableElement<T>>();
    for (var element : inputElements) {
      if (!(element instanceof ComparableElement)) {
        return null;
      }
      // Checks if the value of the element is comparable with the other element values.
      if (!this.getComparisonClass().isInstance(element.evaluate())) {
        return null;
      }
      comparableElements.add((ComparableElement<T>) element);
    }
    return this.comparableTest(comparableElements, cell);
  }

  protected abstract Class<T> getComparisonClass();

  /**
   * The method that actually handles the comparable elements.
   *
   * @param elements The comparable elements to handle. The list is safe from the original pipeline
   *                 input
   * @param cell     The current cell
   * @return The output elements
   */
  protected abstract List<Element> comparableTest(List<ComparableElement<T>> elements, Cell cell);
}
