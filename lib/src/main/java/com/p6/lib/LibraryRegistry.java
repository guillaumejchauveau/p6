package com.p6.lib;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipelineStep;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An object containing all the {@link ElementGenerator}s and {@link ReactionPipelineStep}s
 * provided by libraries.
 * <br>
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

    for (var library : ServiceLoader.load(Library.class)) {
      this.addLibrary(library);
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
    var libraryElementGenerators = library.getElementGenerators();
    for (var name : libraryElementGenerators.keySet()) {
      this.registerElementGenerator(name, libraryElementGenerators.get(name));
    }
    var libraryPipelineSteps = library.getReactionPipelineSteps();
    for (var name : libraryPipelineSteps.keySet()) {
      this.registerPipelineStep(name, libraryPipelineSteps.get(name));
    }
    this.logger.info("Library '" + library.getName() + "' loaded");
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
      throw new IllegalArgumentException("Duplicated element generator '" + name + "'");
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
      throw new IllegalArgumentException("Duplicated reaction pipeline step '" + name + "'");
    }
    this.reactionPipelineSteps.put(name, pipelineStep);
  }

  /**
   * Instantiate an {@link ElementGenerator}.
   *
   * @param name The name of the element generator
   * @param args The arguments for the constructor
   * @return The created element generator
   */
  public ElementGenerator createElementGenerator(String name, Object... args) {
    if (!this.elementGenerators.containsKey(name)) {
      throw new IllegalArgumentException("Unknown element generator '" + name + "'");
    }
    return this.elementGenerators.get(name).parseArgs(args);
  }

  /**
   * Instantiate a {@link ReactionPipelineStep}.
   *
   * @param name The name of the reaction pipeline step
   * @param args The arguments for the constructor
   * @return The created reaction pipeline step
   */
  public ReactionPipelineStep createReactionPipelineStep(String name, Object... args) {
    if (!this.reactionPipelineSteps.containsKey(name)) {
      throw new IllegalArgumentException("Unknown reaction pipeline step '" + name + "'");
    }
    return this.reactionPipelineSteps.get(name).parseArgs(args);
  }
}
