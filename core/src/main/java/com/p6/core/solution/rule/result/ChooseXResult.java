package com.p6.core.solution.rule.result;

import com.p6.core.solution.Symbol;
import com.p6.core.solution.rule.Result;
import java.util.ArrayList;
import java.util.Collection;

public class ChooseXResult extends Result {
  @Override
  public Collection<Symbol> apply(Symbol x, Symbol y) {
    Collection<Symbol> result = new ArrayList<>();
    result.add(x);
    return result;
  }
}
