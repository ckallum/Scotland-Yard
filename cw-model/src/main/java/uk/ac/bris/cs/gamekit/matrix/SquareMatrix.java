package uk.ac.bris.cs.gamekit.matrix;

import static java.util.Collections.nCopies;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public final class SquareMatrix<T> extends AbstractMatrix<T> implements Serializable {

	private static final long serialVersionUID = 3361784939506993018L;
	private final List<List<T>> cells;

	/**
	 * Creates a new {@code size*size} square matrix with default values. Is is
	 * recommended that the default value supplied be immutable as all cells
	 * will be referencing to the same instance
	 *
	 * @param size size of the size*size grid, must be &gt; 0
	 * @param defaultValue initial value for all cells, can be null
	 */
	public SquareMatrix(int size, T defaultValue) {
		if (size < 1) throw new IllegalArgumentException("Size must me > 0, got " + size);
		this.cells = IntStream.range(0, size)
				.mapToObj(x -> new ArrayList<>(nCopies(size, defaultValue)))
				.collect(toCollection(ArrayList::new));
	}

	/**
	 * Copy constructor
	 *
	 * @param squareMatrix the matrix to copy from
	 */
	public SquareMatrix(SquareMatrix<T> squareMatrix) {
		this.cells = squareMatrix.cells.stream().map(ArrayList::new)
				.collect(toCollection(ArrayList::new));
	}

	@Override
	public T get(int row, int column) {
		checkBound("row", row);
		checkBound("column", column);
		return cells.get(row).get(column);
	}

	@Override
	public void put(int row, int column, T cell) {
		checkBound("row", row);
		checkBound("column", column);
		cells.get(row).set(column, cell);
	}

	@Override
	public boolean inBounds(int row, int column) {
		return (row >= 0 && row < size()) && (column >= 0 && column < size());
	}

	@Override
	public List<T> row(int row) {
		checkBound("row", row);
		return unmodifiableList(cells.get(row));
	}

	@Override
	public void row(int row, List<T> values) {
		Objects.requireNonNull(values);
		checkBound("row", row);
		if (size() != values.size()) throw new IllegalArgumentException("size != value.size()");
		cells.set(row, values);
	}

	@Override
	public List<T> column(int column) {
		checkBound("column", column);
		return unmodifiableList(cells.stream().map(c -> c.get(column)).collect(toList()));
	}

	@Override
	public void column(int column, List<T> values) {
		Objects.requireNonNull(values);
		checkBound("column", column);
		if (size() != values.size()) throw new IllegalArgumentException("size != value.size()");
		for (int i = 0; i < values.size(); i++)
			cells.get(i).set(column, values.get(i));
	}

	@Override
	public List<T> mainDiagonal() {
		return unmodifiableList(
				IntStream.range(0, size()).mapToObj(i -> get(i, i)).collect(toList()));
	}

	@Override
	public List<T> antiDiagonal() {
		int size = size();
		return unmodifiableList(
				IntStream.range(0, size()).mapToObj(i -> get(i, size - 1 - i)).collect(toList()));
	}

	@Override
	public List<T> asList() {
		return cells.stream().flatMap(List::stream).collect(toList());
	}

	@Override
	public int columnSize() {
		return size();
	}

	@Override
	public int rowSize() {
		return size();
	}

	private int size() {
		return cells.size();
	}

	private void checkBound(String name, int value) {
		if (value < 0) throw new IndexOutOfBoundsException(name + " " + value + " < 0");
		if (value >= size())
			throw new IndexOutOfBoundsException(name + " " + value + " > " + (size() - 1));
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return cells.stream().map(l -> l.stream().map(Object::toString).collect(joining(", ")))
				.collect(joining("\n", "[", "]"));
	}
}
