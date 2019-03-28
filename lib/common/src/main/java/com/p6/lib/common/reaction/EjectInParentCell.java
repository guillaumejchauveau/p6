package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * Ejects the pipeline's content in the cell's parent cell. Then empties the pipeline
 * but does not stop it.
 */
public class EjectInParentCell implements ReactionPipelineStep {
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    cell.eject(inputElements);
    return new ArrayList<>();
  }

  @Override
  public String toString() {
    return "EjectInParentCell";
  }
}
