package com.p6.lib.integers;

import com.p6.core.solution.Solution;
import com.p6.core.solution.SolutionConfigurator;
import com.p6.core.solution.rule.result.ChooseXResult;
import com.p6.lib.integers.rules.GreaterThanCondition;

public class IntegerSolutionConfigurator extends SolutionConfigurator {
  @Override
  public void configure(Solution solution) {
    solution.createRule(new GreaterThanCondition(), new ChooseXResult());
    for (int i = 0; i < 10000; i++) {
      solution.addSymbol(new IntegerSymbol(i));
    }
  }
}
