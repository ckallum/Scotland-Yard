package uk.ac.bris.cs.scotlandyard.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.bris.cs.scotlandyard.harness.TestHarness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.bus;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.pass;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.rounds;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.taxi;
import static uk.ac.bris.cs.scotlandyard.harness.PlayerInteractions.player;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.gameNotOver;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.gameOver;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLACK;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLUE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.GREEN;
import static uk.ac.bris.cs.scotlandyard.model.Colour.RED;
import static uk.ac.bris.cs.scotlandyard.model.Colour.WHITE;

/**
 * Tests related to whether the model reports game over correctly
 */
public class ModelGameOverTest extends ParameterisedModelTestBase {

	private TestHarness harness;
	@Before public void initialise() { harness = new TestHarness(); }
	@After public void tearDown() { harness.forceReleaseShutdownLock(); }

	@Test
	public void testStartRoundShouldThrowIfGameAlreadyOver() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 105, 0,0,0,0,0);
		ScotlandYardGame game = createGame(mrX, blue);
		assertThat(game.isGameOver()).isTrue();
		// should throw because game is over
		assertThatThrownBy(game::startRotate).isInstanceOf(IllegalStateException.class);
	}

	@Test
	public void testGameNotOverWhenThereIsStillRoundsLeft() {

		// 86 -> 103 -> 102
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		// 85 -> 68 -> 51
		PlayerConfiguration blue = harness.newPlayer(BLUE, 85);

		harness.play(createGame(rounds(true, false, false), mrX, blue))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(103)),
						player(BLUE).makeMove().willPick(taxi(68)))
				.thenRequire(gameNotOver())
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(102)),
						player(BLUE).makeMove().willPick(taxi(51)))
				.thenRequire(gameNotOver())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGameOverAfterAllRoundsUsed() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 85);

		harness.play(createGame(rounds(true), mrX, blue))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(103)),
						player(BLUE).makeMove().willPick(taxi(68)))
				.thenRequire(gameOver())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGameOverIfAllDetectivesStuck() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 105, 1, 0, 0, 0, 0);
		PlayerConfiguration red = harness.newPlayer(RED, 70, 1, 0, 0, 0, 0);

		harness.play(createGame(mrX, blue, red))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(104)),
						// Blue runs out of tickets at this point
						player(BLUE).makeMove().willPick(taxi(106)),
						// Red runs out of tickets at this point
						player(RED).makeMove().willPick(taxi(71)))
				// All detectives ran out of tickets, they are stuck
				.thenRequire(gameOver())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGameOverIfMrXStuck() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86, 1, 1, 1, 0, 0);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 108);
		harness.play(createGame(mrX, blue)).startRotationAndAssertTheseInteractionsOccurInOrder(
				// MrX picks uses the last taxi ticket and lands on a spot where there
				// is no other method of transport, he can no longer move
				player(BLACK).makeMove().willPick(taxi(104)),
				// MrX will receive an extra bus ticket but he is still stuck
				player(BLUE).makeMove().willPick(bus(105)))
				// game is over because MrX is stuck and cannot make a move (no other detectives can
				// move because they are waiting for MrX, thus game cannot proceed because no one
				// can move at this point; in this case, MrX loses the game by foolishly walking
				// into a bad spot
				.thenRequire(gameOver())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGameNotOverIfMrXWasFreedBeforeNextRotation() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86, 1, 1, 1, 0, 0);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 108);
		PlayerConfiguration red = harness.newPlayer(RED, 134);
		harness.play(createGame(mrX, blue, red))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						// MrX uses the last taxi ticket and lands on a spot where there is no
						// other method of transport, he can no longer move
						player(BLACK).makeMove().willPick(taxi(104)),
						// MrX will receive an extra bus ticket but he is still stuck
						player(BLUE).makeMove().willPick(bus(105)),
						// MrX will receive an extra TAXI ticket, he is now freed
						player(RED).makeMove().willPick(taxi(118)))
				// game is not over because MrX still has a spare TAXI ticket that he can use
				.thenRequire(gameNotOver())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGameOverIfMrXCaptured() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 85);

		harness.play(createGame(mrX, blue)).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(103)),
				// MrX captured at 103
				player(BLUE).makeMove().willPick(taxi(103)))
				.thenRequire(gameOver())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testDetectiveWinsIfMrXCornered() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 103);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 68, 0,0,0,0,0);
		PlayerConfiguration red = harness.newPlayer(RED, 84, 0,0,0,0,0);
		PlayerConfiguration green = harness.newPlayer(GREEN, 102);
		harness.play(createGame(mrX, blue, red, green))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						// MrX moves to 85, of which 2 of the 3 connecting nodes are occupied by
						// blue(68) and red(84)
						player(BLACK).makeMove().willPick(taxi(85)),
						player(BLUE).makeMove().willPick(pass()),
						player(RED).makeMove().willPick(pass()),
						// green then cuts MrX off by moving to 103, MrX is now cornered and
						// cannot move
						player(GREEN).makeMove().willPick(taxi(103)))
				.thenRequire(gameOver())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGameNotOverIfMrXCorneredButCanStillEscape() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 40);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 39, 0,0,0,0,0);
		PlayerConfiguration red = harness.newPlayer(RED, 51, 0,0,0,0,0);
		PlayerConfiguration white = harness.newPlayer(WHITE, 69, 0,0,0,0,0);
		PlayerConfiguration green = harness.newPlayer(GREEN, 41);

		harness.play(createGame(mrX, blue, red, white, green))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(52)),
						player(BLUE).makeMove().willPick(pass()),
						player(RED).makeMove().willPick(pass()),
						player(WHITE).makeMove().willPick(pass()),
						// green then cuts MrX off by moving to 40 but MrX can still escape by
						// taking a bus/secret to 41/13/67/86 or even a double move
						player(GREEN).makeMove().willPick(taxi(40)))
				.thenRequire(gameNotOver())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testGameNotOverBeforeAnyRoundWithNonTerminatingConfiguration() {
		ScotlandYardGame game = createGame(
				harness.newPlayer(BLACK, 86), harness.newPlayer(BLUE, 108));
		// game is not over with initial non-terminating setup
		assertThat(game.isGameOver()).isFalse();
	}

	@Test
	public void testGameOverBeforeAnyRoundWithTerminatingConfiguration() {
		// blue cannot move and is the only detective, the game is over by
		// default
		PlayerConfiguration blue = harness.newPlayer(BLUE, 108, 0,0,0,0,0);
		ScotlandYardGame game = createGame(harness.newPlayer(BLACK, 86), blue);
		// game is over with initial condition terminating setup
		assertThat(game.isGameOver()).isTrue();
	}

	@Test
	public void testWinningPlayerIsEmptyBeforeGameOver() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 108);
		harness.play(createGame(mrX, blue)).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(104)),
				player(BLUE).makeMove().willPick(bus(105)))
				.thenRequire(gameNotOver())
				// game is not over, no winning players
				.thenAssert("Winning players is empty",
						g -> assertThat(g.getWinningPlayers()).isEmpty())
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testWinningPlayerOnlyContainsBlackIfMrXWins() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 108, 1, 0, 0, 0, 0);

		harness.play(createGame(mrX, blue)).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(104)),
				// blue uses his last move
				player(BLUE).makeMove().willPick(taxi(105)))
				.thenRequire(gameOver())
				.thenAssert("Black is the only winner",
						g -> assertThat(g.getWinningPlayers()).containsExactlyInAnyOrder(BLACK))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testWinningPlayerOnlyContainAllDetectivesIfDetectiveWins() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 86);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 85);
		PlayerConfiguration red = harness.newPlayer(RED, 111);

		harness.play(createGame(mrX, blue, red))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(103)),
						// MrX captured at 103
						player(BLUE).makeMove().willPick(taxi(103)))
				.thenRequire(gameOver())
				.thenAssert("All detectives are winners", g ->
						assertThat(g.getWinningPlayers()).containsExactlyInAnyOrder(BLUE, RED))
				.thenIgnoreAnyFurtherInteractions();
	}
}
