package uk.ac.bris.cs.scotlandyard.harness;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import uk.ac.bris.cs.scotlandyard.auxiliary.AnonymousMoves.AnonymousMove;
import uk.ac.bris.cs.scotlandyard.harness.Captures.Player.MakeMove;
import uk.ac.bris.cs.scotlandyard.harness.TestHarness.AssertionContext;
import uk.ac.bris.cs.scotlandyard.harness.TestHarness.Interaction;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.format;
import static uk.ac.bris.cs.scotlandyard.harness.Assertions.capture;
import static uk.ac.bris.cs.scotlandyard.harness.Interactions.assertEach;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.Interaction.describeMethod;

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
public final class PlayerInteractions {

	private PlayerInteractions() {}

	static Comparable<?> mkPlayerKey(Colour colour) {
		return format("Player(%s)", colour);
	}


	// XXX well, this describes intent more clearly than a mutating builder, so yeah
	public static PlayerMethods player(Colour colour) {
		List<StackTraceElement> stack = capture();
		return () -> new Action() {
			private List<Requirement<ScotlandYardView>> viewReqs = new ArrayList<>();
			private List<Requirement<Integer>> locationReqs = new ArrayList<>();
			private List<Requirement<Set<Move>>> movesReqs = new ArrayList<>();
			private List<Requirement<Consumer<Move>>> callbackReqs = new ArrayList<>();
			private Move move = null;
			private boolean pickFirstMove = true;
			private boolean respond = true;
			@Override public Action givenGameState(Requirement<ScotlandYardView> req) {
				viewReqs.add(req);
				return this;
			}
			@Override public Action givenLocation(Requirement<Integer> req) {
				locationReqs.add(req);
				return this;
			}
			@Override public Action givenMoves(Requirement<Set<Move>> req) {
				movesReqs.add(req);
				return this;
			}
			@Override public Action givenCallback(Requirement<Consumer<Move>> req) {
				callbackReqs.add(req);
				return this;
			}
			@Override public MakeMoveInteraction willPick(Move move) {
				// XXX move can be intentionally null to test whether model throws
				this.respond = true;
				this.pickFirstMove = false;
				this.move = move;
				return this;
			}
			@Override public MakeMoveInteraction willPick(AnonymousMove move) {
				return willPick(move.colour(colour));
			}
			@Override public MakeMoveInteraction willPickFirst() {
				this.respond = true;
				this.pickFirstMove = true;
				return this;
			}
			@Override public MakeMoveInteraction wontRespond() {
				this.respond = false;
				return this;
			}
			@Override public Comparable<?> key() {
				return mkPlayerKey(colour);
			}
			@Override
			public void applyAssertion(MakeMove capture, AssertionContext ctx) {
				assertEach(ctx, viewReqs, capture.view);
				assertEach(ctx, locationReqs, capture.location);
				assertEach(ctx, movesReqs, capture.moves);
				assertEach(ctx, callbackReqs, capture.callback);
			}
			@Override public void executeAction(MakeMove capture, String callingClass) {
				if (!respond) return;
				if (pickFirstMove) capture.callback.accept(capture.moves.iterator().next());
				else capture.callback.accept(move);

			}
			@Override public String describe() {
				return describeMethod("makeMove", of(
						"view", viewReqs,
						"location", locationReqs,
						"moves", movesReqs,
						"callback", callbackReqs));
			}
			@Override public List<StackTraceElement> origin() { return stack; }
		};
	}

	interface MakeMoveInteraction extends Interaction<MakeMove> {}

	public interface PlayerMethods {
		Action makeMove();
	}

	public interface Action extends MakeMoveInteraction {
		Action givenGameState(Requirement<ScotlandYardView> req);
		Action givenLocation(Requirement<Integer> req);
		Action givenMoves(Requirement<Set<Move>> req);
		Action givenCallback(Requirement<Consumer<Move>> req);
		MakeMoveInteraction willPick(Move move);
		MakeMoveInteraction willPickFirst();
		MakeMoveInteraction willPick(AnonymousMove move);
		MakeMoveInteraction wontRespond();
	}

}
