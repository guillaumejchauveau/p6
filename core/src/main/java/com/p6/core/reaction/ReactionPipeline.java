package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.utils.List;
import java.utils.ArrayList;

public class ReactionPipeline {
  private List<ReactionPipelineStep> steps;

  public ReactionPipeline() {
    this.steps = new ArrayList<>();
  }

  public void addStep(ReactionPipelineStep step) {
    this.steps.add(step);
  }

  public Boolean handle(List<Element> elements, Cell cell) {
    for (ReactionPipelineStep step : this.steps) {
      step.handle(elements, cell);
      if (elements.size() == 0 || cell.isDisolved()) {
        return false;
      }
    }
    cell.addAllElements(elements);
    return true;
  }
}
