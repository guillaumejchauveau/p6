package com.p6.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipelineParser {
  enum State {
    START,
    END
  }

  private Map<String, Object> references;

  public PipelineParser(Map<String, Object> parentReferences) {
    this.references = new HashMap<>(parentReferences);
  }

  public List<Instruction> parse(String clause) {
    List<Instruction> instructions = new ArrayList<>();
    String buffer = "";
    State state = State.START;

    for (int i = 0; i < clause.length(); i++) {
      if (!",:();".contains(Character.toString(clause.charAt(i)))) {
        buffer = buffer + (clause.charAt(i));
      } else {
        switch (state) {
          case START:
            state = State.END;
            break;
          case END:
            break;
        }
      }
    }
    return instructions;
  }


}
