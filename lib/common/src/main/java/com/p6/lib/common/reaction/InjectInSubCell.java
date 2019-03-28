package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * Injects the content of the pipeline in a pre-determined sub-cell. Then empties the pipeline
 * but does not stop it.
 */
public class InjectInSubCell implements ReactionPipelineStep {
  private final Cell subCell;

  /**
   * Creates an injection step.
   *
   * @param subCell The sub-cell to inject into
   */
  public InjectInSubCell(Cell subCell) {
    this.subCell = subCell;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    cell.inject(inputElements, this.subCell);
    return new ArrayList<>();
  }

  @Override
  public String toString() {
    return "InjectInSubCell";
  }
}
