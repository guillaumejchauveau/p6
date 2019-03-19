package com.p6.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    Logger logger = LogManager.getLogger();
    List<Instruction> instructions = new ArrayList<>();
    String buffer = "";
    Instruction instruction = null;
    State state = State.LEFT_ELEMENT;
    for (int i = 0; i < clause.length(); i++) {
      char separator = clause.charAt(i);
      if (!",:();".contains(Character.toString(separator))) {
        buffer = buffer + separator;
      } else {
        buffer = buffer.trim();
        logger.debug(buffer);
        logger.debug(separator);
        logger.debug(state);

        switch (state) {
          case LEFT_ELEMENT:
            this.references.put(buffer, true);
            state = State.RIGHT_ELEMENT;
            break;

          case RIGHT_ELEMENT:
            this.references.put(buffer, false);
            state = State.INSTRUCTION_NAME;
            break;

          case INSTRUCTION_NAME:
            instruction = new Instruction(buffer);
            if (separator == '(') {
              state = State.INSTRUCTION_ARG;
            } else if (separator == ':') {
              instructions.add(instruction);
            } else if (separator == ';') {
              instructions.add(instruction);
              return instructions;
            }
            break;

          case INSTRUCTION_ARG:
            if (buffer.charAt(0) == '$') {
              instruction.addArgument(references.get(buffer.substring(1)));
            } else {
              instruction.addArgument(buffer);
            }
            if (separator == ')') {
              instructions.add(instruction);
              state = State.INSTRUCTION_POST_ARG;
            } else if (separator != ',') {
              // Erreur de syntaxe.
            }
            break;

          case INSTRUCTION_POST_ARG:
            if (separator == ';') {
              return instructions;
            } else if (separator == ':') {
              state = State.INSTRUCTION_NAME;
            }
            break;
        }
        buffer = "";
      }

    }
    return instructions;
  }


}
