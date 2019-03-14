package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Element;
import com.p6.core.solution.Cell;
import java.utils.List;

public class EqualsReactionCondition implements ReactionPipelineStep {
  public void handle(List<Element> elements, Cell cell) {
    for (int i = 1; i < elements.size(); i++) {
      if (!elements.get(i).evaluate().equals(elements.get(0).evaluate())) {
        while (elements.size() != 0) {
          elements.remove(elements.size() - 1);
        }
      }
    }
  }
}
