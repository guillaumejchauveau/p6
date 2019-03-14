package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.List;

public class InjectInSubCell implements ReactionPipelineStep {
  private final Cell subCell;

  public InjectInSubCell(Cell subCell) {
    this.subCell = subCell;
  }

  public List<Element> handle(List<Element> inputElements, Cell cell) {
    cell.inject(inputElements, this.subCell);
    return inputElements;
  }
}
