package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
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

  private String printSteps() {
    StringBuilder stringBuilder = new StringBuilder("[");
    for (ReactionPipelineStep reactionPipelineStep : this.steps) {
      stringBuilder.append(reactionPipelineStep.getClass().getName() + ", ");
    }
    stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length() - 1, "]");
    return stringBuilder.toString();
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
      outputElements = step.handle(outputElements, cell);
      if (cell.isDissolved()) {
        this.logger.debug("Cell dissolved with steps " + this.printSteps());
        return true;
      }
      if (outputElements == null) {
        return false;
      }
    }
    cell.addAllElements(outputElements);
    this.logger.debug("Elements " + inputElements + " replaced by " + outputElements + " with steps " + this.printSteps());
    return this.steps.size() != 0;
  }
}
