package com.p6.cli.app;

import com.p6.core.reactor.BasicReactor;
import com.p6.core.reactor.Reactor;
import com.p6.core.solution.Cell;
import com.p6.lib.common.reaction.DissolveCellReactionProduct;
import com.p6.lib.common.reaction.ReactantsReactionProduct;
import com.p6.lib.integers.IntegerElement;
import com.p6.lib.integers.reaction.DivideReactionCondition;
import com.p6.lib.integers.reaction.GreaterThanReactionCondition;
import com.p6.utils.logging.LoggingHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point.
 */
public class App {
  /**
   * A test script.
   *
   * @param args System args
   */
  public static void main(String[] args) {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    Logger logger = LogManager.getLogger();

    Cell solution = new Cell();
    solution.createRule(new GreaterThanReactionCondition(), new ReactantsReactionProduct(true));
    for (int i = 0; i < 10000; i++) {
      solution.addElement(new IntegerElement(i));
    }
    Cell solution2 = new Cell();
    solution.addSubCell(solution2);
    solution2.createRule(new DivideReactionCondition(), new DissolveCellReactionProduct());
    solution2.createRule(new GreaterThanReactionCondition(), new ReactantsReactionProduct(true));
    for (int i = 10000; i < 20000; i++) {
      solution2.addElement(new IntegerElement(i));
    }

    Reactor reactor = new BasicReactor();
    logger.debug(solution2);
    reactor.iterate(solution2, 21000);
    logger.debug(solution2);
    logger.debug("i");
    logger.debug(solution);
    reactor.iterate(solution, 21000);
    logger.debug(solution);
  }
}
