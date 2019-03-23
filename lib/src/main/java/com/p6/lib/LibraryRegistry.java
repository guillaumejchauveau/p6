package com.p6.lib;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An object containing all the {@link ElementGenerator}s and {@link ReactionPipelineStep}s
 * provided by libraries.
 * The libraries are loaded using a {@link ServiceLoader}.
 */
public class LibraryRegistry {
  private Map<String, Library> libraries;
  private Map<String, InitArgsParser<? extends ElementGenerator>> elementGenerators;
  private Map<String, InitArgsParser<? extends ReactionPipelineStep>> reactionPipelineSteps;
  private Logger logger;

  /**
   * Creates a registry and loads all the services implementing {@link Library}.
   */
  public LibraryRegistry() {
    this.libraries = new HashMap<>();
    this.elementGenerators = new HashMap<>();
    this.reactionPipelineSteps = new HashMap<>();
    this.logger = LogManager.getLogger();

    for (Library library : ServiceLoader.load(Library.class)) {
      this.addLibrary(library);
      this.logger.debug("Library '" + library.getName() + "' loaded");
    }
  }

  /**
   * Adds all the provided objects by a library to the registry.
   *
   * @param library The new library
   */
  public void addLibrary(Library library) {
    if (this.libraries.containsKey(library.getName())) {
      throw new IllegalArgumentException("Duplicated library");
    }
    this.libraries.put(library.getName(), library);
    Map<String, InitArgsParser<? extends ElementGenerator>> libraryElementGenerators =
        library.getElementGenerators();
    for (String name : libraryElementGenerators.keySet()) {
      this.registerElementGenerator(name, libraryElementGenerators.get(name));
    }
    Map<String, InitArgsParser<? extends ReactionPipelineStep>> libraryPipelineSteps =
        library.getReactionPipelineSteps();
    for (String name : libraryPipelineSteps.keySet()) {
      this.registerPipelineStep(name, libraryPipelineSteps.get(name));
    }
  }

  /**
   * Registers a new {@link ElementGenerator}.
   *
   * @param name             The unique name for the element generator
   * @param elementGenerator The {@link InitArgsParser} used to instantiate the object
   */
  public void registerElementGenerator(
      String name, InitArgsParser<? extends ElementGenerator> elementGenerator) {
    if (this.elementGenerators.containsKey(name)) {
      throw new IllegalArgumentException("Duplicated element generator");
    }
    this.elementGenerators.put(name, elementGenerator);
  }

  /**
   * Registers a new {@link ReactionPipelineStep}.
   *
   * @param name         The unique name for the reaction pipeline step
   * @param pipelineStep The {@link InitArgsParser} used to instantiate the object
   */
  public void registerPipelineStep(String name,
                                   InitArgsParser<? extends ReactionPipelineStep> pipelineStep) {
    if (this.reactionPipelineSteps.containsKey(name)) {
      throw new IllegalArgumentException("Duplicated reaction pipeline step");
    }
    this.reactionPipelineSteps.put(name, pipelineStep);
  }

  /**
   * A set of all the registered element generator names.
   */
  public Set<String> getElementGeneratorNames() {
    return this.elementGenerators.keySet();
  }

  /**
   * A set of all the registered reaction pipeline step names.
   */
  public Set<String> getReactionPipelineStepNames() {
    return this.reactionPipelineSteps.keySet();
  }

  /**
   * Instantiate an element generator.
   *
   * @param name The name of the element generator
   * @param args The arguments for the constructor
   * @return The created element generator
   */
  public ElementGenerator createElementGenerator(String name, Object... args) {
    if (!this.elementGenerators.containsKey(name)) {
      throw new IllegalArgumentException("Unknown element generator");
    }
    return this.elementGenerators.get(name).parseArgs(args);
  }

  /**
   * Instantiate a reaction pipeline step.
   *
   * @param name The name of the reaction pipeline step
   * @param args The arguments for the constructor
   * @return The created reaction pipeline step
   */
  public ReactionPipelineStep createReactionPipelineStep(String name, Object... args) {
    if (!this.reactionPipelineSteps.containsKey(name)) {
      throw new IllegalArgumentException("Unknown reaction pipeline step");
    }
    return this.reactionPipelineSteps.get(name).parseArgs(args);
  }
}
