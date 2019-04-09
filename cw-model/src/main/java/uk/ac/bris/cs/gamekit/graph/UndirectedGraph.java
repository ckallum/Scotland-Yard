package uk.ac.bris.cs.gamekit.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An undirected graph implementation
 *
 * @param <V> the type for {@link Node} values
 * @param <D> the type for {@link Edge} data
 */
public final class UndirectedGraph<V, D> extends AbstractGraph<V, D> implements Graph<V, D> , Serializable{

	private static final long serialVersionUID = 6932837778781276092L;
	private final Map<V, Node<V>> valueNodeMap = new LinkedHashMap<>();
	private final Map<Node<V>, List<Edge<V, D>>> sourceEdges = new HashMap<>();
	private final Map<Node<V>, List<Edge<V, D>>> destinationEdges = new HashMap<>();
	private final List<Edge<V, D>> edges = new ArrayList<>();

	/**
	 * Creates a new undirected graph
	 */
	public UndirectedGraph() {}

	/**
	 * Copy constructor
	 */
	public UndirectedGraph(Graph<V, D> other) {
		other.getNodes().forEach(this::addNode);
		other.getEdges().forEach(e -> appendEdge(getNode(e.source().value()), e));
	}

	@Override
	public void addNode(Node<V> node) {
		if (node == null) throw new NullPointerException("node == null");
		if (valueNodeMap.containsKey(node.value()))
			throw new IllegalArgumentException(node + " is already in the graph");
		valueNodeMap.put(node.value(), node);
		sourceEdges.put(node, new ArrayList<>());
		destinationEdges.put(node, new ArrayList<>());
	}

	/**
	 * Adds an edge to the node. This will add two edges, the given edge and the
	 * given edge with source and destination swapped.
	 *
	 * @param edge the edge to add, must not be null; it is an error to add
	 */
	@Override
	public void addEdge(Edge<V, D> edge) {
		if (edge == null) throw new NullPointerException("edge == null");
		Node<V> source = getNode(edge.source().value());
		if (source == null) throw new IllegalArgumentException(
				"source of edge(" + edge.source() + ") is not in the graph");
		Node<V> destination = getNode(edge.destination().value());
		if (destination == null) throw new IllegalArgumentException(
				"destination of edge(" + edge.destination() + ") is not in the graph");
		appendEdge(source, edge);
		appendEdge(destination, edge.swap());
	}

	private void appendEdge(Node<V> node, Edge<V, D> edge) {
		sourceEdges.get(node).add(edge);
		destinationEdges.get(node).add(edge);
		edges.add(edge);
	}

	@Override
	public Node<V> getNode(V value) {
		return valueNodeMap.get(value);
	}

	@Override
	public boolean containsNode(V value) {
		return valueNodeMap.containsKey(value);
	}

	@Override
	public List<Node<V>> getNodes() {
		return Collections.unmodifiableList(new ArrayList<>(valueNodeMap.values()));
	}

	@Override
	public Collection<Edge<V, D>> getEdges() {
		return Collections.unmodifiableList(edges);
	}

	@Override
	public Collection<Edge<V, D>> getEdgesFrom(Node<V> source) {
		return Collections.unmodifiableList(sourceEdges.get(source));
	}

	@Override
	public Collection<Edge<V, D>> getEdgesTo(Node<V> destination) {
		return Collections.unmodifiableList(destinationEdges.get(destination));
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return valueNodeMap.size();
	}

	@Override
	public String toString() {
		return "UndirectedGraph{" + "nodes=" + valueNodeMap.values() + ", edges=" + edges + '}';
	}
}
