package uk.ac.bris.cs.scotlandyard.model;

import java.util.Collection;

/**
 * The ScotlandYard game interface, implementations of this interface should be
 * a fully working ScotlandYard game.
 */
public interface ScotlandYardGame extends ScotlandYardView {

	/**
	 * Registers a spectator to the game. Registered spectators will be notified
	 * in no particular order.
	 *
	 * @param spectator the spectator to register; not null
	 * @throws IllegalArgumentException if the spectator to register was already
	 *         registered before
	 */
	void registerSpectator(Spectator spectator);

	/**
	 * Unregisters a already registered spectator from the game.
	 *
	 * @param spectator the spectator to unregister, not null
	 * @throws IllegalArgumentException if the spectator to unregister was not
	 *         registered before
	 */
	void unregisterSpectator(Spectator spectator);

	/**
	 * Starts the rotation. The rotation is defined as all player in the game
	 * has made a move. MrX may move twice in case of double move.
	 *
	 * @throws IllegalStateException if the game has already finished, eg:
	 *         {@link ScotlandYardView#isGameOver()} is true
	 */
	void startRotate();

	/**
	 * @return an immutable collection of all registered spectators in the game;
	 *         could be empty but never null
	 */
	Collection<Spectator> getSpectators();

}
