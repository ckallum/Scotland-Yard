package uk.ac.bris.cs.scotlandyard.model;

import java.util.Set;

public interface Spectator {

	/**
	 * Called when a player has finished making a move. At this point in time
	 * the ticket would have been used (or transferred to MrX)
	 *
	 * @param view the view of the game, never null
	 * @param move the move commenced, never null
	 */
	default void onMoveMade(ScotlandYardView view, Move move) {}

	/**
	 * Called when a new round is started. see
	 * {@link ScotlandYardView#getCurrentRound()} for definition of a round.
	 *
	 * @param view the view of the game, never null
	 * @param round the started round
	 */
	default void onRoundStarted(ScotlandYardView view, int round) {}

	/**
	 * Called whe a rotation is complete
	 *
	 * @param view the view of the game, never null
	 */
	default void onRotationComplete(ScotlandYardView view) {}

	/**
	 * Called when the game is over
	 *
	 * @param view the view of the game, never null
	 * @param winningPlayers winners, never empty and never null
	 */
	default void onGameOver(ScotlandYardView view, Set<Colour> winningPlayers) {}

}
