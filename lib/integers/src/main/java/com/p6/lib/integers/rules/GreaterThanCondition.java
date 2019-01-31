package com.p6.lib.integers.rules;

import com.p6.core.solution.Symbol;
import com.p6.core.solution.rule.Condition;
import com.p6.lib.integers.IntegerSymbol;

public class GreaterThanCondition extends Condition {
  public Boolean test(Symbol x, Symbol y) {
    if (x instanceof IntegerSymbol && y instanceof IntegerSymbol) {
      return ((IntegerSymbol) x).evaluate() > ((IntegerSymbol) y).evaluate();
    }

    return false;
  }
}
