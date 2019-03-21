package com.p6.lib.integers;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.lib.InitArgsParser;
import com.p6.lib.Library;
import com.p6.lib.integers.genesis.Range;

import java.util.HashMap;
import java.util.Map;

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
    return reactionPipelineSteps;
  }
}
