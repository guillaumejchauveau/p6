package com.p6.core.solution.rule;

import com.p6.core.solution.Symbol;
import java.util.Collection;

public abstract class Result {
  public abstract Collection<Symbol> apply(Symbol x, Symbol y);
}
