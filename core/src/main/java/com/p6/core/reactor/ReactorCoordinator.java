package com.p6.core.reactor;

import com.p6.core.solution.Cell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ReactorCoordinator {
  private Map<Cell, Reactor> reactors;
  private Collection<Thread> threads;

  public ReactorCoordinator(Cell rootCell, Integer iterationTarget, Integer stabilityTarget) {
    this.reactors = new HashMap<>();
    this.threads = null;

    Stack<Cell> cellStack = new Stack<>();
    cellStack.push(rootCell);
    while (!cellStack.empty()) {
      Cell cell = cellStack.pop();
      for (Cell subCell : cell.getSubCells()) {
        cellStack.push(subCell);
      }
      this.reactors.put(cell, new Reactor(cell, iterationTarget, stabilityTarget));
    }
  }

  public void run() {
    this.threads = new ArrayList<>();
    for (Cell cell : this.reactors.keySet()) {
      Thread thread = new Thread(this.reactors.get(cell), cell.getName());
      thread.start();
      this.threads.add(thread);
    }
  }

  public void interrupt() {
    if (this.threads == null) {
      throw new IllegalStateException("Reactors are not running");
    }

    for (Thread thread : this.threads) {
      thread.interrupt();
    }
  }
}
