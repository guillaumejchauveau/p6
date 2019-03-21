package com.p6.lib;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;

import java.util.Collection;

public class SolutionBuilder {
  private Cell currentCell;
  private ReactionPipeline currentPipeline;
  private Boolean sealed;

  public SolutionBuilder() {
    this.sealed = false;
  }

  private void checkSeal() {
    if (this.sealed) {
      throw new RuntimeException("Solution is sealed");
    }
  }

  private void checkCurrentCell() {
    if (this.currentCell == null) {
      throw new RuntimeException("Cell not created");
    }
  }

  private void checkCurrentPipeline() {

  }

  public Cell getSolution() {
    if (!this.sealed) {
      throw new RuntimeException("Solution is not sealed");
    }
    return this.currentCell;
  }

  public Cell createCell() {
    this.checkSeal();
    Cell cell = new Cell();
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

  public SolutionBuilder addElement(Element element) {
    this.checkSeal();
    this.checkCurrentCell();
    this.currentCell.addElement(element);
    return this;
  }

  public SolutionBuilder addElement(Collection<Element> elements) {
    this.checkSeal();
    this.checkCurrentCell();
    this.currentCell.addAllElements(elements);
    return this;
  }

  public SolutionBuilder addElement(ElementGenerator elementGenerator) {
    this.checkSeal();
    this.checkCurrentCell();
    this.currentCell.addAllElements(elementGenerator);
    return this;
  }

  public SolutionBuilder createPipeline() {
    this.checkSeal();
    this.checkCurrentCell();
    if (this.currentPipeline != null) {
      this.currentCell.addPipeline(this.currentPipeline);
    }
    this.currentPipeline = new ReactionPipeline();
    return this;
  }

  public SolutionBuilder addStep(ReactionPipelineStep step) {
    this.checkSeal();
    this.checkCurrentCell();
    this.checkCurrentPipeline();
    this.currentPipeline.addStep(step);
    return this;
  }
}
