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
import com.p6.parser.Instruction;
import com.p6.parser.PipelineParser;
import com.p6.utils.logging.LoggingHelper;

import java.util.HashMap;
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
    final Logger logger = LogManager.getLogger();

    Cell cell1 = new Cell();
    ReactionPipeline pipeline1 = new ReactionPipeline();
    pipeline1.addStep(new Sort(Element.Side.LEFT));
    pipeline1.addStep(new ChooseReactant(Element.Side.LEFT));
    cell1.addPipeline(pipeline1);
    cell1.addAllElements(new Range(0, 10000, 1));

    Cell cell2 = new Cell();
    ReactionPipeline pipeline2 = new ReactionPipeline();
    pipeline2.addStep((List<Element> inputElements, Cell cell) -> {
      Element el = inputElements.get(0);
      if (el instanceof IntegerElement && ((IntegerElement) el).evaluate() <= 10002) {
        cell.dissolve();
      }
      return inputElements.subList(0, 1);
    });
    cell2.addPipeline(pipeline2);
    logger.debug("----------------");
    cell2.addAllElements(new Range(10000, 20000, 1));

    cell1.addSubCell(cell2);

    Reactor reactor = new BasicReactor();
    logger.debug(cell2);
    reactor.iterate(cell2, 5000, 5000);
    logger.debug(cell2);
    logger.debug(cell1);
    reactor.iterate(cell1, 20000, 20000);
    logger.debug(cell1);

    PipelineParser pipelineParser = new PipelineParser(new HashMap<>());
    for (Instruction instruction : pipelineParser.parse(" xxx , yyy :    greater: grea ( lo l , $yyy) : lol ;")) {
      //for (Instruction instruction : pipelineParser.parse(" x,y:greater : superieur;")) {
      logger.debug(instruction.name);
      for (Object arg : instruction.getArguments()) {
        logger.debug(arg);
      }
      logger.debug("----");
    }
  }
}
