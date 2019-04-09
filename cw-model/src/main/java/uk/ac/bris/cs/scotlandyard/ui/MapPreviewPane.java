package uk.ac.bris.cs.scotlandyard.ui;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import uk.ac.bris.cs.fxkit.interpolator.DecelerateInterpolator;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.ResourceManager.ImageResource;
import uk.ac.bris.cs.scotlandyard.model.Colour;

/**
 * Lightweight map with annotation, preview and circling capabilities
 */
public class MapPreviewPane extends Pane {

	private final Pane annotations = new Pane();
	private final Pane mask = new Pane();
	private final ResourceManager manager;

	public MapPreviewPane(ResourceManager manager) {
		this.manager = manager;
		ImageView mapView = new ImageView();
		Pane shadow = new Pane();
		getChildren().addAll(mapView, shadow, annotations);
		Image image = manager.getImage(ImageResource.MAP);
		mapView.setImage(image);
		shadow.setStyle("-fx-background-color: rgba(0,0, 0, 0.5)");
		setMinSize(image.getWidth(), image.getHeight());
//		resize(image.getWidth(), image.getHeight());
		shadow.setPrefSize(image.getWidth(), image.getHeight());
		annotations.setPrefSize(image.getWidth(), image.getHeight());
		mask.setBlendMode(BlendMode.OVERLAY);
		shadow.getChildren().add(mask);
	}

	public void reset() {
		clearAnnotations();
		clearHighlights();
	}

	private void clearAnnotations() {
		playerMap.clear();
		annotations.getChildren().clear();
	}

	private final Map<Colour, Node> playerMap = new EnumMap<>(Colour.class);

	public void annotate(Integer location, Colour colour) {
		if (location == null || location == -1) {

			Node node = playerMap.get(colour);
			if (node != null) {
				annotations.getChildren().remove(node);
				playerMap.remove(colour);
			}
			return;
		}

		Node node = playerMap.computeIfAbsent(colour, c -> {
			Circle circle = new Circle(30);
			circle.setStroke(Color.WHITE);
			circle.setStrokeWidth(5);
			circle.setFill(Color.valueOf(colour.name()));
			annotations.getChildren().addAll(circle);
			return circle;
		});
		Point2D point = manager.coordinateAtNode(location);

		if (Utils.translate(node).equals(Point2D.ZERO)) {
			Utils.translate(node, point);
			return;
		}

		if (point.distance(node.getTranslateX(), node.getTranslateY()) > 1) {
			TranslateTransition t = new TranslateTransition(Duration.millis(120), node);
			t.setInterpolator(DecelerateInterpolator.DEFAULT);
			t.setToX(point.getX());
			t.setToY(point.getY());
			t.play();
		} else {
			Utils.translate(node, point);
		}

	}

	private void clearHighlights() {
		mask.setVisible(false);
	}

	public void highlight(Collection<Integer> locations) {
		highlight(Colour.WHITE, locations);
	}

	public void circle(int location, Colour colour) {
		Point2D point = manager.coordinateAtNode(location);
		Circle circle = new Circle(45);
		circle.setFill(Color.TRANSPARENT);
		circle.setStroke(Color.valueOf(colour.name()));
		circle.setStrokeWidth(8);
		circle.setTranslateX(point.getX());
		circle.setTranslateY(point.getY());
		circle.setOpacity(0.8);
		annotations.getChildren().add(circle);
	}

	private void highlight(Colour colour, Collection<Integer> locations) {
		if (locations.isEmpty()) {
			clearHighlights();
			return;
		}

		mask.getChildren().clear();
		for (Integer location : locations) {
			Point2D point = manager.coordinateAtNode(location);
			Circle circle = new Circle(35);
			circle.setFill(Color.valueOf(colour.name()));
			circle.setTranslateX(point.getX());
			circle.setTranslateY(point.getY());
			circle.setOpacity(0.8);
			circle.setStyle("-fx-effect: dropshadow(gaussian, white, 50, 0.5, 0, 0)");
			mask.getChildren().add(circle);
		}

		mask.setVisible(true);

	}
}
