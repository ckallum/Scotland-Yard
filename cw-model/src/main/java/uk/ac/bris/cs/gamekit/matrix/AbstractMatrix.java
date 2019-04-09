package uk.ac.bris.cs.gamekit.matrix;

import java.util.Objects;

public abstract class AbstractMatrix<T> implements Matrix<T> {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Matrix)) return false;
		Matrix<?> that = (Matrix<?>) o;
		if (this.count() != that.count() ||
				this.columnSize() != that.columnSize() ||
				this.rowSize() != that.rowSize())
			return false;
		return Objects.equals(this.asList(), that.asList());
	}

	@Override
	public int hashCode() {
		return Objects.hash(asList());
	}

}
