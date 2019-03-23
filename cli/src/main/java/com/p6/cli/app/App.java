package com.p6.cli.app;

import com.p6.core.reactor.BasicReactor;
import com.p6.core.reactor.Reactor;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.LibraryRegistry;
import com.p6.lib.SolutionBuilder;
import com.p6.parser.InvalidSyntaxException;
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
  public static void main(String[] args) throws InvalidSyntaxException {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    final Logger logger = LogManager.getLogger();

    LibraryRegistry registry = new LibraryRegistry();

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

    /*
    StructureParser parser = new StructureParser();
    System.out.println(parser.parseNextStructure());

    PipelineParser pipelineParser = new PipelineParser(new HashMap<>());
    String clause = " xxx , yyy :    greater: grea ( lo l , $yyy) : lol ";
    for (Instruction instruction : pipelineParser.parse(clause)) {
      logger.debug(instruction.name);
      for (Object arg : instruction.getArguments()) {
        logger.debug(arg);
      }
      logger.debug("----");
    }*/
  }
}
