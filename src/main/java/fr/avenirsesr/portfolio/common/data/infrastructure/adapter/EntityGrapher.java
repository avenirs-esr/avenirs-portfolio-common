package fr.avenirsesr.portfolio.common.data.infrastructure.adapter;

import fr.avenirsesr.portfolio.common.data.domain.FetchGraph;
import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeNode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for building JPA {@link EntityGraph EntityGraphs} fluently. Provides a clean API to
 * dynamically define fetch plans and nested subgraphs, allowing precise control over entity
 * relationship loading at runtime.
 *
 * @param <T> the root entity type
 */
public class EntityGrapher<T> {

  private final EntityGraph<T> rootGraph;
  private final Map<String, Subgraph<?>> subgraphs = new HashMap<>();
  private Object currentGraph;

  private EntityGrapher(Class<T> rootClass, EntityManager em) {
    this.rootGraph = em.createEntityGraph(rootClass);
    this.currentGraph = rootGraph;
  }

  /**
   * Creates a new {@code EntityGrapher} instance for the given root entity class.
   *
   * <p>This method initializes the root {@link EntityGraph} using the provided {@link
   * EntityManager}. It marks the starting point from which the graph will be constructed.
   *
   * @param rootClass the root entity class for which the graph will be created
   * @param em the {@link EntityManager} used to instantiate the underlying {@link EntityGraph}
   * @return a new {@code EntityGrapher} positioned at the root graph
   */
  public static <T> EntityGrapher<T> of(Class<T> rootClass, EntityManager em) {
    return new EntityGrapher<>(rootClass, em);
  }

  /**
   * Moves the grapher’s cursor to an existing node within the graph.
   *
   * <p>This allows continuing the graph definition from a previously added subgraph. The node must
   * have been added beforehand using {@link #add(String)}, otherwise an {@code
   * IllegalArgumentException} will be thrown.
   *
   * @param node the name of the attribute node to navigate to
   * @return the {@code EntityGrapher} positioned at the specified node
   * @throws IllegalArgumentException if the node does not exist in the graph
   */
  public EntityGrapher<T> from(String node) {
    if (!subgraphs.containsKey(node)) {
      throw new IllegalArgumentException("Node " + node + " does not exists");
    }
    currentGraph = subgraphs.get(node);
    return this;
  }

  /**
   * Adds the specified attribute as a subgraph and moves the grapher’s cursor to it.
   *
   * <p>This both fetches the attribute and creates a new subgraph level for defining deeper
   * relationships.
   *
   * @param attribute the name of the attribute to fetch and add as a subgraph
   * @return the {@code EntityGrapher} positioned at the newly added node
   * @throws IllegalArgumentException if the attribute node already exists in the graph
   */
  public EntityGrapher<T> add(String attribute) {
    if (subgraphs.containsKey(attribute)) {
      throw new IllegalArgumentException("Attribute " + attribute + " already exists");
    }

    fetch(attribute);
    currentGraph = subgraphs.get(attribute);
    return this;
  }

  /**
   * Fetches the specified attribute at the current graph level without moving deeper.
   *
   * <p>This is typically used when the attribute needs to be eagerly loaded but does not require
   * defining additional nested relationships.
   *
   * @param attribute the name of the attribute to fetch
   * @return the current {@code EntityGrapher} instance
   */
  public EntityGrapher<T> fetch(String attribute) {
    switch (currentGraph) {
      case EntityGraph<?> entityGraph -> entityGraph.addAttributeNodes(attribute);
      case Subgraph<?> subgraph -> subgraph.addAttributeNodes(attribute);
      default -> throw new IllegalStateException("Graph type not supported for " + attribute);
    }
    Object newGraph =
        switch (currentGraph) {
          case EntityGraph<?> entityGraph -> entityGraph.addSubgraph(attribute);
          case Subgraph<?> subgraph -> subgraph.addSubgraph(attribute);
          default -> throw new IllegalStateException("Graph type not supported for " + attribute);
        };

    subgraphs.put(attribute, (Subgraph<?>) newGraph);
    return this;
  }

  /**
   * Moves the grapher’s cursor back to the root of the graph.
   *
   * <p>Useful when defining multiple top-level branches of the entity graph.
   *
   * @return the {@code EntityGrapher} positioned at the root node
   */
  public EntityGrapher<T> root() {
    currentGraph = rootGraph;
    return this;
  }

  /**
   * Finalizes the graph construction and returns the built {@link EntityGraph}.
   *
   * <p>This graph can then be passed to a JPA query using {@code
   * query.setHint("jakarta.persistence.fetchgraph", graph)}.
   *
   * @return the fully built {@link EntityGraph}
   */
  public EntityGraph<T> build() {
    return rootGraph;
  }

  public List<String> attributes() {
    var attributes =
        switch (currentGraph) {
          case EntityGraph<?> entityGraph ->
              entityGraph.getAttributeNodes().stream()
                  .map(AttributeNode::getAttributeName)
                  .toList();
          case Subgraph<?> subgraph ->
              subgraph.getAttributeNodes().stream().map(AttributeNode::getAttributeName).toList();
          default -> throw new IllegalStateException("Graph type not supported");
        };
    root();
    return attributes;
  }

  /**
   * Build the {@link EntityGraph} based on an provided {@link FetchGraph}
   *
   * @return the fully built {@link EntityGraph}
   */
  public static <T> EntityGrapher<T> from(
      FetchGraph fetchGraph, Class<T> rootClass, EntityManager em) {
    var graph = EntityGrapher.of(rootClass, em);
    apply(fetchGraph, null, graph);

    return graph;
  }

  private static void apply(
      FetchGraph fetchGraph, @Nullable String parentAttribute, EntityGrapher<?> grapher) {
    for (var entry : fetchGraph.children().entrySet()) {
      String attribute = entry.getKey();
      FetchGraph child = entry.getValue();

      grapher.fetch(attribute);

      if (!child.children().isEmpty()) {
        grapher.from(attribute);
        apply(child, attribute, grapher);
        if (parentAttribute != null) {
          grapher.from(parentAttribute);
        } else {
          grapher.root();
        }
      }
    }
  }
}
