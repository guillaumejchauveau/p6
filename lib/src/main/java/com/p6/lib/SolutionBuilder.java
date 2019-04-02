package com.p6.lib;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.Collection;

/**
 * An object used to created easily a P6 program (a complete solution).
 */
public class SolutionBuilder {
  private Cell currentCell;
  private ReactionPipeline currentPipeline;
  private Boolean sealed;
  private LibraryRegistry registry;

  /**
   * Creates a solution builder.
   *
   * @param registry The library registry used to create named element generators
   *                 and reaction pipeline steps.
   */
  public SolutionBuilder(LibraryRegistry registry) {
    this.sealed = false;
    this.registry = registry;
  }

  private void checkSeal() {
    if (this.sealed) {
      throw new IllegalStateException("Solution is sealed");
    }
  }

  private void checkCurrentCell() {
    if (this.currentCell == null) {
      throw new IllegalStateException("Cell not created");
    }
  }

  private void checkCurrentPipeline() {
    if (this.currentPipeline == null) {
      throw new IllegalStateException("Pipeline not created");
    }
  }

  /**
   * Returns the created solution if the top cell is sealed.
   *
   * @return The complete solution
   */
  public Cell getSolution() {
    if (!this.sealed) {
      throw new IllegalStateException("Solution is not sealed");
    }
    return this.currentCell;
  }

  /**
   * Adds a new cell to the solution. It can either be the top cell or a sub-cell
   * of the current cell.
   *
   * @return The new cell
   */
  public Cell createCell() {
    this.checkSeal();
    var cell = new Cell();
    if (this.currentCell != null) {
      if (this.currentPipeline != null) {
        this.currentCell.addPipeline(this.currentPipeline);
        this.currentPipeline = null;
      }
      this.currentCell.addSubCell(cell);
    }
    this.currentCell = cell;
    return cell;
  }

  /**
   * Seals the current cell and goes back to its parent cell if existing.
   * If not, the sealed cell is the top cell: the solution is sealed.
   *
   * @return The solution builder for chained calls
   */
  public SolutionBuilder sealCell() {
    this.checkSeal();
    this.checkCurrentCell();

    if (this.currentPipeline != null) {
      this.currentCell.addPipeline(this.currentPipeline);
      this.currentPipeline = null;
    }
    if (this.currentCell.getParentCell() != null) {
      this.currentCell = this.currentCell.getParentCell();
    } else {
      this.sealed = true;
    }
    return this;
  }

  /**
   * Adds an element to the current cell.
   *
   * @param element The element to add
   * @return The solution builder for chained calls
   */
  public SolutionBuilder addElement(Element element) {
    this.checkSeal();
    this.checkCurrentCell();
    this.currentCell.addElement(element);
    return this;
  }

  /**
   * Adds a collection of elements to the current cell.
   *
   * @param elements The elements to add
   * @return The solution builder for chained calls
   */
  public SolutionBuilder addElement(Collection<Element> elements) {
    this.checkSeal();
    this.checkCurrentCell();
    this.currentCell.addAllElements(elements);
    return this;
  }

  /**
   * Adds the elements created by an element generator.
   *
   * @param elementGenerator The element generator
   * @return The solution builder for chained calls
   */
  public SolutionBuilder addElement(ElementGenerator elementGenerator) {
    this.checkSeal();
    this.checkCurrentCell();
    this.currentCell.addAllElements(elementGenerator);
    return this;
  }

  /**
   * Uses the library registry to load an element generator.
   *
   * @param name The name of the element generator in the registry
   * @param args The arguments for the generator's constructor
   * @return The solution builder for chained calls
   */
  public SolutionBuilder addElement(String name, Object... args) {
    return this.addElement(this.registry.createElementGenerator(name, args));
  }

  /**
   * Initializes a new reaction pipeline for the current cell.
   *
   * @return The solution builder for chained calls
   */
  public SolutionBuilder createPipeline() {
    this.checkSeal();
    this.checkCurrentCell();
    if (this.currentPipeline != null) {
      this.currentCell.addPipeline(this.currentPipeline);
    }
    this.currentPipeline = new ReactionPipeline();
    return this;
  }

  /**
   * Adds a reaction pipeline step to the current reaction pipeline.
   *
   * @param step The step to add
   * @return The solution builder for chained calls
   */
  public SolutionBuilder addStep(ReactionPipelineStep step) {
    this.checkSeal();
    this.checkCurrentCell();
    this.checkCurrentPipeline();
    this.currentPipeline.addStep(step);
    return this;
  }

  /**
   * Uses the library registry to load a reaction pipeline step.
   *
   * @param name The name of the reaction pipeline step in the registry
   * @param args The arguments for the step's constructor
   * @return The solution builder for chained calls
   */
  public SolutionBuilder addStep(String name, Object... args) {
    return this.addStep(this.registry.createReactionPipelineStep(name, args));
  }
}
