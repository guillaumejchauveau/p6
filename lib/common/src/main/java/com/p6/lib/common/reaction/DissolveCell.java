package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Element;

/**
 *
 */
public class DissolveCell implements ReactionPipelineStep {
  /**
   *
   */
  public void handle(List<Element> elements, Cell cell) {
    cell.dissolve();
  }
}
