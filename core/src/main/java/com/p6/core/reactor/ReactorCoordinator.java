package com.p6.core.reactor;

import com.p6.core.solution.Cell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * An object used to coordinate the creation and execution of reactors for a program's cells.
 */
public class ReactorCoordinator {
  public enum State {
    IDLE,
    PROCESSING,
    STOPPED,
    FAILED
  }

  private Map<Cell, Reactor> reactors;
  private Collection<Thread> threads;

  /**
   * Initializes the coordinator with the execution settings for the reactors.
   *
   * @param rootCell        The top cell in the cell tree
   * @param iterationTarget The maximum number of reactions (per-reactor)
   * @param stabilityTarget The maximum number of consecutive no-reactions (per-reactor)
   * @see Reactor
   */
  public ReactorCoordinator(Cell rootCell, Integer iterationTarget, Integer stabilityTarget) {
    this.reactors = new HashMap<>();
    this.threads = null;

    // Crawls the cell tree and a creates a reactor for each cell.
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

  /**
   * Checks the state of each reactor and returns a suitable value.
   * Does not differentiates reactors that are stable, stopped or interrupted.
   *
   * @return The state depending of the reactors' state
   */
  public State getState() {
    boolean idle = true;

    for (Reactor reactor : this.reactors.values()) {
      switch (reactor.getState()) {
        case IDLE:
          continue;
        case PROCESSING:
          return State.PROCESSING;
        case FAILED:
          return State.FAILED;
        default:
          idle = false;
      }
    }
    return idle ? State.IDLE : State.STOPPED;
  }

  /**
   * Starts the execution of the whole program. Starts the reactors in their own threads.
   */
  public void run() {
    this.threads = new ArrayList<>();
    for (Cell cell : this.reactors.keySet()) {
      Thread thread = new Thread(this.reactors.get(cell), cell.getName());
      thread.start();
      this.threads.add(thread);
    }
  }

  /**
   * Tries to interrupt the reactors' threads.
   */
  public void interrupt() {
    if (this.threads == null) {
      throw new IllegalStateException("Reactors are not running");
    }

    for (Thread thread : this.threads) {
      thread.interrupt();
    }
  }
}
