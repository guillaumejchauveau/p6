package com.p6.lib.common;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.InitArgsParser;
import com.p6.lib.Library;
import com.p6.lib.common.reaction.ChooseElement;
import com.p6.lib.common.reaction.ClearPipeline;
import com.p6.lib.common.reaction.DissolveCell;
import com.p6.lib.common.reaction.EjectInParentCell;
import com.p6.lib.common.reaction.Equals;
import com.p6.lib.common.reaction.InjectInSubCell;
import com.p6.lib.common.reaction.NotEquals;
import java.util.Map;

/**
 * A {@link Library} providing reaction pipeline steps not dependent of the element's type.
 */
public class CommonLibrary extends Library {
  @Override
  public String getName() {
    return "Common";
  }

  @Override
  public Map<String, InitArgsParser<ReactionPipelineStep>> getReactionPipelineSteps() {
    var reactionPipelineSteps = super.getReactionPipelineSteps();

    reactionPipelineSteps.put("choose", args -> {
      if (!(args.length == 1 && args[0] instanceof Element.Side)) {
        throw new IllegalArgumentException(
          "Choose reaction pipeline step requires an element reference");
      }
      return new ChooseElement((Element.Side) args[0]);
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

    reactionPipelineSteps.put("eject", args -> new EjectInParentCell());
    reactionPipelineSteps.put("notEquals", args -> new NotEquals());
    return reactionPipelineSteps;
  }
}
