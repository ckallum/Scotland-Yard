package uk.ac.bris.cs.scotlandyard.ai;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGame;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Spectator;

/**
 * A factory that produces players that can choose moves on their own, e.g. an
 * AI(CPU) player
 *
 * Players on the same side(determined by {@link Colour#isDetective()} and
 * {@link Colour#isMrX()}) will share the same factory instance so state may be
 * shared; be aware that
 * {@link Player#makeMove(ScotlandYardView, int, Set, Consumer)} is executed on
 * a separate thread
 */
public interface PlayerFactory {

	/**
	 * Create a player with the given colour<br>
	 * The returned player's
	 * {@link Player#makeMove(ScotlandYardView, int, Set, Consumer)} will be
	 * executed on a separate thread.
	 * 
	 * @param colour the colour; never null
	 * @return a player
	 */
	Player createPlayer(Colour colour);

	/**
	 * Create some spectators to be added to the game, this method will be
	 * called before {@link #ready(Visualiser, ResourceProvider)}
	 * 
	 * @param view the view of the game
	 * @return a list of spectators, defaults to empty list; not null
	 */
	default List<Spectator> createSpectators(ScotlandYardView view) {
		return Collections.emptyList();
	}

	/**
	 * Called when the game is about to start(i.e. before the first
	 * {@link ScotlandYardGame#startRotate()}) <br>
	 * 
	 * Defaults to no-op <br>
	 * Both {@code visualiser} and {@code provider} instances are guaranteed to
	 * last until {@link #finish()}
	 *
	 * @param visualiser a visualiser instance
	 * @param provider a resource provider; never null
	 */
	default void ready(Visualiser visualiser, ResourceProvider provider) {}

	/**
	 * Called when the game is finished(i.e. when
	 * {@link Spectator#onGameOver(ScotlandYardView, Set)} is called) <br>
	 * 
	 * Defaults to no-op
	 */
	default void finish() {}

}
