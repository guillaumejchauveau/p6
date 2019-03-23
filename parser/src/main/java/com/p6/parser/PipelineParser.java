package com.p6.parser;

import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipelineParser {

  enum State {
    LEFT_ELEMENT,
    RIGHT_ELEMENT,
    INSTRUCTION_NAME,
    INSTRUCTION_ARG,
    INSTRUCTION_POST_ARG
  }

  private Map<String, Object> references;

  public PipelineParser(Map<String, Object> parentReferences) {
    this.references = new HashMap<>(parentReferences);
  }

  public List<Instruction> parse(String clause) {
    List<Instruction> instructions = new ArrayList<>();
    String buffer = "";
    Instruction instruction = null;
    State state = State.LEFT_ELEMENT;

    for (int i = 0; i < clause.length(); i++) {
      char currentChar = clause.charAt(i);
      // End of line
      boolean eol = i == clause.length() - 1;

      if (",:()".contains(Character.toString(currentChar)) || eol) {
        buffer = buffer.trim();

        switch (state) {
          case LEFT_ELEMENT:
            this.references.put(buffer, Element.Side.LEFT);
            state = State.RIGHT_ELEMENT;
            break;

          case RIGHT_ELEMENT:
            this.references.put(buffer, Element.Side.RIGHT);
            state = State.INSTRUCTION_NAME;
            break;

          case INSTRUCTION_NAME:
            instruction = new Instruction(buffer);
            if (currentChar == '(') {
              state = State.INSTRUCTION_ARG;
            } else if (currentChar == ':' || eol) {
              instructions.add(instruction);
            }
            break;

          case INSTRUCTION_ARG:
            if (buffer.charAt(0) == '$') {
              instruction.addArgument(references.get(buffer.substring(1)));
            } else {
              instruction.addArgument(buffer);
            }
            if (currentChar == ')') {
              instructions.add(instruction);
              state = State.INSTRUCTION_POST_ARG;
            }
            break;

          case INSTRUCTION_POST_ARG:
            if (currentChar == ':') {
              state = State.INSTRUCTION_NAME;
            }
            break;

          default:
            throw new IllegalStateException("State is illegal");
        }
        buffer = "";
      } else {
        buffer += currentChar;
      }
    }
    return instructions;
  }
}
