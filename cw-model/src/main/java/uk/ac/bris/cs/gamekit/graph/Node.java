package uk.ac.bris.cs.gamekit.graph;

import java.io.Serializable;
import java.util.Objects;

/**
 * A node in a {@link Graph}
 * 
 * @param <V> the type of the value this node holds
 */
public final class Node<V> implements Serializable{

	private static final long serialVersionUID = 6923768108710951907L;
	private final V value;

	public Node(V value) {
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * @return the value this node holds, never null
	 */
	public V value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Node<?> node = (Node<?>) o;
		return Objects.equals(value, node.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return "Node(" + value + ")";
	}
}
