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
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.pass;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.secret;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.taxi;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.underground;
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
public class ModelSixPlayerPlayOutTestComplex extends ParameterisedModelTestBase {

	private TestHarness harness;
	@Before public void initialise() { harness = new TestHarness(true); }
	@After public void tearDown() { harness.forceReleaseShutdownLock(); }

	@Test
	public void testGameWhereMrXPickedConsecutiveDoubleMoves() {
		PlayerConfiguration black = harness.newPlayer(
				black(45).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5));
		PlayerConfiguration blue = harness.newPlayer(
				blue(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration green = harness.newPlayer(
				green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration red = harness.newPlayer(
				red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration white = harness.newPlayer(
				white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration yellow = harness.newPlayer(
				yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		List<Boolean> rounds = Arrays.asList(
				false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, false, true
		                                    );
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
								blue(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(45))
						.givenMoves(hasSize(138))
						.willPick(x2(BLACK, secret(46), underground(79))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 5),
								blue(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, secret(0), underground(0)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(1)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 0))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 2, X2, 1, SC, 4),
								blue(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(2)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 2, X2, 1, SC, 4),
								blue(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(underground(BLACK, 0))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 2, X2, 1, SC, 4),
								blue(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(50))
						.givenMoves(hasSize(3))
						.willPick(taxi(BLUE, 49)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 49))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(94))
						.givenMoves(hasSize(6))
						.willPick(bus(GREEN, 77)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(GREEN, 77))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(174))
						.givenMoves(hasSize(3))
						.willPick(taxi(RED, 173)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(RED, 173))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(117))
						.givenMoves(hasSize(4))
						.willPick(taxi(WHITE, 116)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(WHITE, 116))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(103).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(103))
						.givenMoves(hasSize(3))
						.willPick(taxi(YELLOW, 102)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(YELLOW, 102))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 2, X2, 1, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(79))
						.givenMoves(hasSize(264))
						.willPick(x2(BLACK, secret(111), secret(67))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 2, X2, 0, SC, 4),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, secret(111), secret(111)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 8, BS, 4, UG, 2, X2, 0, SC, 3),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(3)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 8, BS, 4, UG, 2, X2, 0, SC, 3),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 111))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 8, BS, 4, UG, 2, X2, 0, SC, 2),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(4)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 8, BS, 4, UG, 2, X2, 0, SC, 2),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 111))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 8, BS, 4, UG, 2, X2, 0, SC, 2),
								blue(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(49))
						.givenMoves(hasSize(3))
						.willPick(taxi(BLUE, 66)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 9, BS, 4, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 66))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 9, BS, 4, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(77))
						.givenMoves(hasSize(8))
						.willPick(bus(GREEN, 124)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 9, BS, 5, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(124).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(GREEN, 124))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 9, BS, 5, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(124).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								red(173).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(173))
						.givenMoves(hasSize(4))
						.willPick(taxi(RED, 160)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 10, BS, 5, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(124).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								red(160).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(RED, 160))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 10, BS, 5, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(124).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								red(160).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(116))
						.givenMoves(hasSize(8))
						.willPick(bus(WHITE, 86)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 10, BS, 6, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(124).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								red(160).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(WHITE, 86))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 10, BS, 6, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(124).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								red(160).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(102))
						.givenMoves(hasSize(5))
						.willPick(bus(YELLOW, 67)),
				spectator().onMoveMade().givenGameState(eq(seed.round(4).over(true)
								.winning(GREEN, BLUE, RED, YELLOW, WHITE).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 10, BS, 7, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(124).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								red(160).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenMove(eq(bus(YELLOW, 67))),
				spectator().onGameOver().givenGameState(eq(seed.round(4).over(true)
								.winning(GREEN, BLUE, RED, YELLOW, WHITE).players(
								//<editor-fold defaultstate="collapsed">
								black(111).with(TX, 10, BS, 7, UG, 2, X2, 0, SC, 2),
								blue(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(124).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								red(160).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenWinners(eq(ImmutableSet.of(GREEN, BLUE, RED, YELLOW, WHITE))))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testGameWhereMrXPickedConsecutiveDoubleMovesAtTimes() {
		PlayerConfiguration black = harness.newPlayer(
				black(51).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5));
		PlayerConfiguration blue = harness.newPlayer(
				blue(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration green = harness.newPlayer(
				green(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration red = harness.newPlayer(
				red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration white = harness.newPlayer(
				white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration yellow = harness.newPlayer(
				yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		List<Boolean> rounds = Arrays.asList(
				false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, false, true
		                                    );
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
								blue(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(51))
						.givenMoves(hasSize(138))
						.willPick(secret(BLACK, 52)),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(1)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 0))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(174).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(174))
						.givenMoves(hasSize(3))
						.willPick(taxi(BLUE, 161)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 161))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(26).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(26))
						.givenMoves(hasSize(3))
						.willPick(taxi(GREEN, 39)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 39))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(112))
						.givenMoves(hasSize(4))
						.willPick(taxi(RED, 100)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(RED, 100))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 3, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(29))
						.givenMoves(hasSize(9))
						.willPick(bus(WHITE, 55)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 4, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(WHITE, 55))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 4, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(117))
						.givenMoves(hasSize(4))
						.willPick(taxi(YELLOW, 116)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(YELLOW, 116))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 3, X2, 2, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(52))
						.givenMoves(hasSize(202))
						.willPick(x2(BLACK, secret(67), secret(79))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 3, X2, 1, SC, 4),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, secret(0), secret(79)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(2)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 8, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 0))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 4, UG, 3, X2, 1, SC, 2),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(3)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 4, UG, 3, X2, 1, SC, 2),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 79))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 4, UG, 3, X2, 1, SC, 2),
								blue(161).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(161))
						.givenMoves(hasSize(7))
						.willPick(bus(BLUE, 107)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 5, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(BLUE, 107))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 5, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(39).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(39))
						.givenMoves(hasSize(4))
						.willPick(taxi(GREEN, 52)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 5, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 52))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 5, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(100))
						.givenMoves(hasSize(8))
						.willPick(bus(RED, 63)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 6, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(RED, 63))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 6, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(55))
						.givenMoves(hasSize(4))
						.willPick(bus(WHITE, 89)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 7, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(WHITE, 89))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 7, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(116))
						.givenMoves(hasSize(8))
						.willPick(bus(YELLOW, 86)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 8, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(YELLOW, 86))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 8, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 8, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(79))
						.givenMoves(hasSize(221))
						.willPick(taxi(BLACK, 62)),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 8, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(4)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 8, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLACK, 79))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 8, UG, 3, X2, 1, SC, 2),
								blue(107).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(107))
						.givenMoves(hasSize(6))
						.willPick(bus(BLUE, 105)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 9, UG, 3, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(BLUE, 105))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 9, UG, 3, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(52).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(52))
						.givenMoves(hasSize(7))
						.willPick(taxi(GREEN, 69)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 9, UG, 3, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 69))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 9, UG, 3, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(63).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(63))
						.givenMoves(hasSize(8))
						.willPick(bus(RED, 65)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 10, UG, 3, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(RED, 65))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 10, UG, 3, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(89).with(TX, 11, BS, 6, UG, 4, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(89))
						.givenMoves(hasSize(7))
						.willPick(underground(WHITE, 67)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 10, UG, 4, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(underground(WHITE, 67))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 10, UG, 4, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(86).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(86))
						.givenMoves(hasSize(6))
						.willPick(bus(YELLOW, 102)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 11, UG, 4, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(YELLOW, 102))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 11, UG, 4, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 11, UG, 4, X2, 1, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(62))
						.givenMoves(hasSize(88))
						.willPick(x2(BLACK, taxi(79), underground(111))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 11, UG, 4, X2, 0, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, taxi(79), underground(79)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 11, UG, 4, X2, 0, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(5)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 11, UG, 4, X2, 0, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLACK, 79))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 11, UG, 3, X2, 0, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(6)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 11, UG, 3, X2, 0, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(underground(BLACK, 79))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 11, UG, 3, X2, 0, SC, 2),
								blue(105).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(105))
						.givenMoves(hasSize(10))
						.willPick(bus(BLUE, 87)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 12, UG, 3, X2, 0, SC, 2),
								blue(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(BLUE, 87))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 8, BS, 12, UG, 3, X2, 0, SC, 2),
								blue(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								green(69).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(69))
						.givenMoves(hasSize(4))
						.willPick(taxi(GREEN, 86)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 12, UG, 3, X2, 0, SC, 2),
								blue(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								green(86).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 86))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 12, UG, 3, X2, 0, SC, 2),
								blue(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								green(86).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(65).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(65))
						.givenMoves(hasSize(7))
						.willPick(bus(RED, 82)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 13, UG, 3, X2, 0, SC, 2),
								blue(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								green(86).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(82).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(RED, 82))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 13, UG, 3, X2, 0, SC, 2),
								blue(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								green(86).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(82).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(67).with(TX, 11, BS, 6, UG, 3, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(67))
						.givenMoves(hasSize(11))
						.willPick(underground(WHITE, 111)),
				spectator().onMoveMade().givenGameState(eq(seed.current(YELLOW).round(6).over(true)
								.winning(GREEN, BLUE, RED, YELLOW, WHITE).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 13, UG, 4, X2, 0, SC, 2),
								blue(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								green(86).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(82).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(111).with(TX, 11, BS, 6, UG, 2, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenMove(eq(underground(WHITE, 111))),
				spectator().onGameOver().givenGameState(eq(seed.current(YELLOW).round(6).over(true)
								.winning(GREEN, BLUE, RED, YELLOW, WHITE).players(
								//<editor-fold defaultstate="collapsed">
								black(79).with(TX, 9, BS, 13, UG, 4, X2, 0, SC, 2),
								blue(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								green(86).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(82).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(111).with(TX, 11, BS, 6, UG, 2, X2, 0, SC, 0),
								yellow(102).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenWinners(eq(ImmutableSet.of(GREEN, BLUE, RED, YELLOW, WHITE))))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testGameWhereSomeoneRanOutOfTickets() {
		PlayerConfiguration black = harness.newPlayer(
				black(71).with(TX, 1, BS, 1, UG, 3, X2, 2, SC, 5));
		PlayerConfiguration blue = harness.newPlayer(
				blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0));
		PlayerConfiguration green = harness.newPlayer(
				green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration red = harness.newPlayer(
				red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration white = harness.newPlayer(
				white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration yellow = harness.newPlayer(
				yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		List<Boolean> rounds = Arrays.asList(
				false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, true
				, false, false, false, false, false, true
		                                    );
		ScotlandYardGame game = createGame(rounds, defaultGraph(),
				black, blue, green, red, white, yellow);
		Spectator spectator = harness.createSpectator();
		game.registerSpectator(spectator);
		ImmutableScotlandYardView seed = ImmutableScotlandYardView.snapshot(game);
		harness.play(game).startRotationAndAssertTheseInteractionsOccurInOrder(
				player(BLACK).makeMove()
						.givenGameState(eq(seed.players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 1, BS, 1, UG, 3, X2, 2, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(71))
						.givenMoves(hasSize(81))
						.willPick(x2(BLACK, taxi(89), bus(55))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 1, BS, 1, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, taxi(0), bus(0)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 0, BS, 1, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(1)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 0, BS, 1, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLACK, 0))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 0, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(2)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 0, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(BLACK, 0))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 0, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(117))
						.givenMoves(hasSize(1))
						.willPick(pass(BLUE)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 0, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(pass(BLUE))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 0, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(141).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(141))
						.givenMoves(hasSize(4))
						.willPick(taxi(GREEN, 134)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 1, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 134))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 1, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(91).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(91))
						.givenMoves(hasSize(5))
						.willPick(taxi(RED, 105)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 2, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(RED, 105))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 2, BS, 0, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(123).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(123))
						.givenMoves(hasSize(9))
						.willPick(bus(WHITE, 124)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 2, BS, 1, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(WHITE, 124))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 2, BS, 1, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(50))
						.givenMoves(hasSize(3))
						.willPick(taxi(YELLOW, 49)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 1, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(YELLOW, 49))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 1, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 1, UG, 3, X2, 1, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(55))
						.givenMoves(hasSize(95))
						.willPick(x2(BLACK, bus(89), secret(88))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 3, BS, 1, UG, 3, X2, 0, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, bus(89), secret(89)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 3, BS, 0, UG, 3, X2, 0, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(3)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 3, BS, 0, UG, 3, X2, 0, SC, 5),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(BLACK, 89))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 3, BS, 0, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(4)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 3, BS, 0, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 89))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 3, BS, 0, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(117))
						.givenMoves(hasSize(1))
						.willPick(pass(BLUE)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 3, BS, 0, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(pass(BLUE))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 3, BS, 0, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(134).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(134))
						.givenMoves(hasSize(4))
						.willPick(taxi(GREEN, 118)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 4, BS, 0, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 118))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 4, BS, 0, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(105).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(105))
						.givenMoves(hasSize(10))
						.willPick(bus(RED, 87)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 4, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(RED, 87))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 4, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(124).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(124))
						.givenMoves(hasSize(9))
						.willPick(taxi(WHITE, 130)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 5, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(WHITE, 130))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 5, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(49))
						.givenMoves(hasSize(3))
						.willPick(taxi(YELLOW, 66)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 6, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(YELLOW, 66))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 6, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(4).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 6, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(88))
						.givenMoves(hasSize(2))
						.willPick(taxi(BLACK, 89)),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 5, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(5)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 5, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLACK, 89))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 5, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(117))
						.givenMoves(hasSize(1))
						.willPick(pass(BLUE)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 5, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(pass(BLUE))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 5, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(118).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(118))
						.givenMoves(hasSize(4))
						.willPick(taxi(GREEN, 129)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 6, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 129))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 6, BS, 1, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(87))
						.givenMoves(hasSize(5))
						.willPick(bus(RED, 86)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 6, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(RED, 86))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 6, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(130).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(130))
						.givenMoves(hasSize(3))
						.willPick(taxi(WHITE, 131)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 7, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(WHITE, 131))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 7, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(66))
						.givenMoves(hasSize(4))
						.willPick(taxi(YELLOW, 67)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 8, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(YELLOW, 67))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 8, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(5).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 8, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(89))
						.givenMoves(hasSize(15))
						.willPick(taxi(BLACK, 88)),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 7, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(6)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 7, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLACK, 89))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 7, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(117))
						.givenMoves(hasSize(1))
						.willPick(pass(BLUE)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 7, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(pass(BLUE))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 7, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(129).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(129))
						.givenMoves(hasSize(4))
						.willPick(taxi(GREEN, 135)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 8, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(135).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 135))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 8, BS, 2, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(135).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(86).with(TX, 10, BS, 6, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(86))
						.givenMoves(hasSize(7))
						.willPick(bus(RED, 87)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 8, BS, 3, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(135).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(RED, 87))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 8, BS, 3, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(135).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(131).with(TX, 9, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(131))
						.givenMoves(hasSize(3))
						.willPick(taxi(WHITE, 114)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 9, BS, 3, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(135).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(114).with(TX, 8, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(WHITE, 114))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(6).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 9, BS, 3, UG, 3, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(135).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(114).with(TX, 8, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(67).with(TX, 8, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(67))
						.givenMoves(hasSize(13))
						.willPick(underground(YELLOW, 89)),
				spectator().onMoveMade().givenGameState(eq(seed.round(6).over(true)
								.winning(BLUE, RED, WHITE, YELLOW, GREEN).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 9, BS, 3, UG, 4, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(135).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(114).with(TX, 8, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(89).with(TX, 8, BS, 8, UG, 3, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenMove(eq(underground(YELLOW, 89))),
				spectator().onGameOver().givenGameState(eq(seed.round(6).over(true)
								.winning(BLUE, RED, WHITE, YELLOW, GREEN).players(
								//<editor-fold defaultstate="collapsed">
								black(89).with(TX, 9, BS, 3, UG, 4, X2, 0, SC, 4),
								blue(117).with(TX, 0, BS, 0, UG, 1, X2, 0, SC, 0),
								green(135).with(TX, 7, BS, 8, UG, 4, X2, 0, SC, 0),
								red(87).with(TX, 10, BS, 5, UG, 4, X2, 0, SC, 0),
								white(114).with(TX, 8, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(89).with(TX, 8, BS, 8, UG, 3, X2, 0, SC, 0))
						//</editor-fold>
				                                          ))
						.givenWinners(eq(ImmutableSet.of(BLUE, RED, WHITE, YELLOW, GREEN))))
				.thenAssertNoFurtherInteractions();
	}

	@Test
	public void testGameWhereMrXWasNeverCaught() {
		PlayerConfiguration black = harness.newPlayer(
				black(51).with(TX, 4, BS, 3, UG, 3, X2, 2, SC, 5));
		PlayerConfiguration blue = harness.newPlayer(
				blue(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration green = harness.newPlayer(
				green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration red = harness.newPlayer(
				red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration white = harness.newPlayer(
				white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		PlayerConfiguration yellow = harness.newPlayer(
				yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0));
		List<Boolean> rounds = Arrays.asList(
				false, false, true
		                                    );
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
								blue(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(51))
						.givenMoves(hasSize(138))
						.willPick(x2(BLACK, secret(67), bus(102))),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 5),
								blue(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(x2(BLACK, secret(0), bus(0)))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(1)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(1).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 0))),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(2)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(BLACK, 0))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 4, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(112).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(112))
						.givenMoves(hasSize(4))
						.willPick(taxi(BLUE, 100)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 100))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 2, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(94).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(94))
						.givenMoves(hasSize(6))
						.willPick(bus(GREEN, 77)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(GREEN, 77))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 5, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(50).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(50))
						.givenMoves(hasSize(3))
						.willPick(taxi(RED, 49)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(RED, 49))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 3, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(29).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(29))
						.givenMoves(hasSize(9))
						.willPick(bus(WHITE, 55)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 4, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(WHITE, 55))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 6, BS, 4, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(117).with(TX, 11, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(117))
						.givenMoves(hasSize(4))
						.willPick(taxi(YELLOW, 108)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 4, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(YELLOW, 108))),
				spectator().onRotationComplete()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 4, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.respondWith(startRotate(game)),
				player(BLACK).makeMove()
						.givenGameState(eq(seed.round(2).players(
								//<editor-fold defaultstate="collapsed">
								black(0).with(TX, 7, BS, 4, UG, 3, X2, 1, SC, 4),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(102))
						.givenMoves(hasSize(12))
						.willPick(secret(BLACK, 127)),
				spectator().onRoundStarted()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 7, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenRound(eq(3)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 7, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(secret(BLACK, 127))),
				player(BLUE).makeMove()
						.givenGameState(eq(seed.current(BLUE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 7, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(100).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(100))
						.givenMoves(hasSize(8))
						.willPick(taxi(BLUE, 113)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(GREEN).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 8, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(BLUE, 113))),
				player(GREEN).makeMove()
						.givenGameState(eq(seed.current(GREEN).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 8, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(77).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(77))
						.givenMoves(hasSize(8))
						.willPick(taxi(GREEN, 96)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(RED).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 9, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(96).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(GREEN, 96))),
				player(RED).makeMove()
						.givenGameState(eq(seed.current(RED).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 9, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(96).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(49).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(49))
						.givenMoves(hasSize(3))
						.willPick(taxi(RED, 66)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(WHITE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 10, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(96).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(RED, 66))),
				player(WHITE).makeMove()
						.givenGameState(eq(seed.current(WHITE).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 10, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(96).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(55).with(TX, 11, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(55))
						.givenMoves(hasSize(4))
						.willPick(taxi(WHITE, 54)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.current(YELLOW).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 11, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(96).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(54).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(taxi(WHITE, 54))),
				player(YELLOW).makeMove()
						.givenGameState(eq(seed.current(YELLOW).round(3).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 11, BS, 4, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(96).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(54).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(108).with(TX, 10, BS, 8, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenLocation(eq(108))
						.givenMoves(hasSize(6))
						.willPick(bus(YELLOW, 116)),
				spectator().onMoveMade()
						.givenGameState(eq(seed.round(3).over(true).winning(BLACK).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 11, BS, 5, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(96).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(54).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenMove(eq(bus(YELLOW, 116))),
				spectator().onGameOver()
						.givenGameState(eq(seed.round(3).over(true).winning(BLACK).players(
								//<editor-fold defaultstate="collapsed">
								black(127).with(TX, 11, BS, 5, UG, 3, X2, 1, SC, 3),
								blue(113).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								green(96).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								red(66).with(TX, 9, BS, 8, UG, 4, X2, 0, SC, 0),
								white(54).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0),
								yellow(116).with(TX, 10, BS, 7, UG, 4, X2, 0, SC, 0))
								//</editor-fold>
						                  ))
						.givenWinners(eq(ImmutableSet.of(BLACK))))
				.thenAssertNoFurtherInteractions();
	}

}