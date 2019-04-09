package uk.ac.bris.cs.scotlandyard.model;

/**
 * Visitor for classes extending {@link Move}
 */
public interface MoveVisitor {

	/**
	 * Called when visiting a pass move
	 *
	 * @param move the move; never null
	 */
	default void visit(PassMove move) {}

	/**
	 * Called when visiting a ticket move
	 *
	 * @param move the move; never null
	 */
	default void visit(TicketMove move) {}

	/**
	 * Called when visiting a double move
	 *
	 * @param move the move; never null
	 */
	default void visit(DoubleMove move) {}

}
