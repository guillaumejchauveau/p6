package com.p6.parser;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.solution.Element;
import com.p6.lib.LibraryRegistry;
import com.p6.parser.instruction.InstructionListParser;
import com.p6.parser.instruction.ReactionPipelineStepInstruction;
import java.util.List;
import java.util.Map;

/**
 * Parses a list of instructions to create an entire {@link ReactionPipeline}.
 * <br>
 * The string representing the pipeline starts with names for referencing the left and right
 * element of the pipeline input. Therefore, the method
 * {@link InstructionListParser#parse(String)} has been extended to handle this part of the string.
 */
public class ReactionPipelineParser extends InstructionListParser<ReactionPipelineStepInstruction> {
  /**
   * Initializes the instruction list parser for {@link ReactionPipelineStepInstruction}s.
   *
   * @param parentReferences Inherited references
   * @throws ReflectiveOperationException Thrown most likely in the case of a programmatic error
   */
  public ReactionPipelineParser(Map<String, Object> parentReferences)
      throws ReflectiveOperationException {
    super(parentReferences, ReactionPipelineStepInstruction.class.getConstructor(String.class));
  }

  /**
   * Parses the string representing the reaction pipeline.
   * <br>
   * Splits the string at the first ':' character to separate the {@link Element.Side} references
   * add the actual instruction list for the pipeline's steps.
   * <br>
   * {@inheritDoc}
   */
  @Override
  public List<ReactionPipelineStepInstruction> parse(String clause) throws InvalidSyntaxException {
    var referencesIndex = clause.indexOf(':');
    try {
      var elementReferenceNames = clause.substring(0, referencesIndex).split(",");
      this.references.put(elementReferenceNames[0].trim(), Element.Side.LEFT);
      this.references.put(elementReferenceNames[1].trim(), Element.Side.RIGHT);
      return super.parse(clause.substring(referencesIndex + 1));
    } catch (IndexOutOfBoundsException e) {
      throw new InvalidSyntaxException("Left and right element reference name expected", clause,
        referencesIndex);
    }
  }

  /**
   * Creates the reaction pipeline defined by the input string using a {@link LibraryRegistry}.
   *
   * @param clause   The string to parse
   * @param registry The registry to use
   * @return The created reaction pipeline
   * @throws InvalidSyntaxException Thrown of the string cannot be parsed
   */
  public ReactionPipeline create(String clause, LibraryRegistry registry)
      throws InvalidSyntaxException {
    var instructions = this.parse(clause);
    if (instructions.size() == 0) {
      throw new InvalidSyntaxException("Reaction pipeline step instruction expected", clause, 0);
    }
    var pipeline = new ReactionPipeline();
    for (var stepInstruction : instructions) {
      pipeline.addStep(stepInstruction.create(registry));
    }
    return pipeline;
  }
}
