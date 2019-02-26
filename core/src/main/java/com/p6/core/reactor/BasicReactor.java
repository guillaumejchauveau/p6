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
   */
  @Override
  public void iterate(Solution solution, Integer iterationTarget, Integer stabilityTarget) {
    Logger logger = LogManager.getLogger();
    int stability = 0;

    for (int iteration = 0; iteration < iterationTarget; iteration++) {
      if (solution.getElementsCount() <= 1) {
        logger.debug("Solution cannot react further");
        break;
      }
      if (stability >= stabilityTarget) {
        logger.debug("Solution reached target stability");
        break;
      }

      Element x = solution.chooseElement();
      Element y = solution.chooseElement();

      boolean reactionOccurred = false;
      for (ReactionCondition reactionCondition : solution.getConditions()) {
        if (reactionCondition.test(x, y)) {
          solution.applyRule(reactionCondition, x, y);
          reactionOccurred = true;
          stability = 0;
          break;
        }
      }

      if (!reactionOccurred) {
        solution.addElement(x);
        solution.addElement(y);
        stability++;
      }
    }
  }
}
