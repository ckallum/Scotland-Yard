package uk.ac.bris.cs.scotlandyard.model;

import java.util.Objects;

/**
 * Represents a ticket move in the Scotland Yard game
 */
public class TicketMove extends Move {

	private static final long serialVersionUID = -8579140322766860934L;
	private final Ticket ticket;
	private final int destination;

	/**
	 * Create a new ticket move with ticket and destination
	 *
	 * @param colour the colour of the player playing this move
	 * @param ticket the ticket for this move
	 * @param destination the destination for this move
	 */
	public TicketMove(Colour colour, Ticket ticket, int destination) {
		super(colour);
		this.destination = destination;
		this.ticket = ticket;
	}

	/**
	 * @return the ticket used for this move
	 */
	public Ticket ticket() {
		return ticket;
	}

	/**
	 * @return the destination of the move
	 */
	public int destination() {
		return destination;
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
		TicketMove that = (TicketMove) o;
		return destination == that.destination && ticket == that.ticket;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), ticket, destination);
	}

	@Override
	public String toString() {
		return "Ticket[" + super.toString() + "-(" + this.ticket + ")->" + this.destination + "]";
	}

}
