package com.p6.core.solution;

import com.p6.utils.tests.TestCase;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test cases for {@link Cell}.
 */
public class CellTest extends TestCase {
  /**
   * Tests rules management and execution of the cell.
   */
  @Ignore
  @Test
  public void rules() {
    /*Cell cell = new Cell();
    ReactionCondition condition = new ReactionCondition() {
      @Override
      public Boolean test(Element x, Element y) {
        return null;
      }
    };
    ReactionProduct product = new ReactionProduct() {
      @Override
      public void react(Element x, Element y) {
        x.assign(1);
      }
    };
    cell.createRule(condition, product);
    Set<ReactionCondition> cellConditions = cell.getConditions();
    Assert.assertEquals("Cell should have 1 condition", 1, cellConditions.size());
    Assert.assertEquals("Registered condition should be the same has the original", condition,
      cellConditions.iterator().next());
    Element<Integer> x = new Element<>(0) {
    };
    Element<Integer> y = new Element<>(0) {
    };
    cell.applyRule(condition, x, y);
    Assert.assertEquals("Product should be called", 1, x.evaluate().intValue());*/
  }

  /**
   * Tests the cell's elements management methods.
   */
  @Test
  public void elements() {
    Element<Integer> x = new Element<Integer>(0) {
    };
    Cell cell = new Cell();
    Assert.assertEquals("Cell should be initialized with 0 elements", 0,
        cell.getElementsCount().intValue());
    cell.addElement(x);
    Assert.assertEquals("Cell should have 1 element", 1,
        cell.getElementsCount().intValue());
    Assert.assertEquals("Cell should choose the only available element", x, cell.chooseElement());
    Assert.assertEquals("Cell should remove chosen elements", 0,
        cell.getElementsCount().intValue());
    List<Element> elements = new ArrayList<>();
    cell.addAllElements(elements);
    elements.add(x);
    Assert.assertEquals("Cell should have 0 elements", 0,
        cell.getElementsCount().intValue());
    cell.addAllElements(elements);
    Assert.assertEquals("Cell should have 1 element", 1,
        cell.getElementsCount().intValue());
  }

  /**
   * Cell should throw an exception when asked to choose an element if there is none.
   */
  @Test(expected = IllegalArgumentException.class)
  public void emptyCellElementChoice() {
    Cell cell = new Cell();
    cell.chooseElement();
  }

  /**
   * Tests sub-cells management and dissolution.
   */
  @Test
  public void subCells() {
    Cell rootCell = new Cell();
    Assert.assertFalse("Cell should not be initialized dissolved", rootCell.isDissolved());
    Assert.assertNull("Cell should not be initialized with a parent cell",
        rootCell.getParentCell());
    Assert.assertEquals("Cell should not be initialized with sub-cells", 0,
        rootCell.getSubCells().size());
    Cell subCell1 = new Cell();
    subCell1.setParentCell(rootCell);
    Assert.assertEquals("Parent cell should be set", rootCell, subCell1.getParentCell());
    subCell1 = new Cell();
    rootCell.addSubCell(subCell1);
    Assert.assertEquals("Parent cell should have 1 sub-cell", 1, rootCell.getSubCells().size());
    Assert.assertEquals(subCell1, rootCell.getSubCells().iterator().next());
    Assert.assertEquals("Sub-cell 1 should have parent cell", rootCell, subCell1.getParentCell());

    Cell subCell2 = new Cell();
    subCell1.addElement(new Element<Integer>(0) {
    });
    subCell1.addElement(new Element<Integer>(1) {
    });
    subCell2.addElement(new Element<Integer>(2) {
    });
    subCell2.addElement(new Element<Integer>(3) {
    });
    subCell1.addSubCell(subCell2);
    Assert.assertEquals("Root cell should have 1 sub-cell", 1, rootCell.getSubCells().size());
    subCell2.dissolve();
    Assert.assertTrue("Sub-cell 2 should be dissolved", subCell2.isDissolved());
    Assert.assertEquals("Sub-cell 1 should have 4 elements", 4,
        subCell1.getElementsCount().intValue());
    Assert.assertEquals("Root cell should have 0 elements", 0,
        rootCell.getElementsCount().intValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void subCellLoop() {
    Cell rootCell = new Cell();
    Cell parentCell = new Cell();
    rootCell.addSubCell(parentCell);
    parentCell.addSubCell(rootCell);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parentCellLoop() {
    Cell parentCell = new Cell();
    Cell subCell = new Cell();
    parentCell.addSubCell(subCell);
    parentCell.setParentCell(subCell);
  }

  @Test(expected = IllegalArgumentException.class)
  public void subCellSelfLoop() {
    Cell parentCell = new Cell();
    parentCell.addSubCell(parentCell);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parentCellSelfLoop() {
    Cell subCell = new Cell();
    subCell.setParentCell(subCell);
  }

  @Test(expected = IllegalStateException.class)
  public void nonSubCellDissolution() {
    Cell cell = new Cell();
    cell.dissolve();
  }
}
