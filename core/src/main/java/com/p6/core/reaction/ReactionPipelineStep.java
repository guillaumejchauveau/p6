package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;

@FunctionalInterface
public interface ReactionPipelineStep {
  void handle(List<Element> elements, Cell cell);
}
