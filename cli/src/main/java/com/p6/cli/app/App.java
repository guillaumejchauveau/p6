package com.p6.cli.app;

import com.p6.parser.Instruction;
import com.p6.utils.LoggingHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;

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
    Logger logger = LogManager.getLogger();

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

