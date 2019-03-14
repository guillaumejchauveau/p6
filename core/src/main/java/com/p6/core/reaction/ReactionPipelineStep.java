package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.List;

/**
 *
 */
@FunctionalInterface
public interface ReactionPipelineStep {
  /**
   * @param inputElements
   * @param cell
   * @return
   */
  List<? extends Element> handle(List<Element> inputElements, Cell cell);
}
