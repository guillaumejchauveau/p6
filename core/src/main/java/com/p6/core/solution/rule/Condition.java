package com.p6.core.solution.rule;

import com.p6.core.solution.Symbol;

public abstract class Condition {
  public abstract Boolean test(Symbol x, Symbol y);
}
