package com.p6.core.solution;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipeline;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A cell is a standalone P6 program. It contains it's own elements and pipelines and can also
 * contain sub-programs (sub-cells).
 */
public class Cell {
  /**
   * The program's rules.
   */
  private List<ReactionPipeline> pipelines;
  /**
   * The cell's elements.
   */
  private List<Element> elements;
  /**
   * Cells inside the current cell.
   */
  private List<Cell> subCells;
  /**
   * The cell containing the current cell.
   */
  private Cell parentCell;
  /**
   * A random number generator used to select elements of the cell.
   */
  private Random random;
  private Boolean isDissolved;
  private final Logger logger;

  /**
   * Creates an empty cell.
   */
  public Cell() {
    this.pipelines = new ArrayList<>();
    this.elements = new ArrayList<>();
    this.subCells = new ArrayList<>();
    this.random = new Random();
    this.isDissolved = false;
    this.logger = LogManager.getLogger();
  }

  /**
   * Indicates if the cell is still active.
   *
   * @return The status of the cell
   */
  public Boolean isDissolved() {
    return this.isDissolved;
  }

  /**
   * Getter for the sub-cells.
   *
   * @return The collection of the sub-cells
   */
  public Collection<Cell> getSubCells() {
    return Collections.unmodifiableCollection(this.subCells);
  }

  /**
   * Adds a cell inside the current cell.
   *
   * @param subCell The cell to add
   */
  public void addSubCell(Cell subCell) {
    if (subCell == this || subCell == this.getParentCell()) {
      throw new IllegalArgumentException("Given sub-cell is invalid");
    }
    subCell.setParentCell(this);
    this.subCells.add(subCell);
  }

  /**
   * Removes a sub-cell.
   *
   * @param subCell The sub-cell to remove
   */
  private void removeSubCell(Cell subCell) {
    this.subCells.remove(subCell);
  }

  /**
   * Getter for the parent cell.
   *
   * @return The cell's parent cell
   */
  public Cell getParentCell() {
    return this.parentCell;
  }

  /**
   * Updates the cell's parent cell.
   *
   * @param parentCell The cell's parent cell
   */
  public void setParentCell(Cell parentCell) {
    if (parentCell == this || this.subCells.contains(parentCell)) {
      throw new IllegalArgumentException("Given parent cell is invalid");
    }
    this.parentCell = parentCell;
  }

  /**
   * Adds a rule to the cell.
   *
   * @param pipeline The pipeline describing the rule
   */
  public void addPipeline(ReactionPipeline pipeline) {
    this.pipelines.add(pipeline);
  }

  /**
   * A getter used for testing if a reaction can be applied.
   *
   * @return A set of the registered reaction conditions.
   */
  public List<ReactionPipeline> getPipelines() {
    return this.pipelines;
  }

  public Collection<Element> getElements() {
    return this.elements;
  }

  /**
   * The number of elements in the cell.
   *
   * @return The number of element in the cell
   */
  public Integer getElementsCount() {
    return this.elements.size();
  }

  /**
   * Adds an element to the cell.
   *
   * @param element The element to add
   */
  public synchronized void addElement(Element element) {
    this.elements.add(element);
  }

  /**
   * Adds a collection of elements to the cell.
   *
   * @param elements The elements to add
   */
  public synchronized void addAllElements(Collection<Element> elements) {
    this.elements.addAll(elements);
  }

  /**
   * Adds the elements created by an {@link ElementGenerator}.
   *
   * @param generator The element generator
   */
  public void addAllElements(ElementGenerator generator) {
    this.addAllElements(generator.generate());
  }

  /**
   * Selects a random element from the cell. The element is removed from the
   * cell so that it cannot be re-selected.
   *
   * @return The chosen element
   */
  public synchronized Element chooseElement() {
    int elementIndex = this.random.nextInt(this.getElementsCount());
    Element element = this.elements.get(elementIndex);
    this.elements.remove(elementIndex);
    return element;
  }

  /**
   * Marks the cell as dissolved and put all it's remaining elements in the parent cell.
   */
  public synchronized void dissolve() {
    if (this.getParentCell() == null) {
      throw new IllegalStateException("Cell doesn't have a parent cell");
    }
    if (this.isDissolved()) {
      return;
    }
    this.parentCell.addAllElements(this.elements);
    this.parentCell.removeSubCell(this);
    this.isDissolved = true;
    this.logger.debug("Cell dissolved");
  }

  /**
   * Injects elements in a sub-cell.
   *
   * @param elements The elements to inject
   * @param subCell  The sub-cell
   */
  public void inject(Collection<Element> elements, Cell subCell) {
    if (this.subCells.contains(subCell)) {
      subCell.addAllElements(elements);
    }
  }
}
