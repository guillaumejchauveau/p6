package com.p6.core.solution;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.reaction.ReactionProduct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A cell is a standalone p6 program. It contains it's own elements and rules and can also
 * contain sub-programs (sub-cells).
 */
public class Cell {
  /**
   * The program's rules. A rule or reaction is defined by a condition that
   * must be fulfilled and the products of the reaction.
   */
  private Map<ReactionCondition, ReactionProduct> rules;
  /**
   * The solution's elements.
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
  /**
   * @see Cell#isDissolved()
   */
  private Boolean isDissolved;
  private Logger logger;

  /**
   * Creates an empty cell.
   */
  public Cell() {
    this.rules = new HashMap<>();
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
   * @return The collection of the sub-cells
   */
  public Collection<Cell> getSubCells() {
    return this.subCells;
  }

  /**
   * Adds a cell inside the current cell.
   *
   * @param subCell The cell to add
   */
  public void addSubCell(Cell subCell) {
    if (subCell == this || subCell == this.getParentCell()) {
      throw new IllegalArgumentException();
    }
    subCell.setParentCell(this);
    this.subCells.add(subCell);
  }

  /**
   * @param subCell The sub-cell to remove
   */
  private void removeSubCell(Cell subCell) {
    this.subCells.remove(subCell);
  }

  /**
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
      throw new IllegalArgumentException();
    }
    this.parentCell = parentCell;
  }

  /**
   * Adds a rule to the cell given a reaction condition and a reaction product.
   *
   * @param reactionCondition The condition for the reaction
   * @param reactionProduct   The product of the reaction
   */
  public void createRule(ReactionCondition reactionCondition, ReactionProduct reactionProduct) {
    this.rules.put(reactionCondition, reactionProduct);
  }

  public String toString() {
    return this.rules.size() + " rules, " + this.elements.size() + " elements";
  }

  /**
   * A getter used for testing if a reaction can be applied.
   *
   * @return A set of the registered reaction conditions.
   */
  public Set<ReactionCondition> getConditions() {
    return this.rules.keySet();
  }

  /**
   * Adds a reaction's product given it's corresponding condition.
   *
   * @param reactionCondition The condition of the reaction
   * @param x                 The first element for the reaction
   * @param y                 The second element
   */
  public void applyRule(ReactionCondition reactionCondition, Element x, Element y) {
    ReactionProduct product = this.rules.get(reactionCondition);
    product.react(x, y, this);
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
  public void addElement(Element element) {
    this.elements.add(element);
  }

  /**
   * Adds a collection of elements to the cell.
   *
   * @param elements The elements to add
   */
  public void addAllElements(Collection<Element> elements) {
    this.elements.addAll(elements);
  }

  /**
   * Selects a random element from the cell. The element is removed from the
   * cell so that it cannot be re-selected.
   *
   * @return The chosen element
   */
  public Element chooseElement() {
    int elementIndex = this.random.nextInt(this.getElementsCount());
    Element element = this.elements.get(elementIndex);
    this.elements.remove(elementIndex);
    return element;
  }

  /**
   * Marks the cell as dissolved and put all it's remaining elements in the parent cell.
   */
  public void dissolve() {
    if (this.getParentCell() == null) {
      throw new IllegalStateException("Cell doesn't have a parent cell");
    }
    this.parentCell.addAllElements(this.elements);
    this.parentCell.removeSubCell(this);
    this.isDissolved = true;
  }
}
