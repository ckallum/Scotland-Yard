package uk.ac.bris.cs.scotlandyard.ai;

import javafx.scene.layout.Pane;

/**
 * A visualiser provides means for a {@link PlayerFactory} to render visual cues
 * while the game is progressing
 */
public interface Visualiser {

	/**
	 * A surface to draw on, must be called in JavaFX's UI thread.
	 * {@link javafx.application.Platform#runLater(Runnable)} could be used when
	 * calling from a different thread.
	 * 
	 * @return the surface; never null
	 */
	Pane surface();

}
