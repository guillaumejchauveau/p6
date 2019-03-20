package com.p6.lib;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import java.util.Map;

public abstract class Library {
  public abstract String getName();

  public abstract Map<String, InitArgParser<? extends ElementGenerator>> getElementGenerators();

  public abstract Map<String, InitArgParser<? extends ReactionPipelineStep>> getReactionPipelineSteps();
}
