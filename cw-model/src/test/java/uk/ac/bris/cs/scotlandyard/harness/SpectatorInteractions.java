package uk.ac.bris.cs.scotlandyard.harness;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import uk.ac.bris.cs.scotlandyard.harness.Captures.Spectator.GameOver;
import uk.ac.bris.cs.scotlandyard.harness.Captures.Spectator.MoveMade;
import uk.ac.bris.cs.scotlandyard.harness.Captures.Spectator.RotationComplete;
import uk.ac.bris.cs.scotlandyard.harness.Captures.Spectator.RoundStarted;
import uk.ac.bris.cs.scotlandyard.harness.TestHarness.AssertionContext;
import uk.ac.bris.cs.scotlandyard.harness.TestHarness.Interaction;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGame;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.format;
import static uk.ac.bris.cs.scotlandyard.harness.Assertions.capture;
import static uk.ac.bris.cs.scotlandyard.harness.Interactions.assertEach;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.fromAssertion;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.DEFAULT_SPECTATOR;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.Interaction.describeMethod;

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
public class SpectatorInteractions {

	private SpectatorInteractions() {}

	static Comparable<?> mkSpectatorKey(String key) {
		if (key.equals(DEFAULT_SPECTATOR)) return "Spectator";
		else return format("Spectator(%s)", key);
	}

	private static abstract class ActionBuilder<T extends SpectatorAction<T>, C>
			implements SpectatorAction<T>, Interaction<C> {
		final List<Requirement<ScotlandYardView>> viewReqs = new ArrayList<>();
		private final List<StackTraceElement> origin;
		private final Comparable<?> key;
		ActionBuilder(List<StackTraceElement> origin, Comparable<?> key) {
			this.origin = origin;
			this.key = key;
		}
		@Override public List<StackTraceElement> origin() { return origin; }
		@Override public Comparable<?> key() { return key; }
		@Override public T givenGameState(Requirement<ScotlandYardView> req) {
			return store(viewReqs, req);
		}
		@SuppressWarnings("unchecked")
		final <R> T store(List<Requirement<R>> place, Requirement<R> req) {
			place.add(req);
			return (T) this;
		}
	}

	private static class MoveMadeBuilder
			extends ActionBuilder<MoveMadeAction, MoveMade>
			implements MoveMadeAction {
		private final List<Requirement<Move>> moveReqs = new ArrayList<>();
		MoveMadeBuilder(List<StackTraceElement> origin, Comparable<?> key) {
			super(origin, key);
		}
		@Override public MoveMadeAction givenMove(Requirement<Move> req) {
			return store(moveReqs, req);
		}
		@Override public void applyAssertion(MoveMade capture, AssertionContext ctx) {
			assertEach(ctx, viewReqs, capture.view);
			assertEach(ctx, moveReqs, capture.move);
		}
		@Override public String describe() {
			return describeMethod("onMoveMade", of("view", viewReqs, "move", moveReqs));
		}
	}

	private static class RoundStartedBuilder
			extends ActionBuilder<RoundStartedAction, RoundStarted>
			implements RoundStartedAction {
		private final List<Requirement<Integer>> roundReqs = new ArrayList<>();
		RoundStartedBuilder(List<StackTraceElement> origin, Comparable<?> key) {
			super(origin, key);
		}
		@Override public RoundStartedAction givenRound(Requirement<Integer> req) {
			return store(roundReqs, req);
		}
		@Override public void applyAssertion(RoundStarted capture, AssertionContext ctx) {
			assertEach(ctx, viewReqs, capture.view);
			assertEach(ctx, roundReqs, capture.round);
		}
		@Override public String describe() {
			return describeMethod("onRoundStarted", of("view", viewReqs, "round", roundReqs));
		}
	}

	private static class RotationCompleteBuilder
			extends ActionBuilder<RotationCompleteAction, RotationComplete>
			implements RotationCompleteAction {
		private Consumer<ScotlandYardView> action = nothing();
		RotationCompleteBuilder(List<StackTraceElement> origin, Comparable<?> key) {
			super(origin, key);
		}
		@Override
		public RotationCompleteInteraction respondWith(Consumer<ScotlandYardView> action) {
			this.action = action;
			return this;
		}
		@Override public void applyAssertion(RotationComplete capture, AssertionContext ctx) {
			assertEach(ctx, viewReqs, capture.view);
		}
		@Override public void executeAction(RotationComplete capture, String callingClass) {
			if (action != null) action.accept(capture.view);
		}
		@Override public String describe() {
			return describeMethod("onRotationComplete", of("view", viewReqs));
		}
	}

