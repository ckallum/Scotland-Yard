package uk.ac.bris.cs.gamekit.graph;

import java.util.Collection;
import java.util.List;

/**
 * A minimal graph data structure interface
 * 
 * @param <V> the type for {@link Node} values
 * @param <D> the type for {@link Edge} data
 */
public interface Graph<V, D> {

	/**
	 * Adds a {@link Node} to the graph
	 * 
	 * @param node the node to add, must not be null; it is an error to add the
	 *        same node more than once
	 */
	void addNode(Node<V> node);

	/**
	 * Adds an {@link Edge} to the graph, multiple edges with the same source
	 * and destination nodes are allowed.
	 * 
	 * @param edge the edge to add, must not be null; it is an error to add
	 *        edges with source or destination not in this graph
	 */
	void addEdge(Edge<V, D> edge);

	/**
	 * Retrieves the node associated with the given value, see
	 * {@link Node#value()}
	 * 
	 * @param value the value the node holds
	 * @return the node or null if no nodes are found to have the same value
	 */
	Node<V> getNode(V value);

	/**
	 * Checks whether a node with the given value exists
	 */
	boolean containsNode(V value);

	/**
	 * @return immutable list of all nodes in this graph in insertion order;
	 *         could be empty but never null
	 */
	List<Node<V>> getNodes();

	/**
	 * @return immutable set of all edges contained in this graph in no
	 *         particular order; could be empty but never null
	 */
	Collection<Edge<V, D>> getEdges();

	/**
	 * Finds all edges coming from the given source node
	 * 
	 * @param source the source node
	 * @return immutable set of all edges found in no particular order; could be
	 *         empty but never null
	 */
	Collection<Edge<V, D>> getEdgesFrom(Node<V> source);

	/**
	 * Finds all edges going to the given destination node
	 *
	 * @param destination the destination node
	 * @return immutable set of all edges found in no particular order; could be
	 *         empty but never null
	 */
	Collection<Edge<V, D>> getEdgesTo(Node<V> destination);

	/**
	 * Tests whether this graph has any nodes
	 * 
	 * @return true if empty
	 */
	boolean isEmpty();

	/**
	 * Retrieves the number of nodes in this graph
	 * 
	 * @return number of nodes
	 */
	int size();

}
