package com.p6.parser;

import com.p6.core.genesis.ElementGenerator;
import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.solution.Cell;
import com.p6.lib.LibraryRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class CellParser {
  private Object structure;
  private Map<String, Object> references;
  private String name;
  private List<String> reactionPipelinesSources;
  private List<ReactionPipeline> reactionPipelines;
  private List<String> elementGeneratorsSources;
  private List<ElementGenerator> elementGenerators;
  private List subCellsStructures;
  private List<Cell> subCells;
  private Logger logger;

  /**
   *
   * @param source
   * @throws InvalidSyntaxException
   */
  public CellParser(String source) throws InvalidSyntaxException {
    this(new StructureParser(source).parseNextStructure());
  }

  /**
   *
   * @param structure
   * @throws InvalidSyntaxException
   */
  public CellParser(Object structure) throws InvalidSyntaxException {
    this.structure = structure;
    this.references = new HashMap<>();
    this.reactionPipelinesSources = new ArrayList<>();
    this.reactionPipelines = new ArrayList<>();
    this.elementGeneratorsSources = new ArrayList<>();
    this.elementGenerators = new ArrayList<>();
    this.subCellsStructures = new ArrayList<>();
    this.subCells = new ArrayList<>();
    this.logger = LogManager.getLogger();
    this.loadCellStructure();
  }

  /**
   *
   * @return
   */
  public String getName() {
    return this.name;
  }

  /**
   *
   * @throws InvalidSyntaxException
   */
  private void loadCellStructure() throws InvalidSyntaxException {
    // Main structure.
    if (!(this.structure instanceof Map)) {
      throw new InvalidSyntaxException("Cell structure expected");
    }
    Map structure = (Map) this.structure;

    // Cell name.
    try {
      if (structure.get("name") == null) {
        throw new NullPointerException();
      }
      if (!(structure.get("name") instanceof String)) {
        throw new InvalidSyntaxException("Cell name expected to be a string");
      }
      this.name = (String) structure.get("name");
    } catch (ClassCastException | NullPointerException e) {
      throw new InvalidSyntaxException("Cell name expected");
    }

    // Cell rules.
    try {
      if (structure.get("rules") == null) {
        throw new NullPointerException();
      }
      if (!(structure.get("rules") instanceof List)) {
        throw new InvalidSyntaxException("Cell rules expected to be a list");
      }

      for (Object rule : (List) structure.get("rules")) {
        if (!(rule instanceof String)) {
          throw new InvalidSyntaxException("Cell rule expected to be a string");
        }
        this.reactionPipelinesSources.add((String) rule);
      }

    } catch (ClassCastException | NullPointerException e) {
      throw new InvalidSyntaxException("Cell rules expected");
    }

    // Cell elements.
    try {
      if (structure.get("elements") == null) {
        throw new NullPointerException();
      }
      if (!(structure.get("elements") instanceof List)) {
        throw new InvalidSyntaxException("Cell elements expected to be a list");
      }
      for (Object element : (List) structure.get("elements")) {
        if (!(element instanceof String)) {
          throw new InvalidSyntaxException("Cell element expected to be a string");
        }
        this.elementGeneratorsSources.add((String) element);
      }
    } catch (ClassCastException | NullPointerException e) {
      throw new InvalidSyntaxException("Cell elements expected");
    }

    // Cell sub-cells.
    try {
      if (structure.get("subCells") == null) {
        return;
      }
      if (!(structure.get("subCells") instanceof List)) {
        throw new InvalidSyntaxException("Cell subCells expected to be a list");
      }
      this.subCellsStructures = (List) structure.get("subCells");
    } catch (ClassCastException | NullPointerException e) {
      return;
    }
  }

  /**
   *
   * @param registry
   * @throws InvalidSyntaxException
   */
  private void parseSubCells(LibraryRegistry registry) throws InvalidSyntaxException {
    for (Object subCellStructure : this.subCellsStructures) {
      CellParser subCellParser = new CellParser(subCellStructure);
      Cell subCell = subCellParser.create(registry);
      this.subCells.add(subCell);
      this.references.put(subCellParser.getName(), subCell);
    }
  }

  /**
   *
   * @param registry
   * @throws InvalidSyntaxException
   */
  private void parseReactionPipelines(LibraryRegistry registry) throws InvalidSyntaxException {
    try {
      for (String reactionPipelineSource : this.reactionPipelinesSources) {
        ReactionPipelineParser reactionPipelineParser = new ReactionPipelineParser(this.references);
        this.reactionPipelines.add(reactionPipelineParser.create(reactionPipelineSource, registry));
      }
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param registry
   * @throws InvalidSyntaxException
   */
  private void parseElementGenerators(LibraryRegistry registry) throws InvalidSyntaxException {
    try {
      for (String elementGeneratorSource : this.elementGeneratorsSources) {
        ElementGeneratorParser elementGeneratorParser = new ElementGeneratorParser(this.references);
        this.elementGenerators.add(elementGeneratorParser.create(elementGeneratorSource, registry));
      }
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param registry
   * @return
   * @throws InvalidSyntaxException
   */
  public Cell create(LibraryRegistry registry) throws InvalidSyntaxException {
    this.parseSubCells(registry);
    this.parseElementGenerators(registry);
    this.parseReactionPipelines(registry);
    Cell cell = new Cell();
    for (Cell subCell : this.subCells) {
      cell.addSubCell(subCell);
    }
    for (ReactionPipeline reactionPipeline : this.reactionPipelines) {
      cell.addPipeline(reactionPipeline);
    }
    for (ElementGenerator elementGenerator : this.elementGenerators) {
      cell.addAllElements(elementGenerator);
    }
    return cell;
  }
}
