package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.common.reaction.NotEquals;
import com.p6.lib.common.reaction.Sort;
import java.util.List;

/**
 *
 */
public class Greater implements ReactionPipelineStep {
  private NotEquals notEquals;
  private Sort sort;

  /**
   * @param elementSide
   */
  public Greater(Element.Side elementSide) {
    this.notEquals = new NotEquals();
    this.sort = new Sort(elementSide);
  }

  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    List<Element> output = this.notEquals.handle(inputElements, cell);
    if (output != null) {
      return this.sort.handle(output, cell);
    }
    return null;
  }
}
