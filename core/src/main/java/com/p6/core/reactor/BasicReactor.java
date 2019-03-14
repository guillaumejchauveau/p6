package com.p6.core.reactor;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A single-threaded reactor.
 */
public class BasicReactor extends Reactor {
  /**
   * {@inheritDoc}
   *
   * @param cell  The cell to use
   * @param count The number of time the reactor should react
   */
  @Override
  public void iterate(Cell cell, Integer count) {
    Logger logger = LogManager.getLogger();

    for (int iteration = 0; iteration < count; iteration++) {
      if (cell.getElementsCount() <= 1 || cell.isDissolved()) {
        logger.debug("Iteration overshoot: " + (count - iteration));
        break;
      }

      Element x = cell.chooseElement();
      Element y = cell.chooseElement();

      boolean reactionOccurred = false;
      for (ReactionPipeline pipeline : cell.getPipelines()) {
        List<Element> elements = new ArrayList<>();
        elements.add(x);
        elements.add(y);
        if (pipeline.handle(elements, cell)) {
          reactionOccurred = true;
          break;
        }
      }

      if (!reactionOccurred) {
        cell.addElement(x);
        cell.addElement(y);
      }
    }
  }
}
