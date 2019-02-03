package com.p6.core.reactor;

import com.p6.core.solution.Solution;
import org.junit.Test;

public class BasicReactorTest {
  @Test
  public void testRun() {
    Reactor reactor = new BasicReactor();
    reactor.iterate(new Solution(), 0);
  }
}
