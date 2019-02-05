package com.p6.core.reactor;

import com.p6.core.solution.Solution;

/**
 * Makes a solution's elements react according to the solution's rules.
 */
public abstract class Reactor {
  /**
   * Tries to make a reaction happen a given number of times.
   *
   * @param solution The solution to use
   * @param count    The number of time the reactor should react
   */
  public abstract void iterate(Solution solution, Integer count);
}
