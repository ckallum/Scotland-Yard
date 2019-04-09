package uk.ac.bris.cs.scotlandyard;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.ImmutableGraph;
import uk.ac.bris.cs.scotlandyard.ai.ResourceProvider;
import uk.ac.bris.cs.scotlandyard.model.StandardGame;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.Transport;

/**
 * Manager for static resources such as game map and graph
 */
public final class ResourceManager implements ResourceProvider {

	private final Point2D TOP_LEFT_OFFSET = new Point2D(60, 60);

	public enum ImageResource {
		ICON, MAP, UOB_LOGO
	}

	private Map<ImageResource, Image> imageResources;
	private Map<Ticket, Image> ticketResources;
	private Map<Integer, Entry<Integer, Integer>> mapCoordinates;
	private Graph<Integer, Transport> graph;

	/**
	 * Loads all resources into memory <br>
	 * This should be called before any resources are required
	 *
	 * @throws IOException if any of the resources cannot be found
	 */
	public void loadAllResources() throws IOException {

		// shared images
		imageResources = ImmutableMap.of(
				ImageResource.MAP, loadImage("/map_large.png"),
				ImageResource.UOB_LOGO, loadImage("/uob_logo.png"),
				ImageResource.ICON, loadImage("/icon.png"));

		ticketResources = ImmutableMap.copyOf(Stream.of(Ticket.values()).collect(toMap(
				identity(),
				ticket -> loadImage(format("/tickets/%s.png", ticket.name().toLowerCase())))));

		mapCoordinates = ImmutableMap.copyOf(StandardGame.pngMapPositionEntries());
		graph = new ImmutableGraph<>(StandardGame.standardGraph());
	}

	private static Image loadImage(String path) {
		return new Image(path, -1, -1, true, true, false);
	}

	public Image getImage(ImageResource resource) {
		return imageResources.get(resource);
	}

	@Override
	public Image getMap() {
		return getImage(ImageResource.MAP);
	}

	@Override
	public Image getTicket(Ticket ticket) {
		return ticketResources.get(ticket);
	}

	@Override
	public Graph<Integer, Transport> getGraph() {
		return graph;
	}

	@Override
	public Point2D coordinateAtNode(int node) {
		Entry<Integer, Integer> entry = mapCoordinates.get(node);
		return new Point2D(entry.getKey(), entry.getValue());
	}

}
