package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.utils.logging.plugins.SleepFilter;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a reaction rule with a set of steps. A pipeline can fail, in which case the reactor
 * processing the cell should try to execute the next one.
 */
public class ReactionPipeline {
  private List<ReactionPipelineStep> steps;
  private Logger logger;

  /**
   * Creates a pipeline.
   */
  public ReactionPipeline() {
    this.steps = new ArrayList<>();
    this.logger = LogManager.getLogger();
  }

  public String toString() {
    return this.steps.toString();
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
    var outputElements = inputElements;
    for (var step : this.steps) {
      outputElements = step.handle(outputElements, cell);
      if (cell.isDissolved()) {
        this.logger.debug("Cell dissolved with steps " + this);
        return true;
      }
      if (outputElements == null) {
        return false;
      }
    }
    cell.addAllElements(outputElements);
    this.logger.debug(SleepFilter.MARKER, String.format("Elements %s replaced by %s with steps %s",
        inputElements, outputElements, this));
    return this.steps.size() != 0;
  }
}
