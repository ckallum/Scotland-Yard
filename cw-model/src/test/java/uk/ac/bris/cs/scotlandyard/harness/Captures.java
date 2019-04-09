package uk.ac.bris.cs.scotlandyard.harness;

import com.google.common.collect.ImmutableSet;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.fixedOrder;

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
final class Captures {

	private Captures() {}

	static final class Player {
		private Player() {}
		static final class MakeMove {
			final ImmutableScotlandYardView view;
			final int location;
			final ImmutableSet<Move> moves;
			final Consumer<Move> callback;
			MakeMove(ScotlandYardView view,
			         int location, Set<Move> moves, Consumer<Move> callback) {
				this.view = ImmutableScotlandYardView.snapshot(view);
				this.location = location;
				this.moves = ImmutableSet.copyOf(moves);
				this.callback = callback;
			}
			@Override public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				MakeMove that = (MakeMove) o;
				return location == that.location &&
						Objects.equals(view, that.view) &&
						Objects.equals(moves, that.moves);
			}
			@Override
			public int hashCode() { return hash(view, location, moves); }
			@Override public String toString() {
				return format("makeMove(" +
								"\n\tview = %s," +
								"\n\tlocation=%d," +
								"\n\tmoves = %s," +
								"\n\tcallback = %s\n)",
						view, location, moves, callback);
			}
		}
	}


	static final class Spectator {
		private Spectator() {}
		static final class MoveMade {
			final ImmutableScotlandYardView view;
			final Move move;
			MoveMade(ScotlandYardView view, Move move) {
				this.view = ImmutableScotlandYardView.snapshot(view);
				this.move = move;
			}
			@Override public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				MoveMade that = (MoveMade) o;
				return Objects.equals(view, that.view) &&
						Objects.equals(move, that.move);
			}
			@Override public int hashCode() { return hash(view, move); }
			@Override public String toString() {
				return format("onMoveMade(" +
						"\n\tview = %s," +
						"\n\tmove = %s\n)", view, move);
			}
		}
		static final class RoundStarted {
			final ImmutableScotlandYardView view;
			final int round;
			RoundStarted(ScotlandYardView view, int round) {
				this.view = ImmutableScotlandYardView.snapshot(view);
				this.round = round;
			}
			@Override public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				RoundStarted that = (RoundStarted) o;
				return round == that.round &&
						Objects.equals(view, that.view);
			}
			@Override public int hashCode() { return hash(view, round); }
			@Override public String toString() {
				return format("onRoundStarted(" +
						"\n\tview = %s," +
						"\n\tround = %s\n)", view, round);
			}
		}
		static final class RotationComplete {
			final ImmutableScotlandYardView view;
			RotationComplete(ScotlandYardView view) {
				this.view = ImmutableScotlandYardView.snapshot(view);
			}
			@Override public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				RotationComplete that = (RotationComplete) o;
				return Objects.equals(view, that.view);
			}
			@Override public int hashCode() { return hash(view); }
			@Override public String toString() {
				return format("onRotationComplete(" +
						"\n\tview = %s\n)", view);
			}
		}
		static final class GameOver {
			final ImmutableScotlandYardView view;
			final ImmutableSet<Colour> winningPlayers;
			GameOver(ScotlandYardView view,
			         Set<Colour> winningPlayers) {
				this.view = ImmutableScotlandYardView.snapshot(view);
				this.winningPlayers = ImmutableSet.copyOf(winningPlayers);
			}
			@Override public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				GameOver that = (GameOver) o;
				return Objects.equals(view, that.view) &&
						Objects.equals(winningPlayers, that.winningPlayers);
			}
			@Override public int hashCode() { return hash(view, winningPlayers); }
			@Override public String toString() {
				return format("onGameOver(" +
						"\n\tview = %s," +
						"\n\twinningPlayers = %s\n)", view, fixedOrder(winningPlayers));
			}
		}
	}

}
