package com.p6.lib.integers.rules;

import com.p6.core.solution.Symbol;
import com.p6.core.solution.rule.Condition;
import com.p6.lib.integers.IntegerSymbol;

public class GreaterThanCondition extends Condition {
  @Override
  public Boolean test(Symbol x, Symbol y) {
    if (x instanceof IntegerSymbol && y instanceof IntegerSymbol) {
      IntegerSymbol x1 = (IntegerSymbol) x;
      IntegerSymbol y1 = (IntegerSymbol) y;
      return x1.evaluate() > y1.evaluate();
    }
    return false;
  }
}
