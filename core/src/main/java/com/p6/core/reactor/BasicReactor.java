package com.p6.core.reactor;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.solution.Element;
import com.p6.core.solution.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A single-threaded reactor.
 */
public class BasicReactor extends Reactor {
  /**
   * {@inheritDoc}
   *
   * @param solution The solution to use
   * @param count    The number of time the reactor should react
   */
  @Override
  public void iterate(Solution solution, Integer count) {
    Logger logger = LogManager.getLogger();

    for (int iteration = 0; iteration < count; iteration++) {
      if (solution.getElementsCount() <= 1) {
        logger.debug("Iteration overshoot: " + (count - iteration));
        break;
      }

      Element x = solution.chooseElement();
      Element y = solution.chooseElement();

      boolean reactionOccurred = false;
      for (ReactionCondition reactionCondition : solution.getConditions()) {
        if (reactionCondition.test(x, y)) {
          solution.applyRule(reactionCondition, x, y);
          reactionOccurred = true;
          break;
        }
      }

      if (!reactionOccurred) {
        solution.addElement(x);
        solution.addElement(y);
      }
    }
  }
}
