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

  public Reactor(Cell cell, Integer iterationTarget, Integer stabilityTarget) {
    this.cell = cell;
    this.iterationTarget = iterationTarget;
    this.stabilityTarget = stabilityTarget;
    this.logger = LogManager.getLogger();
  }

  @Override
  public void run() {
    this.logger.info("Starting reactor for cell " + this.cell);
    int stability = 0;
    int iteration = 0;

    for (; iteration < this.iterationTarget && !Thread.currentThread().isInterrupted(); iteration++) {
      if (this.cell.getElementsCount() <= 1 || this.cell.isDissolved()) {
        this.logger.info("Solution cannot react further");
        break;
      }
      if (stability >= this.stabilityTarget) {
        this.logger.info("Solution reached target stability");
        break;
      }

      Element x = this.cell.chooseElement();
      Element y = this.cell.chooseElement();

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
