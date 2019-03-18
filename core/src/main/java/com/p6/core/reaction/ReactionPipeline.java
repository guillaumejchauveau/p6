package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class ReactionPipeline {
  private List<ReactionPipelineStep> steps;

  /**
   *
   */
  public ReactionPipeline() {
    this.steps = new ArrayList<>();
  }

  /**
   * @param step
   */
  public void addStep(ReactionPipelineStep step) {
    this.steps.add(step);
  }

  /**
   * @param inputElements
   * @param cell
   * @return
   */
  public Boolean handle(List<? extends Element> inputElements, Cell cell) {
    List<? extends Element> outputElements = inputElements;
    for (ReactionPipelineStep step : this.steps) {
      outputElements = step.handle(Collections.unmodifiableList(inputElements), cell);
      if (cell.isDissolved()) {
        return true;
      }
      if (outputElements == null) {
        return false;
      }
    }
    cell.addAllElements(outputElements);
    return this.steps.size() != 0;
  }
}
