package uk.ac.bris.cs.scotlandyard.auxiliary;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import uk.ac.bris.cs.scotlandyard.auxiliary.AnonymousMoves.AnonymousDoubleMove;
import uk.ac.bris.cs.scotlandyard.auxiliary.AnonymousMoves.AnonymousPassMove;
import uk.ac.bris.cs.scotlandyard.auxiliary.AnonymousMoves.AnonymousTicketMove;
import uk.ac.bris.cs.scotlandyard.harness.Requirement;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.DoubleMove;
import uk.ac.bris.cs.scotlandyard.model.PassMove;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.PlayerConfiguration;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGame;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.TicketMove;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.fromAssertion;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLACK;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLUE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.GREEN;
import static uk.ac.bris.cs.scotlandyard.model.Colour.RED;
import static uk.ac.bris.cs.scotlandyard.model.Colour.WHITE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.YELLOW;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.BUS;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.DOUBLE;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.SECRET;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.TAXI;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.UNDERGROUND;

public class TestGames {
	/**
	 * Default reveal rounds
	 */
	public static final Set<Integer> DEFAULT_REVEAL = unmodifiableSet(
			new HashSet<>(asList(3, 8, 13, 18, 23)));
	/**
	 * Default detective locations
	 */
	public static final List<Integer> DETECTIVE_LOCATIONS = unmodifiableList(
			asList(26, 29, 50, 53, 91, 94, 103, 112, 117, 123, 138, 141, 155, 174));
	/**
	 * Default Mr.X locations
	 */
	public static final List<Integer> MRX_LOCATIONS = unmodifiableList(
			asList(35, 45, 51, 71, 78, 104, 106, 127, 132, 166, 170, 172));
	/**
	 * The default amount of tickets for a detective, which is:
	 * <p>
	 * TAXI = 11 <br>
	 * BUS = 8 <br>
	 * UNDERGROUND = 4 <br>
	 * DOUBLE = 0 <br>
	 * SECRET = 0 <br>
	 *
	 * @return a map with created using
	 * {@link #makeTickets(int, int, int, int, int)}; never null
	 */
	public static Map<Ticket, Integer> detectiveTickets() {
		return makeTickets(11, 8, 4, 0, 0);
	}
	/**
	 * The default amount of tickets for Mr.X, which is:
	 * <p>
	 * TAXI = 4 <br>
	 * BUS = 3 <br>
	 * UNDERGROUND = 3 <br>
	 * DOUBLE = 2 <br>
	 * SECRET = 5 <br>
	 *
	 * @return a map with created using
	 * {@link #makeTickets(int, int, int, int, int)}; never null
	 */
	public static Map<Ticket, Integer> mrXTickets() {
		return makeTickets(4, 3, 3, 2, 5);
	}
	/**
	 * No tickets (zero for all kinds ticket)
	 *
	 * @return a map with created using
	 * {@link #makeTickets(int, int, int, int, int)}; never null
	 */
	public static Map<Ticket, Integer> noTickets() {
		return makeTickets(0, 0, 0, 0, 0);
	}
	/**
	 * Create a map of tickets
	 *
	 * @param taxi amount of tickets for {@link Ticket#TAXI}
	 * @param bus amount of tickets for {@link Ticket#BUS}
	 * @param underground amount of tickets for {@link Ticket#UNDERGROUND}
	 * @param x2 amount of tickets for {@link Ticket#DOUBLE}
	 * @param secret amount of tickets for {@link Ticket#SECRET}
	 * @return a {@link Map} with ticket counts; never null
	 */
	public static Map<Ticket, Integer> makeTickets(int taxi, int bus, int underground, int x2,
	                                               int secret) {
		Map<Ticket, Integer> map = new HashMap<>();
		map.put(Ticket.TAXI, taxi);
		map.put(Ticket.BUS, bus);
		map.put(Ticket.UNDERGROUND, underground);
		map.put(Ticket.DOUBLE, x2);
		map.put(Ticket.SECRET, secret);
		return map;
	}

	/**
	 * Asserts that all given tickets are valid
	 *
	 * @param game the game to use
	 * @param colour the colour to assert
	 * @param taxi taxi ticket count
	 * @param bus bus ticket count
	 * @param underground underground ticket count
	 * @param x2 x2 ticket count
	 * @param secret secret ticket count
	 */
	public static void assertTicketCount(ScotlandYardView game, Colour colour,
	                                     int taxi,
	                                     int bus,
	                                     int underground,
	                                     int x2,
	                                     int secret) {
		ImmutableMap.of(
				TAXI, taxi,
				BUS, bus,
				UNDERGROUND, underground,
				DOUBLE, x2,
				SECRET, secret).forEach((ticket, count) ->
				assertThat(game.getPlayerTickets(colour, ticket))
						.as("Ticket count for %s did not match", ticket)
						.contains(count));
	}

