package com.p6.parser.instruction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.lib.LibraryRegistry;

/**
 * An {@link Instruction} representing a {@link ReactionPipelineStep}.
 */
public class ReactionPipelineStepInstruction extends Instruction<ReactionPipelineStep> {
  /**
   * Initializes a new instruction for a reaction pipeline step.
   *
   * @param name The name of the reaction pipeline step
   */
  public ReactionPipelineStepInstruction(String name) {
    super(name);
  }

  /**
   * Creates the reaction pipeline step corresponding to the instruction.
   *
   * @param registry The registry used to create the reaction pipeline step
   * @return The reaction pipeline step created
   */
  @Override
  public ReactionPipelineStep create(LibraryRegistry registry) {
    return registry.createReactionPipelineStep(this.name, this.arguments.toArray());
  }
}