	private static class GameOverBuilder
			extends ActionBuilder<GameOverAction, GameOver>
			implements GameOverAction {
		private final List<Requirement<Set<Colour>>> winnerReqs = new ArrayList<>();
		GameOverBuilder(List<StackTraceElement> origin, Comparable<?> key) {
			super(origin, key);
		}
		@Override public GameOverAction givenWinners(Requirement<Set<Colour>> req) {
			return store(winnerReqs, req);
		}
		@Override public void applyAssertion(GameOver capture, AssertionContext ctx) {
			assertEach(ctx, viewReqs, capture.view);
		}
		@Override public String describe() {
			return describeMethod("onGameOver", of("view", viewReqs, "winning", winnerReqs));
		}
	}

	public static SpectatorMethods spectator() {return spectator(DEFAULT_SPECTATOR);}
	//TODO should test for multiple spectators in undefined order(i.e keys...)
	public static SpectatorMethods spectator(String key) {
		Comparable<?> k = mkSpectatorKey(key);
		List<StackTraceElement> stack = capture();
		return new SpectatorMethods() {
			@Override public MoveMadeAction onMoveMade() {
				return new MoveMadeBuilder(stack, k);
			}
			@Override public RoundStartedAction onRoundStarted() {
				return new RoundStartedBuilder(stack, k);
			}
			@Override public RotationCompleteAction onRotationComplete() {
				return new RotationCompleteBuilder(stack, k);
			}
			@Override public GameOverAction onGameOver() {
				return new GameOverBuilder(stack, k);
			}
		};
	}

	interface MoveMadeInteraction extends Interaction<MoveMade> {}
	interface RoundStartedInteraction extends Interaction<RoundStarted> {}
	interface RotationCompleteInteraction extends Interaction<RotationComplete> {}
	interface GameOverInteraction extends Interaction<GameOver> {}

	public interface SpectatorMethods {
		MoveMadeAction onMoveMade();
		RoundStartedAction onRoundStarted();
		RotationCompleteAction onRotationComplete();
		GameOverAction onGameOver();
	}

	//@formatter:off
	interface SpectatorAction<T extends SpectatorAction<T>> {
		T givenGameState(Requirement<ScotlandYardView>  req);
		default T givenGameState(Consumer<ScotlandYardView>  assertion) {
			return givenGameState(fromAssertion(assertion));
		}
		default T givenGameState(String name, Consumer<ScotlandYardView> assertion) {
			return givenGameState(fromAssertion(name, assertion));
		}
	}
	public interface MoveMadeAction extends SpectatorAction<MoveMadeAction>, MoveMadeInteraction {
		MoveMadeAction givenMove(Requirement<Move>  req);
		default MoveMadeAction givenMove(Consumer<Move>  assertion) {
			return givenMove(fromAssertion(assertion));
		}
		default MoveMadeAction givenMove(String name, Consumer<Move>  assertion) {
			return givenMove(fromAssertion(name, assertion));
		}
	}
	public interface RoundStartedAction extends SpectatorAction<RoundStartedAction>, RoundStartedInteraction {
		RoundStartedAction givenRound(Requirement<Integer>  req);
		default RoundStartedAction givenRound(Consumer<Integer>  assertion) {
			return givenRound(fromAssertion(assertion));
		}
		default RoundStartedAction givenRound(String name, Consumer<Integer>  assertion) {
			return givenRound(fromAssertion(name, assertion));
		}
	}
	public interface RotationCompleteAction extends SpectatorAction<RotationCompleteAction>, RotationCompleteInteraction {
		RotationCompleteInteraction respondWith(Consumer<ScotlandYardView> action);
	}
	public interface GameOverAction extends SpectatorAction<GameOverAction>, GameOverInteraction {
		GameOverAction givenWinners(Requirement<Set<Colour>>  req);
		default GameOverAction givenWinners(Consumer<Set<Colour>>  assertion) {
			return givenWinners(fromAssertion(assertion));
		}
		default GameOverAction givenWinners(String name, Consumer<Set<Colour>>  assertion) {
			return givenWinners(fromAssertion(name, assertion));
		}
//		GameOverInteraction respondWith(Consumer<ScotlandYardView> action);

	}
	//@formatter:on

	public static Consumer<ScotlandYardView> nothing() { return view -> {}; }
	public static Consumer<ScotlandYardView> startRotate(ScotlandYardGame game) {
		return view -> game.startRotate();
	}

}
