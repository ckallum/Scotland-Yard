package uk.ac.bris.cs.scotlandyard.harness;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import uk.ac.bris.cs.scotlandyard.harness.TestHarness.AssertionContext;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGame;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
@FunctionalInterface
public interface Requirement<T> {

	void check(AssertionContext softly, T actual);

	default Requirement<T> and(Requirement<T> that) {
		return (softly, actual) -> {
			this.check(softly, actual);
			that.check(softly, actual);
		};
	}

//	@SafeVarargs static <T> Requirement<T> where(Requirement<T> first, Requirement<T>... rest) {
//		List<Requirement<T>> reqs = Lists.asList(first, rest);
//		return named(reqs.stream()
//						.map(Requirement::toString)
//						.collect(joining(", ")),
//				(softly, actual) -> {
//					for (Requirement<T> req : reqs) req.check(softly, actual);
//				});
//	}

	static <T> Requirement<T> named(String name, Requirement<T> that) {
		return new Requirement<T>() {
			@Override public void check(AssertionContext softly, T actual) {
				that.check(softly, actual);
			}
			@Override public String toString() { return name; }
		};
	}

	static <T> Requirement<T> canBeAnything() {
		return named("can be anything", (softly, actual) -> {});
	}
	static <T> Requirement<T> doesNotMatter() { return canBeAnything(); }
	static <T> Requirement<T> eq(T expected) {
		List<StackTraceElement> stack = Assertions.capture();
		return named("= " + Objects.toString(expected, "<null>"),
				(softly, actual) -> {
					if (!Objects.equals(expected, actual))
						softly.fail(format("expected:<%s> but was:<%s>", expected, actual), stack);
				});
	}
	static <T> Requirement<T> neq(T notExpected) {
		List<StackTraceElement> stack = Assertions.capture();
		return named("!= " + Objects.toString(notExpected, "<null>"),
				(softly, actual) -> {
					if (Objects.equals(notExpected, actual))
						softly.fail(format("Found forbidden value: <%s>", notExpected), stack);
				});
	}
	static <T> Requirement<T> isA(Class<?> expected) {
		List<StackTraceElement> stack = Assertions.capture();
		return named("is a  " + Objects.toString(expected.getName(), "<null>"),
				(softly, actual) -> {
					if (!expected.isInstance(actual))
						softly.fail(format("Found wrong type: expected %s but found type %s: <%s>",
								expected.getName(), actual.getClass().getName(), actual), stack);
				});
	}

	@SuppressWarnings("varargs") @SafeVarargs
	static <T> Requirement<Set<T>> containsOnly(T... expected) {
		return containsOnly(Arrays.stream(expected).collect(Collectors.toSet()));
	}
	static <T> Requirement<Set<T>> containsOnly(Set<T> expected) {
		List<StackTraceElement> stack = Assertions.capture();
		return named(String.format("= %s", expected),
				(softly, actual) -> {
					Set<T> missing = new HashSet<>(expected);
					missing.removeAll(actual);
					Set<T> extra = new HashSet<>(actual);
					extra.removeAll(expected);
					if (!extra.isEmpty() || !missing.isEmpty()) {
						softly.fail(format("Moves is missing %s, and erroneously contains %s",
								missing, extra), stack);
					}
				});
	}
	static <T> Requirement<T> fromAssertion(Consumer<T> requirement) {
		return fromAssertion("obeys assertion: see below", requirement);
	}

	static <T> Requirement<T> fromAssertion(String name, Consumer<T> requirement) {
		List<StackTraceElement> stack = Assertions.capture();
		return named(name, (softly, actual) -> {
			try {
				requirement.accept(actual);
			} catch (AssertionError e) {
				softly.fail(String.format("%s\n%s", name, e.getMessage()), stack);
			}
		});
	}

	static Requirement<ScotlandYardView> hasCurrentPlayer(Colour colour) {
		return fromAssertion(format("has current player: %s", colour),
				game -> assertThat(game.getCurrentPlayer()).isEqualByComparingTo(colour));
	}

	static Requirement<ScotlandYardGame> currentPlayerIs(Colour colour) {
		return fromAssertion(format("Current player is %s", colour),
				game -> assertThat(game.getCurrentPlayer()).isEqualByComparingTo(colour));
	}

	static Requirement<ScotlandYardView> hasPlayerAt(Colour colour, int location) {
		return fromAssertion(format("has player %s at %d", colour, location),
				game -> assertThat(game.getPlayerLocation(colour)).hasValue(location));
	}

	static Requirement<ScotlandYardGame> playerIsAt(Colour colour, int location) {
		return fromAssertion(format("Player %s is at %d", colour, location),
				game -> assertThat(game.getPlayerLocation(colour)).hasValue(location));
	}

	static Requirement<ScotlandYardGame> currentRoundNumberIs(int round) {
		return fromAssertion(format("is on round %d", round),
				game -> assertThat(game.getCurrentRound()).isEqualTo(round));
	}

	static Requirement<ScotlandYardView> isOnRound(int round) {
		return fromAssertion(format("Current round is %d", round),
				game -> assertThat(game.getCurrentRound()).isEqualTo(round));
	}

	static Requirement<ScotlandYardView> isNotOver() {
		return fromAssertion("is not over", game -> assertThat(game.isGameOver()).isFalse());
	}

	static Requirement<ScotlandYardGame> gameNotOver() {
		return fromAssertion("Game is not over", game -> assertThat(game.isGameOver()).isFalse());
	}

	static Requirement<ScotlandYardView> isOver() {
		return fromAssertion("is over", game -> assertThat(game.isGameOver()).isTrue());
	}

	static Requirement<ScotlandYardGame> gameOver() {
		return fromAssertion("Game is over", game -> assertThat(game.isGameOver()).isTrue());
	}

	static <C extends Collection<?>> Requirement<C> hasSize(int size) {
		List<StackTraceElement> stack = Assertions.capture();
		return named("has size " + size, (softly, actual) -> {
			if (actual == null) {
				softly.fail(format("expected:<%s> but was: <null>", size), stack);
				return;
			}
			if (actual.size() != size) {
				softly.fail(format("expected:<%s> but was:<%d(%s)> ",
						size, actual.size(), actual),
						stack);
			}
		});
	}
}
