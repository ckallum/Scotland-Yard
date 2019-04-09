package uk.ac.bris.cs.scotlandyard.harness;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import uk.ac.bris.cs.scotlandyard.harness.Captures.Player.MakeMove;
import uk.ac.bris.cs.scotlandyard.harness.Captures.Spectator.GameOver;
import uk.ac.bris.cs.scotlandyard.harness.Captures.Spectator.MoveMade;
import uk.ac.bris.cs.scotlandyard.harness.Captures.Spectator.RotationComplete;
import uk.ac.bris.cs.scotlandyard.harness.Captures.Spectator.RoundStarted;
import uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.ImmutablePlayer;
import uk.ac.bris.cs.scotlandyard.harness.PlayerInteractions.MakeMoveInteraction;
import uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.GameOverInteraction;
import uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.MoveMadeInteraction;
import uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.RotationCompleteInteraction;
import uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.RoundStartedInteraction;
import uk.ac.bris.cs.scotlandyard.harness.TestHarness.Setups.Assert;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.PlayerConfiguration;
import uk.ac.bris.cs.scotlandyard.model.PlayerConfiguration.Builder;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGame;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Spectator;
import uk.ac.bris.cs.scotlandyard.model.Ticket;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.fail;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.DETECTIVE_LOCATIONS;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.MRX_LOCATIONS;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.detectiveTickets;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.mrXTickets;
import static uk.ac.bris.cs.scotlandyard.harness.Assertions.assertFailure;
import static uk.ac.bris.cs.scotlandyard.harness.Interactions.assertEach;
import static uk.ac.bris.cs.scotlandyard.harness.PlayerInteractions.mkPlayerKey;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.fromAssertion;
import static uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.mkSpectatorKey;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_GAME_OVER;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_MOVE_MADE;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_ROTATION_COMPLETE;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_ROUND_STARTED;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.BUS;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.DOUBLE;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.SECRET;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.TAXI;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.UNDERGROUND;

/**
 * A <a href="https://en.wikipedia.org/wiki/Domain-specific_language">DSL</a> based test harness
 * for testing {@link ScotlandYardGame} and all it's side effects.
 * <p>
 * To print interactions to System.out, pass the system property printInteractions=true<br>
 * To change the default number of runs of each test from 10, pass the system property captureN,
 * e.g. captureN=5<br>
 * For example: <code>-DprintInteractions=true</code>
 */
public class TestHarness {


	enum EndMode {NO_MORE, IGNORE}

	private final int captureN;
	private final String callingClass;
	private final Queue<Interaction<?>> interactions = new ArrayDeque<>();
	private boolean printInteractions;
	private int totalInteractions = 0;
	private int interactionsProcessed = 0;
	private ScotlandYardGame game;
	private EndMode endMode = EndMode.IGNORE;
	private List<Requirement<ScotlandYardGame>> finalReqs;
	private Consumer<ScotlandYardGame> finalContinuation;
	private final CodeGenRecorder recorder =
			new CodeGenRecorder(ImmutableList.of(new GameModelSequencePUMLCodeGen()));

	private static boolean getBooleanFlag(String flagName) {
		return Optional.ofNullable(System.getProperty(flagName))
				.flatMap(flag -> {
					try {
						return Optional.of(Boolean.parseBoolean(flag));
					} catch (Exception e) {
						return Optional.empty();
					}
				}).orElse(false);
	}

	private static int getIntFlag(String flagName, int fallback) {
		return Optional.ofNullable(System.getProperty(flagName))
				.flatMap(flag -> {
					try {
						return Optional.of(Integer.parseInt(flag));
					} catch (Exception e) {
						return Optional.empty();
					}
				}).orElse(fallback);
	}

	public TestHarness() {
		this(getBooleanFlag("printInteractions"));
	}
	public TestHarness(boolean printInteractions) {
		this(getIntFlag("captureN", 10), printInteractions);
	}
	public TestHarness(int captureN) {
		this(captureN, getBooleanFlag("printInteractions"));
	}
	public TestHarness(int captureN, boolean printInteractions) {
		Assertions.disableSystemExit();
		this.captureN = captureN;
		this.printInteractions = printInteractions;
		this.callingClass = Arrays.stream(Thread.currentThread().getStackTrace())
				.skip(1)
				.map(StackTraceElement::getClassName)
				.filter(s -> !s.equals(getClass().getName()))
				.findFirst().orElse(getClass().getName());
	}

