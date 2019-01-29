package com.p6.lib.integers;

import com.p6.core.solution.Solution;
import com.p6.core.solution.SolutionConfigurator;
import org.junit.Test;

public class IntegerSolutionConfiguratorTest {
  @Test
  public void testConfiguration() {
    SolutionConfigurator configurator = new IntegerSolutionConfigurator();
    configurator.configure(new Solution(), new IntegerSolutionConfiguration());
  }
}
