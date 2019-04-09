package uk.ac.bris.cs.scotlandyard.ui;

import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Spectator;

/**
 * Classes wishing to be notified of game changes should implement this
 * interface, all methods have a default implementation of no-op
 *
 */
public interface GameControl extends Spectator {

	default void onGameAttach(ScotlandYardView view, ModelConfiguration configuration) {}

	default void onGameDetached() {}

}
