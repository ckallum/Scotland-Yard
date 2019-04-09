package uk.ac.bris.cs.scotlandyard.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.bris.cs.scotlandyard.auxiliary.TestGames;
import uk.ac.bris.cs.scotlandyard.harness.TestHarness;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.bus;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.pass;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.rounds;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.taxi;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.x2;
import static uk.ac.bris.cs.scotlandyard.harness.PlayerInteractions.player;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.eq;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.isA;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.isOnRound;
import static uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.spectator;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_GAME_OVER;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_MOVE_MADE;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_ROTATION_COMPLETE;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_ROUND_STARTED;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLACK;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLUE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.GREEN;
import static uk.ac.bris.cs.scotlandyard.model.Colour.RED;
import static uk.ac.bris.cs.scotlandyard.model.Colour.WHITE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.YELLOW;

/**
 * Tests spectator related features of the model
 */
public class ModelSpectatorTest extends ParameterisedModelTestBase {

	private TestHarness harness;
	@Before public void initialise() { harness = new TestHarness(); }
	@After public void tearDown() { harness.forceReleaseShutdownLock(); }

	// convenience for creating 6 valid player at non-overlapping locations
	private ScotlandYardGame createValidSixPlayerGame(TestHarness harness) {
		List<Integer> detectiveLocations = TestGames.DETECTIVE_LOCATIONS;
		return createGame(
				harness.newPlayer(BLACK, TestGames.MRX_LOCATIONS.get(0)),
				harness.newPlayer(RED, detectiveLocations.get(0)),
				harness.newPlayer(GREEN, detectiveLocations.get(1)),
				harness.newPlayer(BLUE, detectiveLocations.get(2)),
				harness.newPlayer(WHITE, detectiveLocations.get(3)),
				harness.newPlayer(YELLOW, detectiveLocations.get(4)));
	}

