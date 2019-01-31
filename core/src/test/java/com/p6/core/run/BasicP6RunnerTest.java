package com.p6.core.run;

import com.p6.core.solution.Solution;
import org.junit.Test;

public class BasicP6RunnerTest {
  @Test
  public void testRun() {
    P6Runner runner = new BasicP6Runner();
    runner.cycle(new Solution(), 0);
  }
}
