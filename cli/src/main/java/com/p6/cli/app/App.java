package com.p6.cli.app;

import com.p6.core.reactor.BasicReactor;
import com.p6.core.reactor.Reactor;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.LibraryRegistry;
import com.p6.lib.SolutionBuilder;
import com.p6.parser.InvalidSyntaxException;
import com.p6.parser.ReactionPipelineParser;
import com.p6.parser.StructureParser;
import com.p6.utils.logging.LoggingHelper;
import java.util.HashMap;
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
  public static void main(String[] args)
      throws InvalidSyntaxException, ReflectiveOperationException {
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

    StringBuilder source = new StringBuilder();
    source.append("{ # Cell\n");
    source.append("  name: A\n");
    source.append("  rules: [ # Pipeline\n");
    source.append("    x,y : greater($x) : choose($x)\n");
    source.append("    x,y : hoho($y) : choose($y) : inject($Aa)\n");
    source.append("  ]\n");
    source.append("  elements: [ # Generator\n");
    source.append("    range(0, 10000, 1)\n");
    source.append("  ]\n");
    source.append("  subCells: [ # Cell\n");
    source.append("    {\n");
    source.append("      name: Aa\n");
    source.append("      rules: [\n");
    source.append("        a,b : oui : dissolve\n");
    source.append("\n");
    source.append("      ]\n");
    source.append("    }\n");
    source.append("  ]\n");
    source.append("}");

    StructureParser parser = new StructureParser(source.toString());
    System.out.println(parser.parseNextStructure());

    ReactionPipelineParser reactionPipelineParser = new ReactionPipelineParser(new HashMap<>());
    logger.debug(reactionPipelineParser.create("x,y : sort($x) : choose($x)", registry));
  }
}
