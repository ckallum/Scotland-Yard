package uk.ac.bris.cs.scotlandyard.model;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Players of the {@link ScotlandYardGame} should implement this interface
 */
public interface Player {

	/**
	 * Called when the player is required to make a move as required by
	 * the @link ScotlandYardGame}
	 * 
	 * @param view a view of the current {@link ScotlandYardGame}, there are no
	 *        guarantees on immutability or thread safety so you should no hold
	 *        reference to the view beyond the scope of this method; never null
	 * @param location the location of the player
	 * @param moves valid moves the player can make; never empty and never null
	 * @param callback callback when a move is chosen from the given valid
	 *        moves, the game cannot
	 */
	void makeMove(ScotlandYardView view, int location, Set<Move> moves, Consumer<Move> callback);

}
