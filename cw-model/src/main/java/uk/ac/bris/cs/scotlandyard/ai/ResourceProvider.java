package uk.ac.bris.cs.scotlandyard.ai;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.Transport;

/**
 * Resource provider to be used within a {@link PlayerFactory}
 */
public interface ResourceProvider {

	/**
	 * Retrieves the Scotland Yard map image from memory, this image is
	 * identical to the one used in the current game
	 * 
	 * @return the map; never null
	 */
	Image getMap();

	/**
	 * Retrieves ticket images from memory, this image identical to the one used
	 * in the current game
	 * 
	 * @param ticket the ticket type
	 * @return the ticket image; never null
	 */
	Image getTicket(Ticket ticket);

	/**
	 * Retrieves an immutable copy of the game graph that is identical to the
	 * current game
	 * 
	 * @return the game graph; never null
	 */
	Graph<Integer, Transport> getGraph();

	/**
	 * Retrieves the position of node on the map image retrieved using
	 * {@link #getMap()}
	 * 
	 * @param node the node to retrieve position
	 * @return the position or null if node is not found
	 */
	Point2D coordinateAtNode(int node);

}
