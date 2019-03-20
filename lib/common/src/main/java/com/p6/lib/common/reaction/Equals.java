package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.List;

/**
 * Checks if the pipeline's elements' values are the same using their implementation of
 * {@link Object#equals}. If they are not, stops the pipeline.
 */
public class Equals implements ReactionPipelineStep {
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    for (int i = 1; i < inputElements.size(); i++) {
      if (!inputElements.get(i).evaluate().equals(inputElements.get(0).evaluate())) {
        return null;
      }
    }
    return inputElements;
  }
}
