package com.p6.cli.app;

import com.p6.core.reactor.BasicReactor;
import com.p6.core.reactor.Reactor;
import com.p6.core.solution.Cell;
import com.p6.lib.common.reaction.ReactantsReactionProduct;
import com.p6.lib.integers.IntegerElement;
import com.p6.lib.integers.reaction.GreaterThanReactionCondition;
import com.p6.utils.LoggingHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point.
 */
public class App {
  /**
   * A test script.
   * @param args System args
   */
  public static void main(String[] args) {
    Cell solution = new Cell();
    solution.createRule(new GreaterThanReactionCondition(), new ReactantsReactionProduct(true));
    for (int i = 0; i < 10000; i++) {
      solution.addElement(new IntegerElement(i));
    }
    LoggingHelper.configureLoggingFramework(Level.ALL);
    Logger logger = LogManager.getLogger();

    logger.debug(solution);
    Reactor reactor = new BasicReactor();
    reactor.iterate(solution, 21000);
    logger.debug(solution);
  }
}
