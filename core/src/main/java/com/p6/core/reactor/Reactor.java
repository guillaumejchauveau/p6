package com.p6.core.reactor;

import com.p6.core.solution.Solution;

public abstract class Reactor {
  public abstract void iterate(Solution solution, Integer count);
}
