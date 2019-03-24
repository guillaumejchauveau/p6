package com.p6.cli.app;

import com.p6.lib.LibraryRegistry;
import com.p6.parser.CellParser;
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

    StringBuilder source = new StringBuilder();
    source.append("    {\n");
    source.append("      name: Aa\n");
    source.append("      rules: [\n");
    source.append("        a,b : sort($a) : choose($b) : inject($tro bien)\n");
    source.append("      ]\n");
    source.append("      elements: [\n");
    source.append("       range(10,20,1)\n");
    source.append("      ]\n");
    source.append("      subCells: [\n");
    source.append("        {\n");
    source.append("          name: tro bien\n");
    source.append("          rules: [\n");
    source.append("             gosh,drouate : choose($gosh)\n");
    source.append("          ]\n");
    source.append("          elements : [\n");
    source.append("             range(0, 10, 1)\n");
    source.append("          ]\n");
    source.append("         }\n");
    source.append("       ]\n");
    source.append("    }\n");

    LibraryRegistry registry = new LibraryRegistry();
    CellParser cellParser = new CellParser(source.toString());
    logger.debug(cellParser.create(registry));
  }
}
