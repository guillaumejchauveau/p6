package com.p6.core.run;

import com.p6.core.solution.Solution;
import com.p6.core.solution.Symbol;
import com.p6.core.solution.rule.Condition;
import java.util.Collection;
import java.util.Random;

public class BasicP6Runner extends P6Runner {
  private Random random;

  public BasicP6Runner() {
    this.random = new Random();
  }

  @Override
  public void cycle(Solution solution, Integer count) {
    for (int cycle = 0; cycle < count; cycle++) {
      int xsymbolId = this.random.nextInt(solution.getSymbolCount());
      int ysymbolId = this.random.nextInt(solution.getSymbolCount());
      Symbol x = solution.getSymbol(xsymbolId);
      Symbol y = solution.getSymbol(ysymbolId);
      if (count - 10 < cycle) {
        System.out.println(x);
        System.out.println(y);
      }
      for (Condition condition : solution.getConditions()) {
        if (condition.test(x, y)) {
          Collection<Symbol> products = solution.getResult(condition).apply(x, y);
          solution.removeSymbol(xsymbolId);
          solution.removeSymbol(ysymbolId - ((xsymbolId < ysymbolId) ? 1 : 0));
          for (Symbol product : products) {
            solution.addSymbol(product);
          }
        }
      }
    }
  }
}
