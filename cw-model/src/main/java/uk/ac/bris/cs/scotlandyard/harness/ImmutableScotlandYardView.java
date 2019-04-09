package uk.ac.bris.cs.scotlandyard.harness;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.ImmutableGraph;
import uk.ac.bris.cs.gamekit.graph.UndirectedGraph;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.DoubleMove;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.PassMove;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.Transport;

import static java.util.Comparator.comparingInt;
import static java.util.Objects.hash;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
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

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
public final class ImmutableScotlandYardView implements ScotlandYardView {

	public static final class ImmutablePlayer {
		final Colour colour;
		final int location;
		final ImmutableMap<Ticket, Integer> tickets;
		ImmutablePlayer(Colour colour, int location,
		                ImmutableMap<Ticket, Integer> tickets) {
			this.colour = colour;
			this.location = location;
			this.tickets = tickets;
		}
		//		ColourAbbr colour() { return ColourAbbr.from(colour); }
		ImmutableMap<TicketAbbr, Integer> tickets() {
			return tickets.entrySet().stream()
					.sorted(Entry.comparingByKey(comparingInt(v -> TicketAbbr.from(v).ordinal())))
					.collect(ImmutableMap.toImmutableMap(
							v -> TicketAbbr.from(v.getKey()),
							Entry::getValue));
		}
		@Override public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ImmutablePlayer player = (ImmutablePlayer) o;
			return location == player.location &&
					Objects.equals(tickets, player.tickets);
		}
		@Override public int hashCode() { return hash(location, tickets); }
		@Override public String toString() {
			return String.format("%s@%d{%s}", colour, location,
					tickets().entrySet().stream().map(Entry::toString).collect(joining(",")));
		}
	}

	private final ImmutableList<Colour> colours;
	private final ImmutableList<Boolean> rounds;
	private final ImmutableGraph<Integer, Transport> graph;

	final ImmutableList<ImmutablePlayer> players;
	final ImmutableSet<Colour> winning;
	final boolean gameOver;
	final Colour currentPlayer;
	final int currentRound;

	public static ImmutableScotlandYardView snapshot(ScotlandYardView view) {
		return new ImmutableScotlandYardView(view);
	}

	private ImmutableScotlandYardView(
			ImmutableList<Colour> colours,
			ImmutableList<Boolean> rounds,
			ImmutableGraph<Integer, Transport> graph,
			ImmutableList<ImmutablePlayer> players,
			ImmutableSet<Colour> winning, boolean gameOver,
			Colour currentPlayer, int currentRound) {
		this.colours = colours;
		this.rounds = rounds;
		this.graph = graph;
		this.players = players;
		this.winning = winning;
		this.gameOver = gameOver;
		this.currentPlayer = currentPlayer;
		this.currentRound = currentRound;
	}
	private ImmutableScotlandYardView(ScotlandYardView view) {
//		Thread.dumpStack();
		players = view.getPlayers()
				.stream()
				.map(p -> {
					Integer location = view.getPlayerLocation(p).orElseThrow(AssertionError::new);
					ImmutableMap<Ticket, Integer> tickets = Stream.of(Ticket.values())
							.collect(ImmutableMap.toImmutableMap(identity(), t -> view
									.getPlayerTickets(p, t)
									.orElseThrow(AssertionError::new)));
					return new ImmutablePlayer(p, location, tickets);
				}).collect(ImmutableList.toImmutableList());
		colours = ImmutableList.copyOf(view.getPlayers());
		winning = ImmutableSet.copyOf(view.getWinningPlayers());
		gameOver = view.isGameOver();
		currentRound = view.getCurrentRound();
		currentPlayer = view.getCurrentPlayer();
		rounds = ImmutableList.copyOf((view.getRounds()));
		graph = new ImmutableGraph<>(new UndirectedGraph<>(view.getGraph()));
	}

	@Override public List<Colour> getPlayers() { return colours; }
	@Override public Set<Colour> getWinningPlayers() { return winning; }
	@Override public Optional<Integer> getPlayerLocation(Colour colour) {
		return players.stream()
				.filter(p -> p.colour == colour)
				.map(p -> p.location).findFirst();
	}
	@Override public Optional<Integer> getPlayerTickets(Colour colour, Ticket ticket) {
		return players.stream()
				.filter(p -> p.colour == colour)
				.map(p -> p.tickets.get(ticket)).findFirst();
	}
	@Override public boolean isGameOver() { return gameOver; }
	@Override public Colour getCurrentPlayer() { return currentPlayer; }
	@Override public int getCurrentRound() { return currentRound; }
	@Override public List<Boolean> getRounds() { return rounds; }
	@Override public Graph<Integer, Transport> getGraph() { return graph; }


	private void checkPlayer(Colour colour) {
		if (players.stream().noneMatch(c -> c.colour == colour))
			throw new IllegalArgumentException("Player " + colour +
					" is not part of " + players);
	}

	public ImmutableScotlandYardView players(ImmutablePlayer... players) {
		ImmutableList<ImmutablePlayer> mapped = ImmutableList.copyOf(players);
		mapped.forEach(p -> checkPlayer(p.colour));
		return new ImmutableScotlandYardView(colours, rounds, graph,
				mapped, winning, gameOver, currentPlayer, currentRound);
	}
	//	public ImmutableSet<Colour> winning() {