	public static void assertTicketsForAllTicketTypeSuchThat(ScotlandYardGame game, Colour colour,
	                                                         Consumer<Optional<Integer>>
			                                                         requirement) {
		// XXX consider using Ticket.values
		assertThat(game.getPlayerTickets(colour, TAXI)).satisfies(requirement);
		assertThat(game.getPlayerTickets(colour, BUS)).satisfies(requirement);
		assertThat(game.getPlayerTickets(colour, UNDERGROUND)).satisfies(requirement);
		assertThat(game.getPlayerTickets(colour, DOUBLE)).satisfies(requirement);
		assertThat(game.getPlayerTickets(colour, SECRET)).satisfies(requirement);
	}

	/**
	 * Require that all given tickets are valid
	 *
	 * @param colour the colour to assert
	 * @param taxi taxi ticket count
	 * @param bus bus ticket count
	 * @param underground underground ticket count
	 * @param x2 x2 ticket count
	 * @param secret secret ticket count
	 */
	public static Requirement<ScotlandYardGame> ticketCountIs(Colour colour,
	                                                          int taxi,
	                                                          int bus,
	                                                          int underground,
	                                                          int x2,
	                                                          int secret) {
		return fromAssertion(format("%s player has: %d taxis, " +
						"%d buses, " +
						"%d undergrounds, " +
						"%d double moves," +
						" and %d secrets",
				colour, taxi, bus, underground, x2, secret),
				game -> assertTicketCount(game, colour, taxi, bus, underground, x2, secret));
	}

	/**
	 * Returns a list of rounds
	 *
	 * @param rounds the rounds
	 * @return a list; never null
	 */
	public static List<Boolean> rounds(Boolean... rounds) {
		return asList(requireNonNull(rounds));
	}
	public static List<Boolean> ofRounds(int rounds, Collection<Integer> reveal) {
		return IntStream.rangeClosed(1, rounds).mapToObj(reveal::contains).collect(toList());
	}
	/**
	 * Creates a new {@link TicketMove}
	 *
	 * @param colour the colour of the move
	 * @param ticket the ticket of the move
	 * @param destination the destination of move
	 * @return a new ticket move
	 */
	public static TicketMove ticket(Colour colour, Ticket ticket, int destination) {
		return new TicketMove(requireNonNull(colour), requireNonNull(ticket), destination);
	}
	/**
	 * Creates a new {@link DoubleMove}
	 *
	 * @param colour colour for the move
	 * @param first the first ticket
	 * @param firstDestination the first destination
	 * @param second the second ticket
	 * @param secondDestination the second destination
	 * @return a new double move
	 */
	public static DoubleMove x2(Colour colour, Ticket first, int firstDestination, Ticket second,
	                            int secondDestination) {
		return new DoubleMove(requireNonNull(colour), requireNonNull(first), firstDestination,
				requireNonNull(second), secondDestination);
	}

