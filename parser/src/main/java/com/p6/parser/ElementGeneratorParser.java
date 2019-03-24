package com.p6.parser;

import com.p6.core.genesis.ElementGenerator;
import com.p6.lib.LibraryRegistry;
import com.p6.parser.instruction.ElementGeneratorInstruction;
import com.p6.parser.instruction.InstructionListParser;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ElementGeneratorParser extends InstructionListParser<ElementGeneratorInstruction> {
  /**
   *
   * @param parentReferences
   * @throws ReflectiveOperationException
   */
  public ElementGeneratorParser(Map<String, Object> parentReferences)
      throws ReflectiveOperationException {
    super(parentReferences, ElementGeneratorInstruction.class.getConstructor(String.class));
  }

  /**
   *
   * @param clause
   * @param registry
   * @return
   * @throws InvalidSyntaxException
   */
  public ElementGenerator create(String clause, LibraryRegistry registry)
      throws InvalidSyntaxException {
    List<ElementGeneratorInstruction> instructions = this.parse(clause);
    if (instructions.size() == 0) {
      throw new InvalidSyntaxException("Element generator instruction expected", clause, 0);
    }
    return instructions.get(0).create(registry);
  }
}
