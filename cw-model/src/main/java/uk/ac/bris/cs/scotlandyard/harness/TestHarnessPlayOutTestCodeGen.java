package uk.ac.bris.cs.scotlandyard.harness;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.ImmutablePlayer;
import uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.TicketAbbr;
import uk.ac.bris.cs.scotlandyard.harness.CodeGenRecorder.CodeGen;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.DoubleMove;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.PassMove;
import uk.ac.bris.cs.scotlandyard.model.PlayerConfiguration;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Spectator;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.TicketMove;

import static java.lang.String.format;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
public class TestHarnessPlayOutTestCodeGen implements CodeGen {

	private final String harnessV = "harness";
	private final String gameV = "game";
	private final String roundsV = "rounds";
	private final String seedV = "seed";
	private final String spectatorV = "spectator";

	private final List<String> interactions = new ArrayList<>();

	@Override public String name() { return "PlayOutTestCodeGen"; }

	@Override public void movePicked(ImmutableScotlandYardView seed,
	                                 ScotlandYardView view, int location, Set<Move> moves,
	                                 Move picked) {
		ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);
		interactions.add(format("player(%s).makeMove()" +
						"\n\t.givenGameState(eq(%s))" +
						"\n\t.givenLocation(eq(%d))" +
						"\n\t.givenMoves(hasSize(%d))" +
						"\n\t.willPick(%s)",
				picked.colour(),
				mkView(seedV, seed, now),
				location,
				moves.size(),
				mkMove(picked)));
	}
	@Override public Spectator mkSpectator(ImmutableScotlandYardView seed) {
		return new Spectator() {
			@Override public void onMoveMade(ScotlandYardView view, Move move) {
				ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);
				interactions.add(format("spectator().onMoveMade()" +
								"\t\n.givenGameState(eq(%s))" +
								"\t\n.givenMove(eq(%s))",
						mkView(seedV, seed, now), mkMove(move)));
			}
			@Override public void onRoundStarted(ScotlandYardView view, int round) {
				ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);
				interactions.add(format("spectator().onRoundStarted()" +
								"\t\n.givenGameState(eq(%s))" +
								"\t\n.givenRound(eq(%d))",
						mkView(seedV, seed, now), round));
			}
			@Override public void onRotationComplete(ScotlandYardView view) {
				ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);
				interactions.add(format("spectator().onRotationComplete()" +
								"\t\n.givenGameState(eq(%s))" +
								"\t\n.respondWith(startRotate(%s))",
						mkView(seedV, seed, now), gameV));
			}
			@Override
			public void onGameOver(ScotlandYardView view, Set<Colour> winningPlayers) {
				ImmutableScotlandYardView now = ImmutableScotlandYardView.snapshot(view);
				interactions.add(format("spectator().onGameOver()" +
								"\t\n.givenGameState(eq(%s))" +
								"\t\n.givenWinners(eq(ImmutableSet.of(%s)))",
						mkView(seedV, seed, now),
						winningPlayers.stream()
								.map(Colour::name)
								.collect(joining(", "))));
			}
		};
	}


	@Override public String readOut(ImmutableScotlandYardView seed,
	                                ImmutableMap<Colour, PlayerConfiguration> configs,
	                                String graphMethod) {
		List<String> ls = new ArrayList<>();
		ls.add(format("TestHarness %s = new TestHarness();", harnessV));

		List<ImmutablePlayer> players = seed.players.stream()
				.map(v -> new ImmutablePlayer(v.colour, configs.get(v.colour).location, v.tickets))
				.collect(toList());

		ls.addAll(players.stream().map(p -> format("PlayerConfiguration %s = %s.newPlayer(\n%s);",
				p.colour.name().toLowerCase(), harnessV, mkPlayer(p))).collect(toList()));

		ls.add(format("List<Boolean> %s = Arrays.asList(\n%s);",
				roundsV, seed.getRounds().stream()
						.map(v -> v.toString() + (v ? "\n" : ""))
						.collect(joining(", "))));

		ls.add(format("ScotlandYardGame %s = createGame(%s, %s, \n%s);",
				gameV, roundsV, graphMethod, players.stream()
						.map(v -> v.colour.name().toLowerCase())
						.collect(joining(", "))));

		ls.add(format("Spectator %s = %s.createSpectator();", spectatorV, harnessV));
		ls.add(format("%s.registerSpectator(%s);", gameV, spectatorV));
		ls.add(format("ImmutableScotlandYardView %s = ImmutableScotlandYardView.snapshot(%s);",
				seedV, gameV));

		ls.add(format("%s.play(%s).startRotationAndAssertTheseInteractionsOccurInOrder(\n%s)" +
						"\n.thenAssertNoFurtherInteractions();",
				harnessV, gameV, interactions.stream().collect(joining(", \n"))));

		return ls.stream().collect(joining("\n"));
	}


	private static String mkTicket(Ticket t) {
		switch (t) {
			case TAXI:
				return "taxi";
			case BUS:
				return "bus";
			case UNDERGROUND:
				return "underground";
			case DOUBLE:
				return "x2";
			case SECRET:
				return "secret";
			default:
				throw new AssertionError();
		}
	}

	private static String mkMove(Move m) {
		String move;
		String colour = m.colour().name();
		if (m instanceof TicketMove) {
			TicketMove tm = (TicketMove) m;
			move = format("%s(%s, %d)", mkTicket(tm.ticket()), colour, tm.destination());
		} else if (m instanceof DoubleMove) {
			DoubleMove dm = (DoubleMove) m;
			move = format("%s(%s, %s(%d), %s(%d))", mkTicket(Ticket.DOUBLE), colour,
					mkTicket(dm.firstMove().ticket()), dm.firstMove().destination(),
					mkTicket(dm.secondMove().ticket()), dm.secondMove().destination());
		} else if (m instanceof PassMove) {
			move = format("pass(%s)", colour);
		} else throw new AssertionError();
		return move;
	}


	private static String mkPlayer(ImmutablePlayer p) {
		String tickets = p.tickets().entrySet()
				.stream()
				.sorted(Entry.comparingByKey(comparingInt(TicketAbbr::ordinal)))
				.flatMap(v -> Stream.of(v.getKey().toString(), v.getValue().toString()))
				.collect(joining(", "));
		return format("%s(%d).with(%s)",
				p.colour.name().toLowerCase(Locale.ENGLISH), p.location, tickets);
	}
	private static String mkView(String seed,
	                             ImmutableScotlandYardView prev,
	                             ImmutableScotlandYardView now) {
		StringBuilder delta = new StringBuilder(seed);
		if (prev.currentPlayer != now.currentPlayer)
			delta.append(format(".current(%s)", now.currentPlayer));
		if (prev.currentRound != now.currentRound)
			delta.append(format(".round(%s)", now.currentRound));
		if (prev.gameOver != now.gameOver)
			delta.append(format(".over(%s)", now.gameOver));
		if (!prev.winning.equals(now.winning))
			delta.append(format(".winning(%s)", now.winning.stream()
					.map(Colour::name)
					.collect(joining(", "))));
		delta.append(format(".players(\n\t//<editor-fold defaultstate=\"%s\"> \n", "collapsed"));
		delta.append(now.players.stream()
				.map(TestHarnessPlayOutTestCodeGen::mkPlayer)
				.collect(joining(", \n")));
		delta.append(")\n\t//</editor-fold>\n");
		return delta.toString();
	}


}