	public void forcePrintInteraction() {
		printInteractions = true;
	}

	public interface Interaction<C> {
		Comparable<?> key();
		void applyAssertion(C capture, AssertionContext ctx);
		default void executeAction(C capture, String callingClass) {
			// no-op
		}
		String describe();
		List<StackTraceElement> origin();

		static String describeMethod(String methodName, ImmutableMap<String, Collection<?>> kvs) {
			return format("%s(\n%s\n)", methodName, kvs.entrySet().stream()
					.map(e -> e.getValue().isEmpty() ?
							new SimpleImmutableEntry<>(e.getKey(), ImmutableList.of("can be " +
									"anything")) : e)
					.map(e -> format("\t%s %s", e.getKey(), e.getValue().stream()
							.map(Object::toString)
							.collect(joining(","))))
					.collect(joining(",\n")));

		}
	}


	public Assert play(ScotlandYardGame game) {
		this.game = game;
		if (printInteractions) recorder.snap(game);
		return new TestAssert();
	}

	private class TestAssert implements Assert {
		private TestAssert previousRound;
		private Interaction<?>[] interactions;
		private List<Requirement<ScotlandYardGame>> reqs = new ArrayList<>();
		Consumer<ScotlandYardGame> continuation = game -> Assertions.enableSystemExit();

		private TestAssert(Interaction<?>... interactions) {
			this(null, interactions);
		}
		private TestAssert(TestAssert previousRound, Interaction<?>... interactions) {
			this.previousRound = previousRound;
			this.interactions = interactions;
			totalInteractions += interactions.length;
		}
		@Override
		public Assert startRotationAndAssertTheseInteractionsOccurInOrder(
				Interaction<?>... interactions) {
			return new TestAssert(this, interactions);
		}
		@Override public final Assert thenRequire(
				Requirement<ScotlandYardGame> requirement) {
			reqs.add(requirement);
			return this;
		}
		@Override public void thenIgnoreAnyFurtherInteractions() {
			endMode = EndMode.IGNORE;
			runInteractions();
		}
		@Override public void thenAssertNoFurtherInteractions() {
			endMode = EndMode.NO_MORE;
			runInteractions();
		}
		@Override public void shouldThrow(Class<? extends Throwable> clazz) {
			try {
				this.runInteractions();
			} catch (Throwable e) {
				if (!clazz.isInstance(e)) {
					StringWriter logger = new StringWriter();
					e.printStackTrace(new PrintWriter(logger));
					assertFailure(
							String.format("Wrong exception thrown: %s expected but found:\n%s\n%s",
									clazz.getSimpleName(), e.toString(), logger.toString()),
							Assertions.capture(),
							callingClass);
				}
				return;
			}
			assertFailure(
					String.format("%s was expected but not thrown", clazz.getSimpleName()),
					Assertions.capture(),
					callingClass);
		}

		private void runInteractions() {
			if (previousRound != null) {
				previousRound.continuation = game -> {
					finalContinuation = continuation;
					interactionsProcessed += interactions.length;
					TestHarness.this.interactions.clear();
					TestHarness.this.interactions.addAll(asList(interactions));
					finalReqs = reqs;
					game.startRotate();
				};
				previousRound.runInteractions();
			} else {
				assertEach((message, stack) ->
						assertFailure(String.format("Error at game creation: " +
										"\nThe following assertion failed: %s",
								message), stack, callingClass), reqs, game);
			}

			assertMoreInteractions();

			if (previousRound == null) {
				continuation.accept(game);
			} else {
				if (printInteractions) {
					System.out.println("BEGIN UML>>>>>>>>>>>>>>>>>>");
					recorder.readOut("__ignored__").values()
							.forEach(System.out::println);
					System.out.println("<<<<<<<<<<<<<<<<<<<<<END UML");
				}
			}
		}
	}

	public void forceReleaseShutdownLock() {
		Assertions.enableSystemExit();
	}

