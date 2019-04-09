package uk.ac.bris.cs.scotlandyard.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.bris.cs.scotlandyard.harness.TestHarness;

import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.bus;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.rounds;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.taxi;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.x2;
import static uk.ac.bris.cs.scotlandyard.harness.PlayerInteractions.player;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.eq;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.hasCurrentPlayer;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.isOnRound;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.neq;
import static uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.spectator;
import static uk.ac.bris.cs.scotlandyard.harness.TestHarness.SpectatorEvent.ON_MOVE_MADE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLACK;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLUE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.RED;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYardView.NOT_STARTED;

/**
 * Test {@link Player} related callbacks for the model
 */
public class ModelRoundTest extends ParameterisedModelTestBase {

	private TestHarness harness;
	@Before public void initialise() { harness = new TestHarness(); }
	@After public void tearDown() { harness.forceReleaseShutdownLock(); }

	@Test
	public void testPlayerNotified() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK);
		PlayerConfiguration blue = harness.newPlayer(BLUE);
		harness.play(createGame(mrX, blue)).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testCallbackIsNotNull() {
		harness.play(createGame(harness.newPlayer(BLACK), harness.newPlayer(BLUE)))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().givenCallback(neq(null)))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testInitialPositionMatchFirstRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK);
		PlayerConfiguration blue = harness.newPlayer(BLUE);

		// all locations should match the initial given location for all players
		harness.play(createGame(mrX, blue)).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().givenLocation(eq(mrX.location)).willPickFirst(),
				player(BLUE).makeMove().givenLocation(eq(blue.location)))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testCallbackPositionMatchPlayerLocationDuringRevealRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		// reveal round should match
		harness.play(createGame(rounds(true, true), mrX, blue))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().givenLocation(eq(mrX.location)).willPickFirst(),
						player(BLUE).makeMove().givenLocation(eq(blue.location)))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testCallbackPositionMatchPlayerLocationDuringHiddenRound() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		// hidden round should match
		ScotlandYardGame game = createGame(rounds(false, false), mrX, blue);
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().givenLocation(eq(mrX.location)).willPick(taxi(46)),
				player(BLUE).makeMove().givenLocation(eq(blue.location)).willPick(taxi(95)))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGetCurrentPlayerCorrectForAllRounds() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));
		DoubleMove expected = x2(BLACK, taxi(32), taxi(19));

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(expected),

				// MrX consumes a double move, current player after the move has been
				// committed will be Blue for all three onMoveMade notifications
				spectator().onMoveMade()
						.givenGameState(hasCurrentPlayer(BLUE)),
				spectator().onMoveMade()
						.givenGameState(hasCurrentPlayer(BLUE)),
				spectator().onMoveMade()
						.givenGameState(hasCurrentPlayer(BLUE)),
				player(BLUE).makeMove().willPick(taxi(95)),

				// Blue consumes a taxi move, current player after the move has been
				// committed will be Blue for all onMoveMade notifications
				spectator().onMoveMade()
						.givenGameState(hasCurrentPlayer(BLACK)))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testRoundIncrementsAfterAllPlayersHavePlayed() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 35);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 50);

		harness.play(createGame(mrX, blue))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(65)),
						player(BLUE).makeMove().willPick(taxi(49)))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(bus(82)),
						player(BLUE).makeMove().givenGameState(isOnRound(2)))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testRoundIncrementsCorrectlyForDoubleMove() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(mrX, blue);
		game.registerSpectator(harness.createSpectator(ON_MOVE_MADE));

		harness.play(game)
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(x2(taxi(32), taxi(19))),
						spectator().onMoveMade().givenGameState(isOnRound(NOT_STARTED)),
						spectator().onMoveMade().givenGameState(isOnRound(1)),
						spectator().onMoveMade().givenGameState(isOnRound(2)))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testMrXIsTheFirstToPlay() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK);
		PlayerConfiguration blue = harness.newPlayer(BLUE);

		harness.play(createGame(mrX, blue)).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPickFirst(),
				player(BLUE).makeMove())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testRoundWaitsForPlayerWhoDoesNotRespond() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK);
		PlayerConfiguration blue = harness.newPlayer(BLUE);

		harness.play(createGame(mrX, blue)).startRotationAndAssertTheseInteractionsOccurInOrder(
				// black player does nothing, preventing the game from rotating
				player(BLACK).makeMove().wontRespond())
				// blue should not receive any move request at all because black stalled
				// the game
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testRoundRotationNotifiesAllPlayer() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 35);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 50);
		PlayerConfiguration red = harness.newPlayer(RED, 26);

		ScotlandYardGame game = createGame(mrX, blue, red);
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(22)),
				player(BLUE).makeMove().willPick(taxi(37)),
				player(RED).makeMove().willPick(taxi(15)))

				// everyone should be notified only once
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testCallbackWithNullWillThrow() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK);
		PlayerConfiguration blue = harness.newPlayer(BLUE);
		ScotlandYardGame game = createGame(mrX, blue);

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick((Move) null))
				.shouldThrow(NullPointerException.class);
	}

	@Test
	public void testCallbackWithIllegalMoveNotInGivenMovesWillThrow() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK);
		PlayerConfiguration blue = harness.newPlayer(BLUE);
		ScotlandYardGame game = createGame(mrX, blue);

		// supplying a illegal tickets to the given consumer should not be
		// allowed in this case, BUS ticket with destination 20 is not included
		// in the given list
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(bus(20)))
				.shouldThrow(IllegalArgumentException.class);
	}

}
