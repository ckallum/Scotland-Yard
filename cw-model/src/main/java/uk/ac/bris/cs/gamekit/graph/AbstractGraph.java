package uk.ac.bris.cs.gamekit.graph;

import java.util.Objects;

public abstract class AbstractGraph<V, D> implements Graph<V, D> {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Graph)) return false;
		Graph<?, ?> that = (Graph<?, ?>) o;
		return Objects.equals(getNodes(), that.getNodes()) &&
				Objects.equals(getEdges(), that.getEdges());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getNodes(), getEdges());
	}

}
