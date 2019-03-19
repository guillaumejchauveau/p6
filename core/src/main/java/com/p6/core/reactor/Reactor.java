package com.p6.core.reactor;

import com.p6.core.solution.Cell;

/**
 * Makes a cell's elements react according to the cell's pipelines.
 */
public abstract class Reactor {
  /**
   * Tries to make a reaction happen a given number of times.
   *
   * @param cell  The solution to use
   * @param count The number of time the reactor should react
   */
  public abstract void iterate(Cell cell, Integer count);
}