	public interface Setups {
		interface Interactions {
			Assert startRotationAndAssertTheseInteractionsOccurInOrder(
					Interaction<?>... interactions);
		}

		interface Assert extends Rest {
			Assert thenRequire(Requirement<ScotlandYardGame> requirement);
			default Assert thenAssert(Consumer<ScotlandYardGame> assertion) {
				return thenRequire(fromAssertion(assertion));
			}
			default Assert thenAssert(String name, Consumer<ScotlandYardGame> assertion) {
				return thenRequire(fromAssertion(name, assertion));
			}
		}

		interface Rest extends Interactions {
			void thenIgnoreAnyFurtherInteractions();
			void thenAssertNoFurtherInteractions();
			void shouldThrow(Class<? extends Throwable> clazz);
		}
	}

	private <C> void captureAndAssertInteraction(Comparable<?> key,
	                                             Class<? extends Interaction<C>> interactionClazz,
	                                             Supplier<C> unsafeCapture) {
		// TODO wrap exceptions
		C initial = unsafeCapture.get();
		List<C> captures = Stream.generate(unsafeCapture)
				.limit(captureN - 1)
				.collect(toList());
		// view getter with side effect will fail
		List<Entry<Boolean, C>> statuses = captures.stream()
				.map(c -> new SimpleImmutableEntry<>(c.equals(initial), c))
				.collect(toList());
		if (!statuses.stream().allMatch(Entry::getKey)) {
			String restOfTheCaptures = Streams.mapWithIndex(statuses.stream(),
					(e, index) -> format("[%s] Capture::%d: %s",
							e.getKey() ? "OK" : "FAIL", index + 1, e.getValue()))
					.collect(joining("\n"));

			List<Long> failedOnes = Streams.mapWithIndex(statuses.stream(),
					(e, index) -> Optional.of(index + 1).filter(i -> !e.getKey()))
					.flatMap(Streams::stream)
					.collect(toList());
			throw new AssertionError(format("After capturing for %d time(s), " +
							"capture [%s] was different from the first capture." +
							"\nFirst capture: \n%s" +
							"\nThe rest was : \n%s",
					captureN,
					failedOnes.stream().map(v -> "::" + v).collect(joining(",")),
					initial, restOfTheCaptures));
		}

		// incorrect class(instance) means ordering is wrong for the model
		Interaction<?> polled = interactions.poll();
		if (polled == null) {
			switch (endMode) {
				case IGNORE:
					return;
				default:
				case NO_MORE:
					fail("No more interactions expected but got:\n%s::%s", key, initial);
			}
		} else {


			String item = String.format("%d/%d",
					interactionsProcessed - interactions.size(), totalInteractions);

			if (!interactionClazz.isAssignableFrom(polled.getClass())) {
				assertFailure(String.format("Was expecting the following interaction " +
								"(%s)" +
								":\n%s::%s" +
								"\n\nBut instead got:\n%s::%s",
						item, polled.key(), polled.describe(), key, initial),
						polled.origin(), callingClass);
			} else {

				Interaction<C> matched = interactionClazz.cast(polled);

				if (!Objects.equals(matched.key(), key)) {
					assertFailure(format("Error at interaction %s: " +
									"\nWas expecting subject of interaction to be %s but got %s" +
									"\nExpected:\n%s::%s" +
									"\nBut found:\n%s::%s",
							item, polled.key(), key,
							polled.key(), polled.describe(),
							key, initial), polled.origin(), callingClass);
				}
				if (printInteractions) System.out.printf("[%s] %s::%s%n", item, key, initial);

				matched.applyAssertion(initial, (message, stack) ->
						assertFailure(String.format("Error at interaction %s: " +
										"\nExpected result:" +
										"\n%s::%s" +
										"\n\nBut some parameters did not match, actual " +
										"invocation" +
										" " +
										"was:" +
										"\n%s::%s" +
										"\n\nSpecifically, the following assertion failed: %s",
								item, polled.key(), polled.describe(),
								key, initial, message), stack, callingClass));
				matched.executeAction(initial, callingClass);

				AssertionContext sra = (message, stack) ->
						assertFailure(String.format("Error after interaction %s: " +
										"\nThe following assertion failed: %s",
								item, message), stack, callingClass);

				if (interactions.isEmpty()) {
					assertEach(sra, finalReqs, game);
					finalContinuation.accept(game);
				}
			}
		}
	}

