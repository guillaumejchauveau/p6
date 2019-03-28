package com.p6.lib.integers;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Element;
import com.p6.lib.InitArgsParser;
import com.p6.lib.Library;
import com.p6.lib.integers.genesis.Range;
import com.p6.lib.integers.reaction.Divisible;
import com.p6.lib.integers.reaction.SortIntegers;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Library} providing reaction pipeline steps and element generators for integers.
 */
public class IntegerLibrary extends Library {
  @Override
  public String getName() {
    return "Integer";
  }

  @Override
  public Map<String, InitArgsParser<? extends ElementGenerator>> getElementGenerators() {
    Map<String, InitArgsParser<? extends ElementGenerator>> elementGenerators = new HashMap<>();
    elementGenerators.put("range", args -> {
      try {
        if (!(
            args.length == 3
            && args[0] instanceof String && args[1] instanceof String && args[2] instanceof String
          )) {
          throw new NumberFormatException();
        }
        return new Range(
          Integer.parseInt((String) args[0]),
          Integer.parseInt((String) args[1]),
          Integer.parseInt((String) args[2]));
      } catch (IndexOutOfBoundsException | NumberFormatException e) {
        throw new IllegalArgumentException("Range element generator requires 3 integers arguments");
      }
    });
    return elementGenerators;
  }

  @Override
  public Map<String, InitArgsParser<? extends ReactionPipelineStep>> getReactionPipelineSteps() {
    Map<String, InitArgsParser<? extends ReactionPipelineStep>> reactionPipelineSteps =
        new HashMap<>();
    reactionPipelineSteps.put("sortInt", args -> {
      if (!(args.length == 1 && args[0] instanceof Element.Side)) {
        throw new IllegalArgumentException(
          "SortInt reaction pipeline step requires an element reference");
      }
      return new SortIntegers((Element.Side) args[0]);
    });

    reactionPipelineSteps.put("divisible", args -> {
      if (!(args.length == 1 && args[0] instanceof Element.Side)) {
        throw new IllegalArgumentException(
          "Divisible reaction pipeline step requires an element reference");
      }
      return new Divisible((Element.Side) args[0]);
    });
    return reactionPipelineSteps;
  }
}
