package com.p6.lib.integers;

import com.p6.core.solution.Solution;
import com.p6.core.solution.SolutionConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@inheritDoc}
 */
public class IntegerSolutionConfigurator extends SolutionConfigurator {
  /**
   * {@inheritDoc}
   */
  @Override
  public void configure(Solution solution) {
    Logger logger = LogManager.getLogger();
    logger.debug("Integer configurator");
  }
}