	private void assertMoreInteractions() {
		if (!interactions.isEmpty()) {
			assertFailure(format("The following interaction was expected but never " +
							"occurred:\n%s",
					interactions.stream().map(v -> format("%s::%s", v.key(), v.describe()))
							.collect(joining("\n"))),
					interactions.peek().origin(), callingClass);
		}
	}

	public PlayerConfiguration newPlayer(ImmutablePlayer player) {
		return newPlayer(player.colour, player.location, player.tickets);
	}

	public PlayerConfiguration newPlayer(Colour colour, int start,
	                                     int taxi, int bus, int underground, int x2, int secret) {
		return newPlayer(colour, start, ImmutableMap.of(
				TAXI, taxi,
				BUS, bus,
				UNDERGROUND, underground,
				DOUBLE, x2,
				SECRET, secret));
	}

	public PlayerConfiguration newPlayer(Colour colour) {
		return newPlayer(colour, colour.isMrX()
				? MRX_LOCATIONS.get(0)
				: DETECTIVE_LOCATIONS.get(0));
	}

	public PlayerConfiguration newPlayer(Colour colour, int start) {
		return newPlayer(colour, start, colour.isMrX() ? mrXTickets() : detectiveTickets());
	}

	public PlayerConfiguration newPlayer(Colour colour, int start, Map<Ticket, Integer> tickets) {
		PlayerConfiguration that = new Builder(colour)
				.at(start)
				.with(tickets)
				.using((view, location, moves, callback) -> captureAndAssertInteraction(
						mkPlayerKey(colour),
						MakeMoveInteraction.class,
						() -> new MakeMove(view, location, moves, callback)))
				.build();

		// XXX will throw the harness needs to assert all events first
		return printInteractions ? recorder.observePlayer(that) : that;
	}

	public enum SpectatorEvent {
		ON_MOVE_MADE,
		ON_ROUND_STARTED,
		ON_ROTATION_COMPLETE,
		ON_GAME_OVER
	}

	static final String DEFAULT_SPECTATOR = "the only one";

	public Spectator createSpectator(SpectatorEvent... listens) {
		return createSpectator(DEFAULT_SPECTATOR, listens);
	}
	public Spectator createSpectator(String key, SpectatorEvent... listens) {
		Set<SpectatorEvent> listenEvents =
				new HashSet<>(asList(listens.length == 0 ? SpectatorEvent.values() : listens));
		Comparable<?> k = mkSpectatorKey(key);
		Spectator that = recorder.createSpectator();
		return new Spectator() {
			@Override public void onMoveMade(ScotlandYardView view, Move move) {
				if (printInteractions) that.onMoveMade(view, move);
				if (!listenEvents.contains(ON_MOVE_MADE)) return;
				captureAndAssertInteraction(k, MoveMadeInteraction.class,
						() -> new MoveMade(view, move));
			}
			@Override public void onRoundStarted(ScotlandYardView view, int round) {
				if (printInteractions) that.onRoundStarted(view, round);
				if (!listenEvents.contains(ON_ROUND_STARTED)) return;
				captureAndAssertInteraction(k, RoundStartedInteraction.class,
						() -> new RoundStarted(view, round));
			}
			@Override public void onRotationComplete(ScotlandYardView view) {
				if (printInteractions) that.onRotationComplete(view);
				if (!listenEvents.contains(ON_ROTATION_COMPLETE)) return;
				captureAndAssertInteraction(k, RotationCompleteInteraction.class,
						() -> new RotationComplete(view));
			}
			@Override public void onGameOver(ScotlandYardView view, Set<Colour> winningPlayers) {
				if (printInteractions) that.onGameOver(view, winningPlayers);
				if (!listenEvents.contains(ON_GAME_OVER)) return;
				captureAndAssertInteraction(k, GameOverInteraction.class,
						() -> new GameOver(view, winningPlayers));
			}
		};
	}

	interface AssertionContext {
		void fail(String message, List<StackTraceElement> stack);
	}

}
