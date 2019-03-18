package com.p6.lib.integers.genesis;

import com.p6.core.solution.Cell;
import com.p6.lib.genesis.Populator;
import com.p6.lib.integers.IntegerElement;

/**
 *
 */
public class Range implements Populator {
  private Integer start;
  private Integer stop;
  private Integer step;

  /**
   * @param start
   * @param stop
   * @param step
   */
  public Range(Integer start, Integer stop, Integer step) {
    this.start = start;
    this.stop = stop;
    this.step = step;
  }

  @Override
  public void populate(Cell cell) {
    for (int i = this.start; i < this.stop; i += this.step) {
      cell.addElement(new IntegerElement(i));
    }
  }
}
