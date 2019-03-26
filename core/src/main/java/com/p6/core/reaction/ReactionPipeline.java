package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a reaction rule with a set of steps. A pipeline can fail, in which case the reactor
 * processing the cell should try to execute the next one.
 */
public class ReactionPipeline {
  private List<ReactionPipelineStep> steps;

  /**
   * Creates a pipeline.
   */
  public ReactionPipeline() {
    this.steps = new ArrayList<>();
  }

  /**
   * Adds a step to the pipeline.
   *
   * @param step The step to add
   */
  public void addStep(ReactionPipelineStep step) {
    this.steps.add(step);
  }

  /**
   * Attempts to handle elements with the configured steps.
   *
   * @param inputElements The elements to handle
   * @param cell          The cell in which the reaction occurs
   * @return The status of the pipeline: true if it succeeded, false if it failed.
   */
  public Boolean handle(List<Element> inputElements, Cell cell) {
    List<Element> outputElements = inputElements;
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
