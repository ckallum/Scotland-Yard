package uk.ac.bris.cs.scotlandyard.model;

import java.util.Objects;

/**
 * Represents a double move in the Scotland Yard game
 */
public class DoubleMove extends Move {

	private static final long serialVersionUID = 8857602351332595005L;
	private final TicketMove firstMove;
	private final TicketMove secondMove;

	/**
	 * Create a new double move from two individual ticket moves
	 *
	 * @param player the colour of the player playing this move
	 * @param firstMove the first ticket move
	 * @param secondMove the second ticket move
	 */
	public DoubleMove(Colour player, TicketMove firstMove, TicketMove secondMove) {
		super(player);
		this.firstMove = firstMove;
		this.secondMove = secondMove;
	}

	/**
	 * Create a new double move from destinations and tickets
	 *
	 * @param player the colour of the player
	 * @param first the first ticket of the move
	 * @param firstDestination the first destination of the move
	 * @param second the second ticket of the move
	 * @param secondDestination the second destination of the move
	 */
	public DoubleMove(Colour player, Ticket first, int firstDestination, Ticket second,
			int secondDestination) {
		super(player);
		this.firstMove = new TicketMove(player, first, firstDestination);
		this.secondMove = new TicketMove(player, second, secondDestination);
	}

	/**
	 * @return the first ticket move
	 */
	public TicketMove firstMove() {
		return firstMove;
	}

	/**
	 * @return the second ticket move
	 */
	public TicketMove secondMove() {
		return secondMove;
	}

	/**
	 * @return the final destination, equivalent to
	 *         {@code secondMove().destination()}
	 */
	public int finalDestination() {
		return secondMove.destination();
	}

	/**
	 * @return true if the first ticket and the second ticket is the same
	 */
	public boolean hasSameTicket() {
		return firstMove.ticket() == secondMove.ticket();
	}

	@Override
	public void visit(MoveVisitor visitor) {
		Objects.requireNonNull(visitor).visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		DoubleMove that = (DoubleMove) o;
		return Objects.equals(firstMove, that.firstMove)
				&& Objects.equals(secondMove, that.secondMove);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), firstMove, secondMove);
	}

	@Override
	public String toString() {
		return "Double[" + colour() + "-(" + firstMove.ticket() + ")->" + firstMove.destination()
				+ "-(" + secondMove.ticket() + ")->" + secondMove.destination() + "]";
	}

}
