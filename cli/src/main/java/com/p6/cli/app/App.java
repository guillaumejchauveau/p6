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
import com.p6.lib.LibraryRegistry;
import com.p6.lib.SolutionBuilder;
import com.p6.parser.Instruction;
import com.p6.parser.PipelineParser;

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

    LibraryRegistry registry = new LibraryRegistry();
    logger.debug(registry.getElementGeneratorNames());
    logger.debug(registry.getReactionPipelineStepNames());

    SolutionBuilder sb = new SolutionBuilder(registry);
    sb.createCell();
    sb.addElement("range", "0", "10000", "1")
      .createPipeline()
      .addStep("notEquals")
      .addStep("sort", Element.Side.LEFT)
      .addStep("choose", Element.Side.RIGHT)
      .sealCell();
    Cell cell = sb.getSolution();

    logger.debug(cell);
    Reactor reactor = new BasicReactor();
    reactor.iterate(cell, 10000, 5000);
    logger.debug(cell);

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
