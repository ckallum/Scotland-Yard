package uk.ac.bris.cs.scotlandyard.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.bris.cs.scotlandyard.harness.TestHarness;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.DEFAULT_REVEAL;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.bus;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.noTickets;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.ofRounds;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.pass;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.taxi;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.ticketCountIs;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.x2;
import static uk.ac.bris.cs.scotlandyard.harness.PlayerInteractions.player;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.playerIsAt;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLACK;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLUE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.RED;

/**
 * Tests actual game logic between players for the model
 */
public class ModelPlayerTest extends ParameterisedModelTestBase {

	private TestHarness harness;
	@Before public void initialise() { harness = new TestHarness(); }
	@After public void tearDown() { harness.forceReleaseShutdownLock(); }

	@Test
	public void testDetectivePassMoveDoesNotAffectMrX() {
		PlayerConfiguration black = harness.newPlayer(BLACK, 45,
				2, 0, 0, 0, 0);
		PlayerConfiguration red = harness.newPlayer(RED, 111, 2, 0, 0, 0, 0);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94, 0, 0, 0, 0, 0);

		ScotlandYardGame game = createGame(ofRounds(23, DEFAULT_REVEAL), black, red, blue);
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(RED).makeMove().willPick(taxi(112)),
				player(BLUE).makeMove().willPick(pass()))
				.thenRequire(ticketCountIs(BLACK, 2, 0, 0, 0, 0))
				.thenRequire(ticketCountIs(RED, 1, 0, 0, 0, 0))
				.thenRequire(ticketCountIs(BLUE, 0, 0, 0, 0, 0))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testDetectiveTicketsGivenToMrXOnlyAfterUse() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45, 1, 1, 1, 0, 0);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94, 2, 0, 0, 0, 0);

		ScotlandYardGame game = createGame(mrX, blue);

		// blue taxi ticket given to black
		// NOTE: black uses the last taxi ticket but was given another one from
		// blue so the total taxi ticket for MrX is one

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(BLUE).makeMove().willPick(taxi(95)))
				.thenRequire(ticketCountIs(BLACK, 1, 1, 1, 0, 0))
				.thenRequire(ticketCountIs(BLUE, 1, 0, 0, 0, 0))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testMrXMovesToDestinationAfterDoubleMove() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45, 2, 1, 1, 1, 0);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94, 2, 0, 0, 0, 0);

		ScotlandYardGame game = createGame(asList(true, true, true), mrX, blue);

		// black should move from 45 to 46 to 47
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), taxi(47))),
				player(BLUE).makeMove().willPick(taxi(95)))
				.thenRequire(playerIsAt(BLACK, 47))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testMrXCorrectTicketDecrementsAfterDoubleMove() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45,
				1, 1, 0, 1, 0);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(mrX, blue);

		// taxi, bus, and double tickets should decrement by 1
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(x2(taxi(46), bus(34))))
				.thenRequire(ticketCountIs(BLACK, 0, 0, 0, 0, 0))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testMrXMovesToDestinationAfterTicketMove() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45,
				1, 0, 0, 0, 0);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		// MrX reveals himself for all rounds
		ScotlandYardGame game = createGame(asList(true, true), mrX, blue);

		// black should move from 45 to 46
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(BLUE).makeMove().willPick(taxi(95)))
				.thenRequire(playerIsAt(BLACK, 46))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testMrXCorrectTicketDecrementsByOneAfterTicketMove() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45, 1, 0, 0, 0, 0);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(mrX, blue);

		// taxi should decrement by 1
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)))
				.thenRequire(ticketCountIs(BLACK, 0, 0, 0, 0, 0))
				.thenIgnoreAnyFurtherInteractions();
	}

	@Test
	public void testDetectiveMovesToDestinationAfterTicketMove() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94, 1, 2, 3, 0, 0);

		ScotlandYardGame game = createGame(mrX, blue);

		// blue should move from 94 to 95
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(BLUE).makeMove().willPick(taxi(95)))
				.thenRequire(playerIsAt(BLUE, 95))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testDetectiveCorrectTicketDecrementsByOneAfterTicketMove() {
		// X 45 TAXI -> 46
		// B 94
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94, 2, 2, 3, 0, 0);

		ScotlandYardGame game = createGame(mrX, blue);

		// taxi should decrement by 1
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(BLUE).makeMove().willPick(taxi(95)))
				.thenRequire(ticketCountIs(BLUE, 1, 2, 3, 0, 0))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testDetectiveLocationHoldsAfterPassMove() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 111);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94, noTickets());

		ScotlandYardGame game = createGame(mrX, red, blue);

		// blue doesn't move
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(RED).makeMove().willPick(taxi(112)),
				player(BLUE).makeMove().willPick(pass()))
				.thenRequire(playerIsAt(BLUE, 94))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testDetectiveTicketCountHoldsAfterPassMove() {
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);
		PlayerConfiguration red = harness.newPlayer(RED, 111);
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94, noTickets());

		ScotlandYardGame game = createGame(mrX, red, blue);

		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove().willPick(taxi(46)),
				player(RED).makeMove().willPick(taxi(112)),
				player(BLUE).makeMove().willPick(pass()))
				.thenRequire(ticketCountIs(BLUE, 0, 0, 0, 0, 0))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testDetectiveLocationAlwaysCorrect() {
		// 45 -> 46 -> 47
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);

		// 94 -> 93 -> 92
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(asList(true, false, true), mrX, blue);

		harness.play(game)
				.thenRequire(playerIsAt(BLUE, 94))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(46)),
						player(BLUE).makeMove().willPick(taxi(93)))
				.thenRequire(playerIsAt(BLUE, 93))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(47)),
						player(BLUE).makeMove().willPick(taxi(92)))
				.thenRequire(playerIsAt(BLUE, 92))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testMrXLocationCorrectOnRevealRound() {
		// 45 -> 46 -> 47
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);

		// 94 -> 93 -> 92
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(asList(true, true, true), mrX, blue);

		// Mr X's location should be available after every reveal round, but not at the play of the
		// game
		harness.play(game)
				.thenRequire(playerIsAt(BLACK, 0))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(46)),
						player(BLUE).makeMove().willPick(taxi(93)))
				.thenRequire(playerIsAt(BLACK, 46))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(47)),
						player(BLUE).makeMove().willPick(taxi(92)))
				.thenRequire(playerIsAt(BLACK, 47))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testMrXLocationIsHisLastRevealLocationOnHiddenRound() {
		// 45 -> 46 -> 47 -> 62 -> 79
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);

		// 94 -> 93 -> 92 -> 73 -> 57
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(asList(true, false, false, true), mrX, blue);

		// Mr X's location should be available after every reveal round, but otherwise give his
		// previous location
		harness.play(game)
				.thenRequire(playerIsAt(BLACK, 0))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(46)),
						player(BLUE).makeMove().willPick(taxi(93)))
				.thenRequire(playerIsAt(BLACK, 46))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(47)),
						player(BLUE).makeMove().willPick(taxi(92)))
				.thenRequire(playerIsAt(BLACK, 46))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(62)),
						player(BLUE).makeMove().willPick(taxi(73)))
				.thenRequire(playerIsAt(BLACK, 46))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(79)),
						player(BLUE).makeMove().willPick(taxi(57)))
				.thenRequire(playerIsAt(BLACK, 79))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testMrXLocationCorrectWithOneRevealRound() {
		// 45 -> 46
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);

		// 94 -> 93
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(singletonList(true), mrX, blue);

		harness.play(game)
				.thenRequire(playerIsAt(BLACK, 0))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(46)),
						player(BLUE).makeMove().willPick(taxi(93)))
				.thenRequire(playerIsAt(BLACK, 46))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testMrXLocationCorrectWithOneHiddenRound() {
		// 45 -> 46
		PlayerConfiguration mrX = harness.newPlayer(BLACK, 45);

		// 94 -> 93
		PlayerConfiguration blue = harness.newPlayer(BLUE, 94);

		ScotlandYardGame game = createGame(singletonList(false), mrX, blue);

		harness.play(game)
				.thenRequire(playerIsAt(BLACK, 0))
				.startRotationAndAssertTheseInteractionsOccurInOrder(
						player(BLACK).makeMove().willPick(taxi(46)),
						player(BLUE).makeMove().willPick(taxi(93)))
				.thenRequire(playerIsAt(BLACK, 0))
				.thenAssertNoFurtherInteractions();
	}

}
