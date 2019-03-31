package demo;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.lib.InitArgsParser;
import com.p6.lib.Library;
import java.util.HashMap;
import java.util.Map;

public class MyLibrary extends Library {
  @Override
  public String getName() {
    return "MyLibrary";
  }

  @Override
  public Map<String, InitArgsParser<? extends ElementGenerator>> getElementGenerators() {
    var elementGenerators = super.getElementGenerators();
    return elementGenerators;
  }

  @Override
  public Map<String, InitArgsParser<? extends ReactionPipelineStep>> getReactionPipelineSteps() {
    var reactionPipelineSteps = super.getReactionPipelineSteps();
    return reactionPipelineSteps;
  }
}
