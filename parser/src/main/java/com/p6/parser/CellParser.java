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
 * A class that uses a {@link StructureParser} to create a {@link Cell}, with all it's
 * {@link ReactionPipeline}s and initial {@link com.p6.core.solution.Element}s given a input
 * string.
 * <br>
 * The structure provided by the structure parser is checked for validity and all the
 * objects corresponding to the cell described are instantiated and configured.
 * <br>
 * A cell structure is a "map" providing the following values:
 * <br>
 * - name: A string to reference the cell in pipelines.
 * <br>
 * - elements: A list of strings, each with the form described in the
 * {@link ElementGeneratorParser}.
 * <br>
 * - rules: A list of strings, each with the form described in the {@link ReactionPipelineParser}.
 * <br>
 * - subCells: An optional list of cell structures.
 *
 * @see StructureParser
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
   * Initializes the parser with a string as source. The string is directly converted to a
   * structure using a {@link StructureParser}.
   *
   * @param source The string to parse
   * @throws InvalidSyntaxException Thrown if the source string cannot be parsed into a structure
   *                                or if the resulting structure's data is incorrect or missing
   */
  public CellParser(String source) throws InvalidSyntaxException {
    this(new StructureParser(source).parseNextStructure());
  }

  /**
   * Initializes the parser with a structure as source. The structure is not modified.
   *
   * @param structure The structure to parse
   * @throws InvalidSyntaxException Thrown if the structure's data is incorrect or missing
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
   * Loads the data contained in the source structure. If the data is incorrect or missing, an
   * {@link InvalidSyntaxException} is thrown.
   */
  private void loadCellStructure() throws InvalidSyntaxException {
    // Main structure.
    if (!(this.structure instanceof Map)) {
      throw new InvalidSyntaxException("Cell structure expected");
    }
    var structure = (Map) this.structure;

    // Cell name.
    try {
      if (structure.get("name") == null) {
        throw new NullPointerException();
      }
      if (!(structure.get("name") instanceof String)) {
        throw new InvalidSyntaxException("Cell name expected to be a string");
      }
      this.name = ((String) structure.get("name")).trim();
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

      for (var rule : (List) structure.get("rules")) {
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
      for (var element : (List) structure.get("elements")) {
        if (!(element instanceof String)) {
          throw new InvalidSyntaxException("Cell element expected to be a string");
        }
        this.elementGeneratorsSources.add((String) element);
      }
    } catch (ClassCastException | NullPointerException e) {
      throw new InvalidSyntaxException("Cell elements expected");
    }

    // Cell sub-cells. Optional.
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
   * Parses the sub-cell structures load by {@link CellParser#loadCellStructure} and stores the
   * created {@link Cell}s.
   *
   * @param registry The library registry used to created the sub-cells element generators and
   *                 reaction pipelines
   */
  private void parseSubCells(LibraryRegistry registry) throws InvalidSyntaxException {
    for (var subCellStructure : this.subCellsStructures) {
      var subCellParser = new CellParser(subCellStructure);
      var subCell = subCellParser.create(registry);
      this.subCells.add(subCell);
      this.references.put(subCell.getName(), subCell);
    }
  }

  /**
   * Parses the strings representing the cell's {@link ReactionPipeline}s loaded by
   * {@link CellParser#loadCellStructure} using a {@link ReactionPipelineParser}. The created
   * reaction pipelines are then stored.
   *
   * @param registry The library registry used to create the reaction pipelines' steps
   */
  private void parseReactionPipelines(LibraryRegistry registry) throws InvalidSyntaxException {
    try {
      var reactionPipelineParser = new ReactionPipelineParser(this.references);
      for (var reactionPipelineSource : this.reactionPipelinesSources) {
        this.reactionPipelines.add(reactionPipelineParser.create(reactionPipelineSource, registry));
      }
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Parses the strings representing the cell's {@link ElementGenerator}s loaded by
   * {@link CellParser#loadCellStructure} using an {@link ElementGeneratorParser}. The created
   * element generators are then stored.
   *
   * @param registry The library registry used to create the element generators
   */
  private void parseElementGenerators(LibraryRegistry registry) throws InvalidSyntaxException {
    try {
      var elementGeneratorParser = new ElementGeneratorParser(this.references);
      for (var elementGeneratorSource : this.elementGeneratorsSources) {
        this.elementGenerators.add(elementGeneratorParser.create(elementGeneratorSource, registry));
      }
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates the cell corresponding to the structure.
   *
   * @param registry The library registry used to created the cell's and sub-cells' reaction
   *                 pipelines and element generators
   * @return The created cell
   * @throws InvalidSyntaxException Thrown if any string representing a reaction pipeline or an
   *                                element generator cannot be parsed
   */
  public Cell create(LibraryRegistry registry) throws InvalidSyntaxException {
    this.parseSubCells(registry);
    this.parseElementGenerators(registry);
    this.parseReactionPipelines(registry);
    this.logger.debug("Creating cell '" + this.name + "'");
    var cell = new Cell();
    cell.setName(this.name);
    for (var subCell : this.subCells) {
      cell.addSubCell(subCell);
    }
    this.logger.debug("Added " + this.subCells.size() + " sub-cells");
    for (var reactionPipeline : this.reactionPipelines) {
      cell.addPipeline(reactionPipeline);
    }
    this.logger.debug("Added " + this.reactionPipelines.size() + " reaction pipelines");
    for (var elementGenerator : this.elementGenerators) {
      cell.addAllElements(elementGenerator);
    }
    this.logger.debug("Added " + this.elementGenerators.size() + " element generators");
    return cell;
  }
}
