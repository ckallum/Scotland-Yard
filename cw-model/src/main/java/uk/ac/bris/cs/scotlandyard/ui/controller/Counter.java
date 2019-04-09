package uk.ac.bris.cs.scotlandyard.ui.controller;

import java.net.URL;
import java.util.OptionalInt;
import java.util.ResourceBundle;

import org.fxmisc.easybind.EasyBind;

import io.atlassian.fugue.Option;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;
import uk.ac.bris.cs.fxkit.interpolator.DecelerateInterpolator;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.TicketMove;
import uk.ac.bris.cs.scotlandyard.ui.Utils;

@BindFXML("layout/Counter.fxml")
public final class Counter implements Controller {

	private static final double DEFAULT_OPACITY = 0.85;
	private static final double HOVER_OPACITY = 0.25;

	@FXML private VBox root;
	@FXML private Circle piece;

	private final ResourceManager manager;
	private final BooleanProperty animation;
	private final SimpleIntegerProperty locationProperty = new SimpleIntegerProperty();

	Counter(ResourceManager manager,
			BooleanProperty animation,
			Colour colour,
			int location) {
		this.manager = manager;
		this.animation = animation;
		this.locationProperty.set(location);
		Controller.bind(this);
		// EasyBind.subscribe(locationProperty, n ->
		// Platform.runLater(this::updateLocation));
		Color color = Color.valueOf(colour.name()).saturate().deriveColor(0, 1.0, 1.0 / 0.4, 1.0);
		// piece.visibleProperty().bind(visibleProperty);
		piece.setFill(color);
		piece.setOpacity(DEFAULT_OPACITY);
		piece.setOnMouseEntered(e -> Utils.fadeTo(piece, HOVER_OPACITY));
		piece.setOnMouseExited(e -> Utils.fadeTo(piece, DEFAULT_OPACITY));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		EasyBind.subscribe(root.layoutBoundsProperty(), b -> updateLocation());
	}

	void updateLocation() {
		// TODO due to timing differences, counter translation might not be
		// compensated relative to itself in some cases; this could be fixed by
		// binding to width and height properties of the current root
		piece.setVisible(false);
		location().ifPresent(location -> {
			piece.setVisible(true);
			Point2D node = positionAtNode(location);
			root.setTranslateX(node.getX());
			root.setTranslateY(node.getY());
		});
	}

	void animateTicketMove(TicketMove ticket, Option<Runnable> callback) {
		if (!animation.get()
				|| ticket.destination() == 0
				|| !location().isPresent()
				|| ticket.destination() == location().orElse(0)) {
			callback.forEach(Runnable::run);
			return;
		}
		Point2D from = positionAtNode(location().getAsInt());
		Point2D to = positionAtNode(ticket.destination());
		TranslateTransition tt = new TranslateTransition(Duration.millis(250), root);
		tt.setInterpolator(new DecelerateInterpolator(2f));
		tt.setFromX(from.getX());
		tt.setToX(from.getY());
		tt.setToX(to.getX());
		tt.setToY(to.getY());
		tt.play();
		tt.setOnFinished(e -> Platform.runLater(() -> callback.forEach(Runnable::run)));
	}

	OptionalInt location() {
		int location = locationProperty.get();
		return location == 0 ? OptionalInt.empty() : OptionalInt.of(location);
	}

	public void location(int location) {
		locationProperty.set(location);
	}

	private Point2D positionAtNode(int node) {
		// offset relative to the centre
		return manager.coordinateAtNode(node)
				.subtract(root().getLayoutBounds().getWidth() / 2,
						root().getLayoutBounds().getHeight() / 2);
	}

	@Override
	public Parent root() {
		return root;
	}
}
