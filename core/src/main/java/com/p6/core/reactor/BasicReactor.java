package com.p6.core.reactor;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.solution.Element;
import com.p6.core.solution.Solution;

public class BasicReactor extends Reactor {
  @Override
  public void iterate(Solution solution, Integer count) {
    for (int iteration = 0; iteration < count; iteration++) {
      if (solution.getElementsCount() <= 1) {
        System.out.println("Iteration overshoot: " + (count - iteration));
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
