package uk.ac.bris.cs.gamekit.graph;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An immutable graph that does not allow adding new nodes or edges
 *
 * @param <V> the type for {@link Node} values
 * @param <D> the type for {@link Edge} data
 */
public final class ImmutableGraph<V, D> extends AbstractGraph<V, D>
		implements
		Graph<V, D>,
		Serializable {

	private static final long serialVersionUID = -54523690732483278L;
	private final Graph<V, D> graph;

	public ImmutableGraph(Graph<V, D> graph) {
		this.graph = Objects.requireNonNull(graph);
	}

	@Override
	public void addNode(Node<V> node) {
		throw new UnsupportedOperationException(
				"Adding node is not supported in an ImmutableGraph");
	}

	@Override
	public void addEdge(Edge<V, D> edge) {
		throw new UnsupportedOperationException(
				"Adding edge is not supported in an ImmutableGraph");
	}

	@Override
	public Node<V> getNode(V value) {
		return graph.getNode(value);
	}

	@Override
	public boolean containsNode(V value) {
		return graph.containsNode(value);
	}

	@Override
	public List<Node<V>> getNodes() {
		return graph == null ? Collections.emptyList() : graph.getNodes();
	}

	@Override
	public Collection<Edge<V, D>> getEdges() {
		return graph.getEdges();
	}

	@Override
	public Collection<Edge<V, D>> getEdgesFrom(Node<V> source) {
		return graph.getEdgesFrom(source);
	}

	@Override
	public Collection<Edge<V, D>> getEdgesTo(Node<V> destination) {
		return graph.getEdgesTo(destination);
	}

	@Override
	public boolean isEmpty() {
		return graph.isEmpty();
	}

	@Override
	public int size() {
		return graph.size();
	}

	@Override
	public String toString() {
		return "ImmutableGraph{" + graph + '}';
	}

}
