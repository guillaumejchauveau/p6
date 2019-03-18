package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.List;

/**
 *
 */
public class NotEquals implements ReactionPipelineStep {
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    for (int i = 1; i < inputElements.size(); i++) {
      if (inputElements.get(i).evaluate().equals(inputElements.get(0).evaluate())) {
        return null;
      }
    }
    return inputElements;
  }
}
