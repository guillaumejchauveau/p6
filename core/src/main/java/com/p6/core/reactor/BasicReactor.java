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
   * @parqehpiham solution The solution to use
   * @param maxCount    The number of time the reactor should react
   */
  @Override
  public void iterate(Solution solution, Integer maxCount, Integer noReactionMaxUser) {
    Logger logger = LogManager.getLogger();

    boolean noReaction = false;
    int noReactionMax = 0;

    for (int iteration = 0; iteration < maxCount; iteration++) {
      if (solution.getElementsCount() <= 1) {
        logger.debug("Iteration overshoot: " + (maxCount - iteration));
        break;
      }

      if (noReactionMax>=noReactionMaxUser){
        break;
      }

      Element x = solution.chooseElement();
      Element y = solution.chooseElement();

      boolean reactionOccurred = false;
      for (ReactionCondition reactionCondition : solution.getConditions()) {
        if (reactionCondition.test(x, y)) {
          solution.applyRule(reactionCondition, x, y);
          reactionOccurred = true;
          noReactionMax = 0;
          break;
        }
      }

      if (noReaction){
        noReactionMax++;
      }

      if (!reactionOccurred){
        solution.addElement(x);
        solution.addElement(y);
        noReaction = true;
      } else {
        noReaction = false;
      }
    }
  }
}
