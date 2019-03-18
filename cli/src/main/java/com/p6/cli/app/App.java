package com.p6.cli.app;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.reactor.BasicReactor;
import com.p6.core.reactor.Reactor;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.common.reaction.ChooseReactant;
import com.p6.lib.common.reaction.Sort;
import com.p6.lib.integers.IntegerElement;
import com.p6.lib.integers.genesis.Range;
import com.p6.utils.logging.LoggingHelper;
import java.util.List;
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
    ReactionPipeline pipeline = new ReactionPipeline();
    pipeline.addStep(new Sort(Element.Side.LEFT));
    pipeline.addStep(new ChooseReactant(Element.Side.LEFT));
    solution.addPipeline(pipeline);
    new Range(0, 10000, 1).populate(solution);

    Cell solution2 = new Cell();
    ReactionPipeline pipeline2 = new ReactionPipeline();
    pipeline2.addStep((List<Element> inputElements, Cell cell) -> {
      Element el = inputElements.get(0);
      if (el instanceof IntegerElement && ((IntegerElement) el).evaluate() <= 10002) {
        cell.dissolve();
      }
      return inputElements.subList(0, 1);
    });
    solution2.addPipeline(pipeline2);
    logger.debug("----------------");
    new Range(10000, 20000, 1).populate(solution2);

    solution.addSubCell(solution2);

    Reactor reactor = new BasicReactor();
    logger.debug(solution2);
    reactor.iterate(solution2, 5000);
    logger.debug(solution2);
    logger.debug(solution);
    reactor.iterate(solution, 20000);
    logger.debug(solution);
  }
}
