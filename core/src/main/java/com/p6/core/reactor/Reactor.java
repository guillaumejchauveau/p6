package com.p6.core.reactor;

import com.p6.core.solution.Cell;

/**
 * Makes a cell's elements react according to the cell's pipelines.
 */
public abstract class Reactor {
  /**
   * Tries to make a reaction happen a given number of times.
   *
   * @param cell            The cell to use
   * @param iterationTarget The maximum number of time the reactor should react
   * @param stabilityTarget The maximum number of consecutive no-reaction
   */
  public abstract void iterate(Cell cell, Integer iterationTarget, Integer stabilityTarget);
}
