package com.p6.parser;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.solution.Element;
import com.p6.lib.LibraryRegistry;
import com.p6.parser.instruction.InstructionListParser;
import com.p6.parser.instruction.ReactionPipelineStepInstruction;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ReactionPipelineParser extends InstructionListParser<ReactionPipelineStepInstruction> {
  /**
   *
   * @param parentReferences
   * @throws ReflectiveOperationException
   */
  public ReactionPipelineParser(Map<String, Object> parentReferences)
      throws ReflectiveOperationException {
    super(parentReferences, ReactionPipelineStepInstruction.class.getConstructor(String.class));
  }

  /**
   *
   * @param clause
   * @return
   * @throws InvalidSyntaxException
   */
  @Override
  public List<ReactionPipelineStepInstruction> parse(String clause) throws InvalidSyntaxException {
    int referencesIndex = clause.indexOf(':');
    try {
      String[] elementReferenceNames = clause.substring(0, referencesIndex).split(",");
      this.references.put(elementReferenceNames[0].trim(), Element.Side.LEFT);
      this.references.put(elementReferenceNames[1].trim(), Element.Side.RIGHT);
      return super.parse(clause.substring(referencesIndex + 1));
    } catch (IndexOutOfBoundsException e) {
      throw new InvalidSyntaxException("Left and right element reference name expected", clause,
        referencesIndex);
    }
  }

  /**
   *
   * @param clause
   * @param registry
   * @return
   * @throws InvalidSyntaxException
   */
  public ReactionPipeline create(String clause, LibraryRegistry registry)
      throws InvalidSyntaxException {
    List<ReactionPipelineStepInstruction> instructions = this.parse(clause);
    if (instructions.size() == 0) {
      throw new InvalidSyntaxException("Reaction pipeline step instruction expected", clause, 0);
    }
    ReactionPipeline pipeline = new ReactionPipeline();
    for (ReactionPipelineStepInstruction stepInstruction : instructions) {
      pipeline.addStep(stepInstruction.create(registry));
    }
    return pipeline;
  }
}
