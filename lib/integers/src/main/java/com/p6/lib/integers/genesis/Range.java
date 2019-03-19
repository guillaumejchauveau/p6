package com.p6.lib.integers.genesis;

import com.p6.core.solution.Element;
import com.p6.core.genesis.ElementGenerator;
import com.p6.lib.integers.IntegerElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Range implements ElementGenerator {
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
  public List<Element> generate() {
    List<Element> outputElement = new ArrayList<>();
    for (int i = this.start; i < this.stop; i += this.step) {
      outputElement.add(new IntegerElement(i));
    }
    return outputElement;
  }
}
