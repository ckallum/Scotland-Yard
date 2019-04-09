package uk.ac.bris.cs.scotlandyard.model;

import java.util.Objects;

/**
 * Ticket types for the Scotland Yard game
 */
public enum Ticket {
	TAXI, BUS, UNDERGROUND, DOUBLE, SECRET;

	/**
	 * Finds the ticket for a given transport type
	 *
	 * @param transport the given transport, not null
	 * @return the ticket matching the transport type; never null
	 */
	public static Ticket fromTransport(Transport transport) {
		switch (Objects.requireNonNull(transport)) {
			case TAXI:
				return TAXI;
			case BUS:
				return BUS;
			case UNDERGROUND:
				return UNDERGROUND;
			case FERRY:
				return SECRET;
			default:
				return TAXI;
		}
	}

}
