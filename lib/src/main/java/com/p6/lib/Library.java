package com.p6.lib;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import java.util.HashMap;
import java.util.Map;

/**
 * An object providing implementations of {@link ReactionPipelineStep}s and
 * {@link ElementGenerator}s.
 */
public abstract class Library {
  /**
   * The name of the library.
   *
   * @return The name of the library.
   */
  public abstract String getName();

  /**
   * The names of the provided {@link ElementGenerator}s mapped to their corresponding
   * {@link InitArgsParser}.
   * @return The names of the provided {@link ElementGenerator}s mapped to their corresponding
   *      {@link InitArgsParser}.
   */
  public Map<String, InitArgsParser<? extends ElementGenerator>> getElementGenerators() {
    return new HashMap<>();
  }

  /**
   * The names of the provided {@link ReactionPipelineStep}s mapped to their corresponding
   * {@link InitArgsParser}.
   *
   * @return The names of the provided {@link ReactionPipelineStep}s mapped to their corresponding
   *     {@link InitArgsParser}.
   */
  public Map<String, InitArgsParser<? extends ReactionPipelineStep>> getReactionPipelineSteps() {
    return new HashMap<>();
  }
}
