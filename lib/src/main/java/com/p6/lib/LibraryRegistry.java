package com.p6.lib;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

public class LibraryRegistry {
  private Map<String, Library> libraries;
  private Map<String, InitArgParser<? extends ElementGenerator>> elementGenerators;
  private Map<String, InitArgParser<? extends ReactionPipelineStep>> reactionPipelineSteps;

  public LibraryRegistry() {
    this.libraries = new HashMap<>();
    this.elementGenerators = new HashMap<>();
    this.reactionPipelineSteps = new HashMap<>();

    for (Library library : ServiceLoader.load(Library.class)) {
      this.addLibrary(library);
    }
  }

  public void addLibrary(Library library) {
    if (this.libraries.containsKey(library.getName())) {
      throw new RuntimeException("Duplicated library");
    }
    this.libraries.put(library.getName(), library);
    Map<String, InitArgParser<? extends ElementGenerator>> libraryElementGenerators =
      library.getElementGenerators();
    for (String name : libraryElementGenerators.keySet()) {
      this.registerElementGenerator(name, libraryElementGenerators.get(name));
    }
    Map<String, InitArgParser<? extends ReactionPipelineStep>> libraryPipelineSteps =
      library.getReactionPipelineSteps();
    for (String name : libraryPipelineSteps.keySet()) {
      this.registerPipelineStep(name, libraryPipelineSteps.get(name));
    }
  }

  public void registerElementGenerator(String name,
                                       InitArgParser<? extends ElementGenerator> elementGenerator) {
    if (this.elementGenerators.containsKey(name)) {
      throw new IllegalArgumentException("Duplicated element generator");
    }
    this.elementGenerators.put(name, elementGenerator);
  }

  public void registerPipelineStep(String name,
                                   InitArgParser<? extends ReactionPipelineStep> pipelineStep) {
    if (this.reactionPipelineSteps.containsKey(name)) {
      throw new IllegalArgumentException("Duplicated reaction pipeline step");
    }
    this.reactionPipelineSteps.put(name, pipelineStep);
  }

  public Set<String> getElementGeneratorNames() {
    return this.elementGenerators.keySet();
  }

  public Set<String> getReactionPipelineStepNames() {
    return this.reactionPipelineSteps.keySet();
  }

  public Boolean hasElementGeneratorName(String name) {
    return this.elementGenerators.containsKey(name);
  }

  public Boolean hasReactionPipelineStepName(String name) {
    return this.reactionPipelineSteps.containsKey(name);
  }

  public ElementGenerator createElementGenerator(String name, Object... args) {
    if (!this.elementGenerators.containsKey(name)) {
      throw new IllegalArgumentException("Unknown element generator");
    }
    return this.elementGenerators.get(name).parseArgs(args);
  }

  public ReactionPipelineStep createReactionPipelineStep(String name, Object... args) {
    if (!this.reactionPipelineSteps.containsKey(name)) {
      throw new IllegalArgumentException("Unknown reaction pipeline step");
    }
    return this.reactionPipelineSteps.get(name).parseArgs(args);
  }
}
