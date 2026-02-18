package fr.avenirsesr.portfolio.common.data.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for building {@link FetchGraph FetchGraphs} fluently. Provides a clean API to
 * dynamically define fetch plans and nested subgraphs, allowing precise control over entity
 * relationship loading at runtime.
 */
public final class FetchGraph {
  private final FetchGraph rootGraph;
  private final Map<String, FetchGraph> subGraphs = new LinkedHashMap<>();
  private FetchGraph currentGraph;

  private FetchGraph(FetchGraph rootGraph) {
    if (rootGraph == null) {
      rootGraph = this;
    }
    this.rootGraph = rootGraph;
    this.currentGraph = rootGraph;
  }

  /**
   * Creates a new {@code FetchGraph} instance.
   *
   * <p>This method initializes the root {@link FetchGraph}
   *
   * @return a new {@code FetchGraph} positioned at the root graph
   */
  public static FetchGraph init() {
    return new FetchGraph(null);
  }

  /**
   * Adds the specified attribute as a subgraph and moves the grapher’s cursor to it.
   *
   * <p>This both fetches the attribute and creates a new subgraph level for defining deeper
   * relationships.
   *
   * @param attribute the name of the attribute to fetch and add as a subgraph
   * @return the {@code FetchGraph} positioned at the newly added node
   * @throws IllegalArgumentException if the attribute node already exists in the graph
   */
  public FetchGraph add(String attribute) {
    if (currentGraph.subGraphs.containsKey(attribute)) {
      throw new IllegalArgumentException("Attribute " + attribute + " already exists");
    }

    FetchGraph child = new FetchGraph(rootGraph);
    currentGraph.subGraphs.put(attribute, child);
    currentGraph = child;
    return this;
  }

  /**
   * Fetches the specified attribute at the current graph level without moving deeper.
   *
   * <p>This is typically used when the attribute needs to be eagerly loaded but does not require
   * defining additional nested relationships.
   *
   * @param attribute the name of the attribute to fetch
   * @return the current {@code FetchGraph} instance
   */
  public FetchGraph fetch(String attribute) {
    currentGraph.subGraphs.putIfAbsent(attribute, new FetchGraph(rootGraph));
    return this;
  }

  /**
   * Moves the grapher’s cursor to an existing node within the graph. note that you may need to go
   * back to the root
   *
   * <p>This allows continuing the graph definition from a previously added subgraph. The node must
   * have been added beforehand using {@link #add(String)}, otherwise an {@code
   * IllegalArgumentException} will be thrown.
   *
   * @param attribute the name of the attribute node to navigate to
   * @return the {@code FetchGraph} positioned at the specified node
   * @throws IllegalArgumentException if the node does not exist in the graph
   */
  public FetchGraph from(String attribute) {
    if (!currentGraph.subGraphs.containsKey(attribute)) {
      throw new IllegalArgumentException("Node " + attribute + " does not exists");
    }
    currentGraph = currentGraph.subGraphs.get(attribute);
    return this;
  }

  /**
   * Moves the grapher’s cursor back to the root of the graph.
   *
   * <p>Useful when defining multiple top-level branches of the entity graph.
   *
   * @return the {@code FetchGraph} positioned at the root node
   */
  public FetchGraph root() {
    currentGraph = rootGraph;
    return this;
  }

  public Map<String, FetchGraph> children() {
    return subGraphs;
  }
}
