package com.p6.parser.instruction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.lib.LibraryRegistry;

public class ReactionPipelineStepInstruction extends Instruction<ReactionPipelineStep> {
  public ReactionPipelineStepInstruction(String name) {
    super(name);
  }

  @Override
  public ReactionPipelineStep create(LibraryRegistry registry) {
    return registry.createReactionPipelineStep(this.name, this.arguments.toArray());
  }
}
