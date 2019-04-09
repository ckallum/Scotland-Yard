package uk.ac.bris.cs.scotlandyard.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

/**
 * Simple value object that stores initial configurations of a player.
 */
public class PlayerConfiguration {

	/**
	 * The colour of the player
	 */
	public final Colour colour;

	/**
	 * The actual player implementation
	 */
	public final Player player;

	/**
	 * Tickets for the player, it is an error to mutate the content of this map
	 */
	public final Map<Ticket, Integer> tickets;

	/**
	 * Initial location of the player
	 */
	public final int location;

	private PlayerConfiguration(Colour colour, Player player, Map<Ticket, Integer> tickets,
			int location) {
		this.colour = colour;
		this.player = player;
		this.tickets = tickets;
		this.location = location;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PlayerConfiguration{");
		sb.append("colour=").append(colour);
		sb.append(", player=").append(player);
		sb.append(", tickets=").append(tickets);
		sb.append(", location=").append(location);
		sb.append('}');
		return sb.toString();
	}

	/**
	 * A builder for {@link PlayerConfiguration}
	 */
	public static class Builder {
		private final Colour colour;
		private Player player;
		private Map<Ticket, Integer> tickets;
		private int location;

		/**
		 * Creates a builder with the given colour
		 *
		 * @param colour see {@link PlayerConfiguration#colour}; not null
		 */
		public Builder(Colour colour) {
			this.colour = Objects.requireNonNull(colour);
		}

		/**
		 * Sets the player of the configuration
		 *
		 * @param player see {@link PlayerConfiguration#player}; not null
		 * @return the builder for chaining; never null
		 */
		public Builder using(Player player) {
			this.player = Objects.requireNonNull(player);
			return this;
		}

		/**
		 * Sets the ticket of the configuration
		 *
		 * @param tickets see {@link PlayerConfiguration#tickets}; not null
		 * @return the builder for chaining; never null
		 */
		public Builder with(Map<Ticket, Integer> tickets) {
			this.tickets =ImmutableMap.copyOf(tickets);
			return this;
		}

		/**
		 * Sets the location of the configuration
		 *
		 * @param location see {@link PlayerConfiguration#location}
		 * @return the builder for chaining; never null
		 */
		public Builder at(int location) {
			this.location = location;
			return this;
		}

		/**
		 * Constructs the {@link PlayerConfiguration} based on the called
		 * builder methods
		 *
		 * @return the configuration; never null
		 */
		public PlayerConfiguration build() {
			return new PlayerConfiguration(colour, Objects.requireNonNull(player),
					Objects.requireNonNull(tickets), location);
		}

	}

}
