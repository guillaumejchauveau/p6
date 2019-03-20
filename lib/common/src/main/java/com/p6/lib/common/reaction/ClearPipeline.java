package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * Erases the content of the pipeline.
 */
public class ClearPipeline implements ReactionPipelineStep {
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    return new ArrayList<>();
  }
}
