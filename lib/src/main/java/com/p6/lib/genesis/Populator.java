package com.p6.lib.genesis;

import com.p6.core.solution.Cell;

/**
 *
 */
@FunctionalInterface
public interface Populator {
  /**
   * @param cell
   */
  void populate(Cell cell);
}
