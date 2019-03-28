package com.p6.core.reactor;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Makes a cell's elements react according to the cell's reaction pipelines.
 */
public class Reactor implements Runnable {
  private final Logger logger;
  private Cell cell;
  private Integer iterationTarget;
  private Integer stabilityTarget;

  /**
   * Initializes the reactor with execution settings.
   *
   * @param cell The cell to execute
   * @param iterationTarget The maximum number of reactions
   * @param stabilityTarget The maximum number of consecutive no-reactions
   */
  public Reactor(Cell cell, Integer iterationTarget, Integer stabilityTarget) {
    this.cell = cell;
    this.iterationTarget = iterationTarget;
    this.stabilityTarget = stabilityTarget;
    this.logger = LogManager.getLogger();
  }

  /**
   * Starts the execution.
   */
  @Override
  public void run() {
    this.logger.info("Starting reactor for cell " + this.cell);
    int stability = 0;
    int iteration = 0;

    for (; iteration < this.iterationTarget && !Thread.currentThread().isInterrupted();
         iteration++) {
      if (this.cell.getElementsCount() <= 1 || this.cell.isDissolved()) {
        this.logger.info("Solution cannot react further");
        break;
      }
      if (stability >= this.stabilityTarget) {
        this.logger.info("Solution reached target stability");
        break;
      }

      // Chooses the reactants.
      Element x = this.cell.chooseElement();
      Element y = this.cell.chooseElement();

      // Tries to execute a reaction pipeline. If one fails, continues to the next one.
      boolean reactionOccurred = false;
      for (ReactionPipeline pipeline : this.cell.getPipelines()) {
        List<Element> elements = new ArrayList<>();
        elements.add(x);
        elements.add(y);
        if (pipeline.handle(elements, this.cell)) {
          reactionOccurred = true;
          stability = 0;
          break;
        }
      }

      // If all the pipelines fail, there is a no-reaction, increasing the stability.
      if (!reactionOccurred) {
        this.cell.addElement(x);
        this.cell.addElement(y);
        stability++;
      }
    }
    if (iteration == this.iterationTarget) {
      this.logger.info("Iteration target reached");
    }
    if (Thread.currentThread().isInterrupted()) {
      this.logger.warn("Reactor interrupted");
    }
  }
}
