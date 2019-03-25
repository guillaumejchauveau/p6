package com.p6.lib.common;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.InitArgsParser;
import com.p6.lib.Library;
import com.p6.lib.common.reaction.ChooseReactant;
import com.p6.lib.common.reaction.ClearPipeline;
import com.p6.lib.common.reaction.DissolveCell;
import com.p6.lib.common.reaction.Equals;
import com.p6.lib.common.reaction.InjectInSubCell;
import com.p6.lib.common.reaction.NotEquals;
import java.util.HashMap;
import java.util.Map;

public class CommonLibrary extends Library {
  @Override
  public String getName() {
    return "Common";
  }

  @Override
  public Map<String, InitArgsParser<? extends ElementGenerator>> getElementGenerators() {
    Map<String, InitArgsParser<? extends ElementGenerator>> elementGenerators = new HashMap<>();
    return elementGenerators;
  }

  @Override
  public Map<String, InitArgsParser<? extends ReactionPipelineStep>> getReactionPipelineSteps() {
    Map<String, InitArgsParser<? extends ReactionPipelineStep>> reactionPipelineSteps =
        new HashMap<>();
    reactionPipelineSteps.put("choose", args -> {
      if (!(args.length == 1 && args[0] instanceof Element.Side)) {
        throw new IllegalArgumentException(
          "Choose reaction pipeline step requires an element reference");
      }
      return new ChooseReactant((Element.Side) args[0]);
    });
    reactionPipelineSteps.put("clear", args -> new ClearPipeline());
    reactionPipelineSteps.put("dissolve", args -> new DissolveCell());
    reactionPipelineSteps.put("equals", args -> new Equals());
    reactionPipelineSteps.put("inject", args -> {
      if (!(args.length == 1 && args[0] instanceof Cell)) {
        throw new IllegalArgumentException(
          "Inject reaction pipeline step requires a cell reference");
      }
      return new InjectInSubCell((Cell) args[0]);
    });
    reactionPipelineSteps.put("notEquals", args -> new NotEquals());
    return reactionPipelineSteps;
  }
}
