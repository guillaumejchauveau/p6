package demo;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Element;
import com.p6.lib.InitArgsParser;
import com.p6.lib.Library;
import java.util.ArrayList;
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
    elementGenerators.put("lotsOfChars", args -> () -> {
      var elements = new ArrayList<Element>();
      for (char c = 'A'; c <= 'K'; c++) {
        elements.add(new CharElement(c));
      }
      return elements;
    });
    return elementGenerators;
  }

  @Override
  public Map<String, InitArgsParser<ReactionPipelineStep>> getReactionPipelineSteps() {
    var reactionPipelineSteps = super.getReactionPipelineSteps();
    reactionPipelineSteps.put("chooseAChar", args -> (inputElements, cell) -> {
      var outputElements = new ArrayList<Element>();
      if (!(inputElements.get(0) instanceof CharElement && inputElements.get(1) instanceof CharElement)) {
        return null;
      }
      outputElements.add(inputElements.get(0));
      return outputElements;
    });
    return reactionPipelineSteps;
  }
}
