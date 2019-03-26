package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.List;

/**
 * Checks if the pipeline's elements' values are different to at least one element (the first).
 * If they are not, stops the pipeline.
 */
public class NotEquals implements ReactionPipelineStep {
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    for (int i = 1; i < inputElements.size(); i++) {
      if (inputElements.get(i).equals(inputElements.get(0))) {
        return null;
      }
    }
    return inputElements;
  }
}
