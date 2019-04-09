package uk.ac.bris.cs.scotlandyard.model;


import com.google.common.collect.ImmutableSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView;
import uk.ac.bris.cs.scotlandyard.harness.TestHarness;

import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.bus;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.secret;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.taxi;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.x2;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.TicketAbbr.BS;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.TicketAbbr.SC;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.TicketAbbr.TX;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.TicketAbbr.UG;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.TicketAbbr.X2;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.black;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.blue;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.green;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.red;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.white;
import static uk.ac.bris.cs.scotlandyard.harness.ImmutableScotlandYardView.yellow;
import static uk.ac.bris.cs.scotlandyard.harness.PlayerInteractions.player;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.eq;
import static uk.ac.bris.cs.scotlandyard.harness.Requirement.hasSize;
import static uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.spectator;
import static uk.ac.bris.cs.scotlandyard.harness.SpectatorInteractions.startRotate;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLACK;
import static uk.ac.bris.cs.scotlandyard.model.Colour.BLUE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.GREEN;
import static uk.ac.bris.cs.scotlandyard.model.Colour.RED;
import static uk.ac.bris.cs.scotlandyard.model.Colour.WHITE;
import static uk.ac.bris.cs.scotlandyard.model.Colour.YELLOW;

/**
 * Complete game play outs that asserts at player and spectator events that the expected
 * conditions must hold
 * Each test includes a PlantUML diagram at the end in comments marked by
 * {@code @startuml ... @enduml}, you may visualise this using tools provided in the project
 * description
 */
public class ModelSixPlayerPlayOutTestSimple extends ParameterisedModelTestBase {

	private TestHarness harness;
	@Before public void initialise() { harness = new TestHarness(true); }
	@After public void tearDown() { harness.forceReleaseShutdownLock(); }

