package com.p6.core.run;

import com.p6.core.solution.Solution;
import com.p6.core.solution.Symbol;
import com.p6.core.solution.rule.Condition;
import java.util.Collection;

public class BasicP6Runner extends P6Runner {
  @Override
  public void iterate(Solution solution, Integer count) {
    for (int iteration = 0; iteration < count; iteration++) {
      if (solution.getSymbolCount() <= 1) {
        System.out.println("Iteration overshoot: " + (count - iteration));
        break;
      }

      Symbol x = solution.chooseSymbol();
      Symbol y = solution.chooseSymbol();

      boolean reactionOccurred = false;
      for (Condition condition : solution.getConditions()) {
        if (condition.test(x, y)) {
          Collection<Symbol> products = solution.getResult(condition).apply(x, y);
          solution.addAllSymbols(products);
          reactionOccurred = true;
          break;
        }
      }

      if (!reactionOccurred) {
        solution.addSymbol(x);
        solution.addSymbol(y);
      }
    }
  }
}
