package uk.ac.bris.cs.gamekit.matrix;

import java.util.List;

/**
 * A minimal matrix structure interface
 * 
 * @param <T> the type of elements in the matrix
 */
public interface Matrix<T> {
	/**
	 * Get the element at the specified location
	 *
	 * @param row the row of the element
	 * @param column the column of the element
	 * @return the element; could be null
	 */
	T get(int row, int column);

	/**
	 * Set specified location to element
	 *
	 * @param row the row of the element
	 * @param column the column of the element
	 * @param cell the element; may be null
	 */
	void put(int row, int column, T cell);

	/**
	 * Tests whether the specified location is within bounds of the matrix
	 *
	 * @param row the row to test
	 * @param column the column to test
	 * @return true if the given location is within bounds
	 */
	boolean inBounds(int row, int column);

	/**
	 * Get the specified row as a list
	 *
	 * @param row the row
	 * @return a immutable list; never null
	 */
	List<T> row(int row);

	/**
	 * Set the entire row with the given list
	 *
	 * @param row the row to set
	 * @param values the list of elements; not null
	 */
	void row(int row, List<T> values);

	/**
	 * Get the specified column as a list
	 *
	 * @param column the row
	 * @return a immutable list; never null
	 */
	List<T> column(int column);

	/**
	 * Set the entire column with the given list
	 *
	 * @param column the row to set
	 * @param values the list of elements; not null
	 */
	void column(int column, List<T> values);

	/**
	 * Get the main diagonal elements of the matrix
	 *
	 * @return an immutable list; never null
	 */
	List<T> mainDiagonal();

	/**
	 * Get the antidiagonal elements of the matrix
	 *
	 * @return an immutable list; never null
	 */
	List<T> antiDiagonal();

	/**
	 * Flatten the matrix to a list
	 *
	 * @return an immutable list with {@code size * size} elements; never null
	 */
	List<T> asList();

	/**
	 * Queries the total element count of the matrix
	 * 
	 * @return the element count of the matrix. A n*m matrix has a count of n*m
	 */
	default int count() {
		return columnSize() * rowSize();
	}

	/**
	 * Queries the column size
	 * 
	 * @return the column size of the matrix
	 */
	int columnSize();

	/**
	 * Queries the row size
	 * 
	 * @return the row size of the matrix
	 */
	int rowSize();
}
