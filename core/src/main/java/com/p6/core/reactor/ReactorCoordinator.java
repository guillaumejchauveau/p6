package com.p6.core.reactor;

import com.p6.core.solution.Cell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReactorCoordinator {
  private Collection<Reactor> reactors;
  private Logger logger;

  public ReactorCoordinator(Cell rootCell, Integer iterationTarget, Integer stabilityTarget) {
    this.reactors = new ArrayList<>();
    this.logger = LogManager.getLogger();

    Stack<Cell> cellStack = new Stack<>();
    cellStack.push(rootCell);
    while (!cellStack.empty()) {
      Cell cell = cellStack.pop();
      for (Cell subCell : cell.getSubCells()) {
        cellStack.push(subCell);
      }
      this.reactors.add(new Reactor(cell, iterationTarget, stabilityTarget));
    }
  }

  public void run() {
    for (Reactor reactor : this.reactors) {
      Thread thread = new Thread(reactor);
      thread.start();
    }
  }
}
