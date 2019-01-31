package com.p6.lib.integers;

import com.p6.core.solution.Symbol;

public class IntegerSymbol extends Symbol {
  private Integer value;

  public IntegerSymbol() {
    this.value = null;
  }

  public IntegerSymbol(Integer value) {
    this.assign(value);
  }

  public String toString() {
    return this.evaluate().toString();
  }

  public void assign(Integer value) {
    this.value = value;
  }

  public Integer evaluate() {
    return this.value;
  }
}
