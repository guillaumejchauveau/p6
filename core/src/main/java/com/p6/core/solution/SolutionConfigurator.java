package com.p6.core.solution;

/**
 * An object that adds elements and rules to a solution.
 */
public abstract class SolutionConfigurator {
  /**
   * Adds elements and rules to the solution.
   *
   * @param solution The solution to configure
   */
  public abstract void configure(Solution solution);
}
