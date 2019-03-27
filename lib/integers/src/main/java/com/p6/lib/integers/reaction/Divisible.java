package com.p6.lib.integers.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.integers.solution.IntegerElement;
import java.util.ArrayList;
import java.util.List;

public class Divisible extends IntegerReactionPipelineStep {
  private Element.Side elementSide;

  public Divisible(Element.Side elementSide) {
    this.elementSide = elementSide;
  }

  @Override
  protected List<Element> integerTest(List<IntegerElement> elements, Cell cell) {
    List<Element> output = new ArrayList<>();
    if (elements.get(0).evaluate() % elements.get(1).evaluate() == 0) {
      output.add(elements.get(0));
      output.add(elements.get(1));
    } else if (elements.get(1).evaluate() % elements.get(0).evaluate() == 0) {
      output.add(elements.get(1));
      output.add(elements.get(0));
    } else {
      return null;
    }
    if (this.elementSide == Element.Side.RIGHT) {
      Element tmp = output.get(0);
      output.remove(0);
      output.add(tmp);
    }
    return output;
  }

  @Override
  public String toString() {
    return "Divisible";
  }
}
