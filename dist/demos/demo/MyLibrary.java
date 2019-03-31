package demo;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.lib.InitArgsParser;
import com.p6.lib.Library;
import java.util.Map;

/**
 * Une bibliotheque P6, indiquee comme etant un service implementant {@link Library} dans
 * META-INF/services.
 */
public class MyLibrary extends Library {
  @Override
  public String getName() {
    return "MyLibrary";
  }

  @Override
  public Map<String, InitArgsParser<ElementGenerator>> getElementGenerators() {
    var elementGenerators = super.getElementGenerators();
    return elementGenerators;
  }

  @Override
  public Map<String, InitArgsParser<ReactionPipelineStep>> getReactionPipelineSteps() {
    var reactionPipelineSteps = super.getReactionPipelineSteps();
    return reactionPipelineSteps;
  }
}
