package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CreateElement implements ReactionPipelineStep {
  private Constructor<Element> elementConstructor;
  private Object[] args;

  public CreateElement(Class<Element> elementType, Class[] argTypes, Object[] args) throws NoSuchMethodException {
    this.elementConstructor = elementType.getConstructor(argTypes);
    this.args = args;
  }

  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    List<Element> outputElements = new ArrayList<>();
    try {
      outputElements.add(this.elementConstructor.newInstance(this.args));
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
    return outputElements;
  }
}
