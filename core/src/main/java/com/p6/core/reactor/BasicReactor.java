package com.p6.core.reactor;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A single-threaded reactor.
 */
public class BasicReactor extends Reactor {
  /**
   * {@inheritDoc}
   * @param cell  The cell to use
   * @param count The number of time the reactor should react
   */
  @Override
  public void iterate(Cell cell, Integer count) {
    Logger logger = LogManager.getLogger();

    for (int iteration = 0; iteration < count; iteration++) {
      if (cell.getElementsCount() <= 1) {
        logger.debug("Iteration overshoot: " + (count - iteration));
        break;
      }

      Element x = cell.chooseElement();
      Element y = cell.chooseElement();

      boolean reactionOccurred = false;
      for (ReactionCondition reactionCondition : cell.getConditions()) {
        if (reactionCondition.test(x, y)) {
          cell.applyRule(reactionCondition, x, y);
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