//		return winning.stream().map(ColourAbbr::from)
//				.collect(ImmutableSet.toImmutableSet());
//	}
//	public ImmutableScotlandYardView winning(Colour... winning) {
//		return winning(Stream.of(winning).map(c -> c.actual).toArray(Colour[]::new));
//	}
	public ImmutableScotlandYardView winning(Colour... winning) {
		ImmutableSet<Colour> cs = ImmutableSet.copyOf(winning);
		if (cs.size() != winning.length)
			throw new IllegalArgumentException("Duplicate colours in " + Arrays.toString(winning));
		cs.forEach(this::checkPlayer);
		return new ImmutableScotlandYardView(this.colours, rounds, graph,
				players, cs, gameOver, currentPlayer, currentRound);
	}
	public ImmutableScotlandYardView over(boolean over) {
		return new ImmutableScotlandYardView(colours, rounds, graph,
				players, winning, over, currentPlayer, currentRound);
	}
	public ImmutableScotlandYardView current(Colour colour) {
		checkPlayer(colour);
		return new ImmutableScotlandYardView(colours, rounds, graph,
				players, winning, gameOver, colour, currentRound);
	}
	public ImmutableScotlandYardView round(int round) {
		if (round - 1 > rounds.size())
			throw new IllegalArgumentException(round + " > total round of " + rounds.size());
		return new ImmutableScotlandYardView(colours, rounds, graph,
				players, winning, gameOver, currentPlayer, round);
	}

	public enum TicketAbbr {
		TX(TAXI), BS(BUS), UG(UNDERGROUND), X2(DOUBLE), SC(SECRET);
		final Ticket actual;
		TicketAbbr(Ticket actual) {this.actual = actual;}

		private final static ImmutableMap<Ticket, TicketAbbr> map = Stream.of(TicketAbbr.values())
				.collect(ImmutableMap.toImmutableMap(v -> v.actual, identity()));
		static TicketAbbr from(Ticket ticket) { return map.get(ticket); }
	}


	public static With player(Colour colour, int location) {
		return (a, _a, b, _b, c, _c, d, _d, e, _e) -> new ImmutablePlayer(colour, location,
				ImmutableMap.of(a.actual, _a,
						b.actual, _b,
						c.actual, _c,
						d.actual, _d,
						e.actual, _e));
	}

	public static With black(int location) { return player(BLACK, location); }
	public static With red(int location) { return player(RED, location); }
	public static With green(int location) { return player(GREEN, location); }
	public static With blue(int location) { return player(BLUE, location); }
	public static With white(int location) { return player(WHITE, location); }
	public static With yellow(int location) { return player(YELLOW, location); }

	public interface With {
		ImmutablePlayer with(TicketAbbr a, int _a, TicketAbbr b, int _b, TicketAbbr c, int _c,
		                     TicketAbbr d, int _d, TicketAbbr e, int _e);
	}


	static <T extends Enum<T>> List<T> fixedOrder(Set<T> ts) {
		return ts.stream().sorted(Comparator.comparingInt(Enum<T>::ordinal)).collect(toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableScotlandYardView that = (ImmutableScotlandYardView) o;
		return gameOver == that.gameOver &&
				currentRound == that.currentRound &&
				Objects.equals(players, that.players) &&
				Objects.equals(winning, that.winning) &&
				currentPlayer == that.currentPlayer &&
				Objects.equals(rounds, that.rounds) &&
				Objects.equals(graph, that.graph);
	}

	@Override
	public int hashCode() {
		return hash(players, winning, gameOver, currentPlayer, currentRound, rounds, graph);
	}

	@Override
	public String toString() {
		return String.format("ScotlandYardView{" +
						"round=%s, current=%s, over=%s, winning=%s, players=%s}",
				currentRound,
				currentPlayer,
				gameOver,
				fixedOrder(winning),
				players);
	}
}
