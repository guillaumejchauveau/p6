package com.p6.cli.app;

import com.p6.core.reactor.BasicReactor;
import com.p6.core.reactor.Reactor;
import com.p6.core.solution.Solution;
import com.p6.lib.common.reaction.ChooseXReactionProduct;
import com.p6.lib.integers.IntegerElement;
import com.p6.lib.integers.reaction.GreaterThanReactionCondition;

/**
 * Entry point.
 */
public class App {
  /**
   * Displays the app's message.
   *
   * @param args System args
   */
  public static void main(String[] args) {
    Solution solution = new Solution();
    solution.createRule(new GreaterThanReactionCondition(), new ChooseXReactionProduct());
    for (int i = 0; i < 10000; i++) {
      solution.addElement(new IntegerElement(i));
    }
    System.out.println(solution);
    Reactor reactor = new BasicReactor();
    reactor.iterate(solution, 21000);
    System.out.println(solution);
  }
}