	public static DoubleMove x2(Colour colour, AnonymousTicketMove first,
	                            AnonymousTicketMove second) {
		return new DoubleMove(requireNonNull(colour),
				requireNonNull(first.colour(colour)),
				requireNonNull(second.colour(colour)));
	}
	/**
	 * Creates a new {@link AnonymousDoubleMove}
	 *
	 * @param first the first anonymous ticket move
	 * @param second the second anonymous ticket move
	 * @return a new anonymous double move
	 */
	public static AnonymousDoubleMove x2(AnonymousTicketMove first, AnonymousTicketMove second) {
		return new AnonymousDoubleMove(requireNonNull(first), requireNonNull(second));
	}
	/**
	 * Creates a new {@link TicketMove} of a taxi
	 *
	 * @param colour colour for the move
	 * @param destination the destination
	 * @return a new taxi ticket move
	 */
	public static TicketMove taxi(Colour colour, int destination) {
		return new TicketMove(requireNonNull(colour), TAXI, destination);
	}
	/**
	 * Creates a new {@link AnonymousTicketMove} of a secret
	 *
	 * @param destination the destination
	 * @return a new secret anonymous ticket move
	 */
	public static AnonymousTicketMove secret(int destination) {
		return new AnonymousTicketMove(SECRET, destination);
	}
	/**
	 * Creates a new {@link TicketMove} of a secret
	 *
	 * @param colour colour for the move
	 * @param destination the destination
	 * @return a new secret ticket move
	 */
	public static TicketMove secret(Colour colour, int destination) {
		return new TicketMove(requireNonNull(colour), SECRET, destination);
	}
	/**
	 * Creates a new {@link AnonymousTicketMove} of a taxi
	 *
	 * @param destination the destination
	 * @return a new taxi anonymous ticket move
	 */
	public static AnonymousTicketMove taxi(int destination) {
		return new AnonymousTicketMove(TAXI, destination);
	}
	/**
	 * Creates a new {@link TicketMove} of a bus
	 *
	 * @param colour colour for the move
	 * @param destination the destination
	 * @return a new bus ticket move
	 */
	public static TicketMove bus(Colour colour, int destination) {
		return new TicketMove(requireNonNull(colour), BUS, destination);
	}
	/**
	 * Creates a new {@link AnonymousTicketMove} of a bus
	 *
	 * @param destination the destination
	 * @return a new bus anonymous ticket move
	 */
	public static AnonymousTicketMove bus(int destination) {
		return new AnonymousTicketMove(BUS, destination);
	}
	/**
	 * Creates a new {@link TicketMove} of an underground
	 *
	 * @param colour colour for the move
	 * @param destination the destination
	 * @return a new underground ticket move
	 */
	public static TicketMove underground(Colour colour, int destination) {
		return new TicketMove(requireNonNull(colour), UNDERGROUND, destination);
	}
	/**
	 * Creates a new {@link AnonymousTicketMove} of an underground
	 *
	 * @param destination the destination
	 * @return a new underground anonymous ticket move
	 */
	public static AnonymousTicketMove underground(int destination) {
		return new AnonymousTicketMove(UNDERGROUND, destination);
	}
	/**
	 * Creates a new {@link PassMove}
	 *
	 * @param colour colour for the move
	 * @return a new pass move
	 */
	public static PassMove pass(Colour colour) {
		return new PassMove(colour);
	}
	/**
	 * Creates a new {@link AnonymousPassMove}
	 *
	 * @return a new anonymous pass move
	 */
	public static AnonymousPassMove pass() {
		return new AnonymousPassMove();
	}


	/**
	 * A player with with default tickets (for MrX, it calls {@link #mrXTickets()}, for
	 * detectives, it calls {@link #detectiveTickets()}) that does nothing when asked to make a
	 * move
	 *
	 * @param colour the colour for the created player
	 * @param location the location is will start at
	 * @return a fully configured with tickets, location, and does nothing when asked to make move
	 */
	public static PlayerConfiguration doNothingPlayer(Colour colour, int location) {
		return new PlayerConfiguration.Builder(colour)
				.at(location)
				.with(colour.isDetective() ? detectiveTickets() : mrXTickets())
				.using(dummyPlayer()).build();
	}

	/**
	 * A player that does nothing when asked to make a move
	 *
	 * @return the dummy player
	 */
	public static Player dummyPlayer() {
		return (view, location, moves, callback) -> {
			// do nothing
		};
	}

	/**
	 * Any given valid Mr.X configuration that does nothing
	 */
	public static PlayerConfiguration doNothingMrX() {
		return doNothingPlayer(BLACK, MRX_LOCATIONS.get(0));
	}
	/**
	 * Any given valid red configuration that does nothing
	 */
	public static PlayerConfiguration doNothingRed() {
		return doNothingPlayer(RED, DETECTIVE_LOCATIONS.get(0));
	}
	/**
	 * Any given valid green configuration that does nothing
	 */
	public static PlayerConfiguration doNothingGreen() {
		return doNothingPlayer(GREEN, DETECTIVE_LOCATIONS.get(1));
	}
	/**
	 * Any given valid blue configuration that does nothing
	 */
	public static PlayerConfiguration doNothingBlue() {
		return doNothingPlayer(BLUE, DETECTIVE_LOCATIONS.get(2));
	}
	/**
	 * Any given valid yellow configuration that does nothing
	 */
	public static PlayerConfiguration doNothingYellow() {
		return doNothingPlayer(YELLOW, DETECTIVE_LOCATIONS.get(3));
	}
	/**
	 * Any given valid white configuration that does nothing
	 */
	public static PlayerConfiguration doNothingWhite() {
		return doNothingPlayer(WHITE, DETECTIVE_LOCATIONS.get(4));
	}


}