	@Test
	public void testRegisterNullSpectatorShouldThrow() {
		assertThatThrownBy(() -> createValidSixPlayerGame().registerSpectator(null))
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	public void testUnregisterNullSpectatorShouldThrow() {
		assertThatThrownBy(() -> createValidSixPlayerGame().unregisterSpectator(null))
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	public void testNoSpectatorsByDefault() {
		assertThat(createValidSixPlayerGame().getSpectators()).isEmpty();
	}

	@Test
	public void testGetSpectatorIsImmutable() {
		Collection<Spectator> spectators = createValidSixPlayerGame(harness).getSpectators();
		assertThatThrownBy(() -> spectators.add(harness.createSpectator()))
				.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	public void testRegisterAndUnregisterSpectator() {
		ScotlandYardGame game = createValidSixPlayerGame(harness);
		Spectator spectator = harness.createSpectator();
		game.registerSpectator(spectator);
		assertThat(game.getSpectators()).containsExactlyInAnyOrder(spectator);
		game.unregisterSpectator(spectator);
		assertThat(game.getSpectators()).isEmpty();
	}

	@Test
	public void testRegisterAndUnregisterMoreThanOneSpectator() {
		ScotlandYardGame game = createValidSixPlayerGame(harness);
		Spectator a = harness.createSpectator("a");
		Spectator b = harness.createSpectator("b");
		game.registerSpectator(a);
		game.registerSpectator(b);
		assertThat(game.getSpectators()).containsExactlyInAnyOrder(a, b);
		game.unregisterSpectator(a);
		assertThat(game.getSpectators()).containsExactlyInAnyOrder(b);
		game.unregisterSpectator(b);
		assertThat(game.getSpectators()).isEmpty();
	}

	@Test
	public void testRegisterSameSpectatorTwiceShouldThrow() {
		ScotlandYardGame game = createValidSixPlayerGame(harness);
		Spectator spectator = harness.createSpectator();
		// can't register the same spectator
		game.registerSpectator(spectator);
		assertThatThrownBy(() -> game.registerSpectator(spectator))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testUnregisterIllegalSpectatorShouldThrow() {
		ScotlandYardGame game = createValidSixPlayerGame(harness);
		// can't unregister a spectator that has never been registered before
		assertThatThrownBy(() -> game.unregisterSpectator(harness.createSpectator()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testDoubleMoveShouldNotifyRoundStartTwoTimesInOrder() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(mrX, blue);
		Spectator spectator = harness.createSpectator();
		game.registerSpectator(spectator);

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))),
				spectator().onMoveMade()
						.givenMove(isA(DoubleMove.class))
						.givenGameState(isOnRound(0)),
				spectator().onRoundStarted(),
				spectator().onMoveMade()
						.givenMove(isA(TicketMove.class))
						.givenGameState(isOnRound(1)),
				spectator().onRoundStarted(),
				spectator().onMoveMade()
						.givenMove(isA(TicketMove.class))
						.givenGameState(isOnRound(2)),
				player(BLUE).makeMove().wontRespond())
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testDoubleMoveShouldNotifyMoveMadeThreeTimesInOrder() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))),
				spectator().onMoveMade()
						.givenMove(isA(DoubleMove.class)),
				spectator().onMoveMade()
						.givenMove(isA(TicketMove.class)),
				spectator().onMoveMade()
						.givenMove(isA(TicketMove.class)),
				player(BLUE).makeMove().wontRespond())
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testDoubleMoveShouldNotifyWithCorrectTicketAndLastDestinationDuringHiddenRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(rounds(false, false, false, false), mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		// during hidden rounds, double move never reveals the actual location
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))),
				spectator().onMoveMade()
						.givenMove(eq(x2(BLACK, taxi(0), bus(0)))),
				spectator().onMoveMade()
						.givenMove(eq(taxi(BLACK, 0))),
				spectator().onMoveMade()
						.givenMove(eq(bus(BLACK, 0))))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testDoubleMoveShouldNotifyWithCorrectTicketAndDestinationDuringRevealRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(rounds(true, true, true, true), mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		// during reveal rounds, double move always reveal the actual location
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))),
				spectator().onMoveMade()
						.givenMove(eq(x2(BLACK, taxi(46), bus(34)))),
				spectator().onMoveMade()
						.givenMove(eq(taxi(BLACK, 46))),
				spectator().onMoveMade()
						.givenMove(eq(bus(BLACK, 34))))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void
	testDoubleMoveShouldNotifyWithCorrectTicketAndDestinationWhenGoingFromRevealToHiddenRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(rounds(true, false, false), mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		// if the first move of a double move happens during a reveal round and
		// the next move is not, reveal actual location for the first ticket and
		// then the last revealed location for the second ticket
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))),
				spectator().onMoveMade().givenMove(eq(x2(BLACK, taxi(46), bus(46)))),
				spectator().onMoveMade().givenMove(eq(taxi(BLACK, 46))),
				spectator().onMoveMade().givenMove(eq(bus(BLACK, 46))))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void
	testDoubleMoveShouldNotifyWithCorrectTicketAndDestinationWhenGoingFromHiddenToRevealRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(rounds(false, true, true), mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		// if the first move of a double move happens during a reveal round and
		// the next move is not, reveal actual location for the first ticket and
		// then the last revealed location for the second ticket
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))),
				spectator().onMoveMade().givenMove(eq(x2(BLACK, taxi(0), bus(34)))),
				spectator().onMoveMade().givenMove(eq(taxi(BLACK, 0))),
				spectator().onMoveMade().givenMove(eq(bus(BLACK, 34))))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void
	testDetectiveTicketMoveShouldNotifyWithCorrectTicketAndDestinationDuringHiddenRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(rounds(false, false, false, false), mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		// detective should not have their ticket destination changed in any way
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))),
				spectator().onMoveMade(),
				spectator().onMoveMade(),
				spectator().onMoveMade(),
				player(BLUE).makeMove().willPick(taxi(95)),
				spectator().onMoveMade().givenMove(eq(taxi(BLUE, 95))))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void
	testDetectiveTicketMoveShouldNotifyWithCorrectTicketAndDestinationDuringRevealRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(rounds(true, true, true, true), mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		// detective should not have their ticket destination changed in any way
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))),
				spectator().onMoveMade(),
				spectator().onMoveMade(),
				spectator().onMoveMade(),
				player(BLUE).makeMove().willPick(taxi(95)),
				spectator().onMoveMade().givenMove(eq(taxi(BLUE, 95))))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testPassMoveShouldNotifyOnce() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 111);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94, 0, 0, 0, 0, 0);
		ScotlandYardGame game = createGame(mrX, red, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		// pass move should notify once
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				spectator().onMoveMade(),
				player(RED).makeMove().willPick(taxi(112)),
				spectator().onMoveMade(),
				player(BLUE).makeMove().willPick(pass()),
				spectator().onMoveMade().givenMove(eq(pass(BLUE))))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testAllPlayersPlayedShouldNotifyRotationCompletedOnce() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 111);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(mrX, red, blue);
		game.registerSpectator(harness.createSpectator(ON_ROTATION_COMPLETE));

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(RED).makeMove().willPick(taxi(112)),
				player(BLUE).makeMove().willPick(taxi(95)),
				spectator().onRotationComplete())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testNotifyRotationCompleteAfterRotationIsReallyComplete() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 85);
		ScotlandYardGame game = createGame(rounds(true, true), mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_ROTATION_COMPLETE, ON_GAME_OVER));

		// notify rotation complete when the rotation completes, but not when the game is over.
		harness.play(game)
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(103)),
						player(BLUE).makeMove().willPick(taxi(68)),
						spectator().onRotationComplete())
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(102)),
						player(BLUE).makeMove().willPick(taxi(51)),
						spectator().onGameOver())
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testAllPlayersPlayedWithOneDoubleMoveShouldNotifyRotationCompletedOnce() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 111);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);
		ScotlandYardGame game = createGame(mrX, red, blue);
		game.registerSpectator(harness.createSpectator(ON_ROTATION_COMPLETE));

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), taxi(47))),
				player(RED).makeMove().willPick(taxi(112)),
				player(BLUE).makeMove().willPick(taxi(95)),
				spectator().onRotationComplete())
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testStartRotateShouldNotifyRoundStartedOnce() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 111);
		ScotlandYardGame game = createGame(mrX, red);
		game.registerSpectator(harness.createSpectator(ON_ROUND_STARTED));

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPickFirst(),
				spectator().onRoundStarted())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGameOverShouldNotifyGameOverOnce() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 47);
		ScotlandYardGame game = createGame(mrX, red);
		game.registerSpectator(harness.createSpectator(ON_GAME_OVER));

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(RED).makeMove().willPick(taxi(46)),
				spectator().onGameOver())
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testMrXCaptureShouldNotifyMoveAndThenNotifyGameOver() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 47);
		ScotlandYardGame game = createGame(mrX, red);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE, ON_GAME_OVER));

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				spectator().onMoveMade(),
				player(RED).makeMove().willPick(taxi(46)),
				spectator().onMoveMade(),
				spectator().onGameOver().givenWinners(eq(players(RED))))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testFinalMoveShouldNotifyMoveAndThenNotifyGameOver() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 111);
		ScotlandYardGame game = createGame(rounds(true), mrX, red);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE, ON_GAME_OVER));

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				spectator().onMoveMade(),
				player(RED).makeMove().willPick(taxi(112)),
				spectator().onMoveMade(),
				spectator().onGameOver().givenWinners(eq(players(BLACK))))
				.thenAssertNoFurtherInteractions();
	}

	// not a test method
	private static Set<Colour> players(Colour... colours) {
		return new HashSet<>(asList(colours));
	}

}
