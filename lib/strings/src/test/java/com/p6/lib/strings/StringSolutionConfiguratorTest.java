package com.p6.lib.strings;

import com.p6.core.solution.Solution;
import com.p6.core.solution.SolutionConfigurator;
import org.junit.Test;

public class StringSolutionConfiguratorTest {
  @Test
  public void testConfiguration() {
    SolutionConfigurator configurator = new StringSolutionConfigurator();
    configurator.configure(new Solution());
  }
}