	@Test
	public void testGameWhereMrXCapturedAtLastMoveBeforeNextRound() {
		PlayerConfiguration black = harness.newPlayer(
				black(78).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5));
		PlayerConfiguration blue = harness.newPlayer(
				blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration green = harness.newPlayer(
				green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration red = harness.newPlayer(
				red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration white = harness.newPlayer(
				white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration yellow = harness.newPlayer(
				yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		List<Boolean> rounds = Arrays.asList(
				false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, false, true);
		ScotlandYardGame game = createGame(rounds, defaultGraph(),
				black, blue, green, red, white, yellow);
		Spectator spectator = harness.createSpectator();
		game.registerSpectator(spectator);
		ImmutableScotlandYardView seed = ImmutableScotlandYardView.snapshot(game);
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove()
						.givenGameState(eq(seed.players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(78))
						.givenMoves(hasSize(193))
						.willPick(x2(BLACK, taxi(77), secret(124))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 5),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, taxi(0), secret(0)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 3, UG, 3, X2, 1, SC, 5),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(1)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 3, UG, 3, X2, 1, SC, 5),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLACK, 0))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(2)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 0))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(26))
						.givenMoves(hasSize(3))
						.willPick(taxi(BLUE, 39)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 39))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(103))
						.givenMoves(hasSize(3))
						.willPick(taxi(GREEN, 102)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 102))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(91))
						.givenMoves(hasSize(5))
						.willPick(taxi(RED, 105)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(RED, 105))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(53).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(53))
						.givenMoves(hasSize(3))
						.willPick(taxi(WHITE, 69)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(69).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(WHITE, 69))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(69).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(138))
						.givenMoves(hasSize(3))
						.willPick(taxi(YELLOW, 124)),
				spectator().onMoveMade().givenGameState(eq(seed.round(2).over(true)
								.winning(BLUE, YELLOW, GREEN, WHITE, RED)
								.players(
										//<editor-fold defaultstate="collapsed">
										black(0).with(TX, 8, BS, 3, UG, 3, X2, 1, SC, 4),
										blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
										green(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
										red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
										white(69).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
										yellow(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenMove(eq(taxi(YELLOW, 124))),
				spectator().onGameOver().givenGameState(eq(seed.round(2).over(true)
								.winning(BLUE, YELLOW, GREEN, WHITE, RED)
								.players(
										//<editor-fold defaultstate="collapsed">
										black(0).with(TX, 8, BS, 3, UG, 3, X2, 1, SC, 4),
										blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
										green(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
										red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
										white(69).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
										yellow(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenWinners(eq(ImmutableSet.of(BLUE, YELLOW, GREEN, WHITE, RED))))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testGameWhereSomePlayersAreNeverInvolved() {
		PlayerConfiguration black = harness.newPlayer(
				black(104).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5));
		PlayerConfiguration blue = harness.newPlayer(
				blue(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration green = harness.newPlayer(
				green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration red = harness.newPlayer(
				red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration white = harness.newPlayer(
				white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration yellow = harness.newPlayer(
				yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		List<Boolean> rounds = Arrays.asList(
				false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, false, true);
		ScotlandYardGame game = createGame(rounds, defaultGraph(),
				black, blue, green, red, white, yellow);
		Spectator spectator = harness.createSpectator();
		game.registerSpectator(spectator);
		ImmutableScotlandYardView seed = ImmutableScotlandYardView.snapshot(game);
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove()
						.givenGameState(eq(seed.players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5),
								blue(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(104))
						.givenMoves(hasSize(54))
						.willPick(x2(BLACK, secret(116), bus(108))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 5),
								blue(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, secret(0), bus(0)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(1)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 0))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(2)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(BLACK, 0))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(91))
						.givenMoves(hasSize(5))
						.willPick(taxi(BLUE, 105)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 105))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(117))
						.givenMoves(hasSize(4))
						.willPick(taxi(GREEN, 108)),
				spectator().onMoveMade().givenGameState(eq(seed.current(RED).round(2).over(true)
								.winning(GREEN, BLUE, RED, YELLOW, WHITE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenMove(eq(taxi(GREEN, 108))),
				spectator().onGameOver().givenGameState(eq(seed.current(RED).round(2).over(true)
								.winning(GREEN, BLUE, RED, YELLOW, WHITE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(138).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenWinners(eq(ImmutableSet.of(GREEN, BLUE, RED, YELLOW, WHITE))))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testGameWhereMrXNeverPickedADoubleMove() {
		PlayerConfiguration black = harness.newPlayer(
				black(127).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5));
		PlayerConfiguration blue = harness.newPlayer(
				blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration green = harness.newPlayer(
				green(155).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration red = harness.newPlayer(
				red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration white = harness.newPlayer(
				white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration yellow = harness.newPlayer(
				yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		List<Boolean> rounds = Arrays.asList(
				false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, false, true);
		ScotlandYardGame game = createGame(rounds, defaultGraph(),
				black, blue, green, red, white, yellow);
		Spectator spectator = harness.createSpectator();
		game.registerSpectator(spectator);
		ImmutableScotlandYardView seed = ImmutableScotlandYardView.snapshot(game);
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove()
						.givenGameState(eq(seed.players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(155).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(127))
						.givenMoves(hasSize(165))
						.willPick(secret(BLACK, 133)),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(155).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(1)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(155).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 0))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(155).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(26))
						.givenMoves(hasSize(3))
						.willPick(taxi(BLUE, 39)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(155).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 39))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(155).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(155))
						.givenMoves(hasSize(4))
						.willPick(taxi(GREEN, 156)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 156))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(174))
						.givenMoves(hasSize(3))
						.willPick(taxi(RED, 161)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(RED, 161))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(123))
						.givenMoves(hasSize(9))
						.willPick(taxi(WHITE, 124)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(WHITE, 124))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(50))
						.givenMoves(hasSize(3))
						.willPick(taxi(YELLOW, 49)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 9, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(YELLOW, 49))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 9, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 9, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(133))
						.givenMoves(hasSize(152))
						.willPick(secret(BLACK, 157)),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 9, BS, 3, UG, 3, X2, 2, SC, 3),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(2)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 9, BS, 3, UG, 3, X2, 2, SC, 3),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 0))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 9, BS, 3, UG, 3, X2, 2, SC, 3),
								blue(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(39))
						.givenMoves(hasSize(4))
						.willPick(taxi(BLUE, 51)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 10, BS, 3, UG, 3, X2, 2, SC, 3),
								blue(51).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 51))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 10, BS, 3, UG, 3, X2, 2, SC, 3),
								blue(51).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(156).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(156))
						.givenMoves(hasSize(8))
						.willPick(bus(GREEN, 157)),
				spectator().onMoveMade().givenGameState(eq(seed.current(RED).round(2).over(true)
								.winning(GREEN, BLUE, RED, YELLOW, WHITE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 10, BS, 4, UG, 3, X2, 2, SC, 3),
								blue(51).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(157).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenMove(eq(bus(GREEN, 157))),
				spectator().onGameOver().givenGameState(eq(seed.current(RED).round(2).over(true)
								.winning(GREEN, BLUE, RED, YELLOW, WHITE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 10, BS, 4, UG, 3, X2, 2, SC, 3),
								blue(51).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(157).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenWinners(eq(ImmutableSet.of(GREEN, BLUE, RED, YELLOW, WHITE))))
				.thenAssertNoFurtherInteractions();
	}
}