package uk.ac.bris.cs.gamekit.matrix;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public final class ImmutableMatrix<T> extends AbstractMatrix<T> implements Serializable {

	private static final long serialVersionUID = -5993100663042974328L;
	private final Matrix<T> matrix;

	public ImmutableMatrix(Matrix<T> matrix) {
		this.matrix = Objects.requireNonNull(matrix);
	}

	@Override
	public T get(int row, int column) {
		return matrix.get(row, column);
	}

	@Override
	public void put(int row, int column, T cell) {
		throw new UnsupportedOperationException("put is not supported in ImmutableMatrix");
	}

	@Override
	public boolean inBounds(int row, int column) {
		return matrix.inBounds(row, column);
	}

	@Override
	public List<T> row(int row) {
		return matrix.row(row);
	}

	@Override
	public void row(int row, List<T> values) {
		throw new UnsupportedOperationException("row is not supported in ImmutableMatrix");
	}

	@Override
	public List<T> column(int column) {
		return matrix.column(column);
	}

	@Override
	public void column(int column, List<T> values) {
		throw new UnsupportedOperationException("column is not supported in ImmutableMatrix");
	}

	@Override
	public List<T> mainDiagonal() {
		return matrix.mainDiagonal();
	}

	@Override
	public List<T> antiDiagonal() {
		return matrix.antiDiagonal();
	}

	@Override
	public List<T> asList() {
		return matrix.asList();
	}

	@Override
	public int count() {
		return matrix.count();
	}

	@Override
	public int columnSize() {
		return matrix.columnSize();
	}

	@Override
	public int rowSize() {
		return matrix.rowSize();
	}

	@Override
	public String toString() {
		return String.format("ImmutableMatrix{\n%s\n}", matrix);
	}

}
