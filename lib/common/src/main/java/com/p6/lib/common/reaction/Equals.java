package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.List;

public class Equals implements ReactionPipelineStep {
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    for (int i = 1; i < inputElements.size(); i++) {
      if (!inputElements.get(i).evaluate().equals(inputElements.get(0).evaluate())) {
        while (inputElements.size() != 0) {
          inputElements.remove(inputElements.size() - 1);
        }
        return null;
      }
    }
    return inputElements;
  }
}
