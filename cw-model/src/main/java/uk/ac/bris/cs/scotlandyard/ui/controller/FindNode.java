package uk.ac.bris.cs.scotlandyard.ui.controller;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.fxmisc.easybind.EasyBind;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import net.kurobako.gesturefx.GesturePane;
import net.kurobako.gesturefx.GesturePane.ScrollMode;

import javafx.beans.binding.When;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;
import uk.ac.bris.cs.fxkit.interpolator.DecelerateInterpolator;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.ui.MapPreviewPane;
import uk.ac.bris.cs.scotlandyard.ui.model.BoardProperty;

@BindFXML(value = "layout/FindNode.fxml")
public final class FindNode implements Controller {

	private static final Joiner JOINER = Joiner.on(", ").skipNulls();
	private static final Splitter SPLITTER = Splitter.on(' ').omitEmptyStrings().trimResults();

	@FXML private VBox root;
	@FXML private TextField search;
	@FXML private Button reset;
	@FXML private Label message;
	@FXML private StackPane mapContainer;

	FindNode(BoardProperty config, Stage parent, ResourceManager manager) {
		Controller.bind(this);
		MapPreviewPane pane = new MapPreviewPane(manager);
		GesturePane gesturePane = new GesturePane(pane);
		gesturePane.scrollModeProperty().bind(config.scrollModeProperty());
		mapContainer.getChildren().add(gesturePane);
		EasyBind.subscribe(search.textProperty(), s -> {
			List<String> items = SPLITTER.splitToList(s);
			Set<Integer> highlights = new HashSet<>();
			List<String> failed = new ArrayList<>();
			for (String item : items) {
				Optional<Integer> optional = maybeInteger(item)
						.flatMap(i -> manager.getGraph().containsNode(i) ? of(i) : empty());
				optional.ifPresent(highlights::add);
				if (!optional.isPresent()) failed.add(item);
			}
			pane.highlight(highlights);
			message.setText(!failed.isEmpty() ? "Invalid node: " + JOINER.join(failed)
					: "Highlighting " + highlights.size() + " node(s)");
			search.setStyle("-fx-background-color: " + (!failed.isEmpty() ? "#bc6a00" :
					"#00aa48"));
			if (!highlights.isEmpty()) {
				gesturePane.animate(Duration.millis(300))
						.interpolateWith(DecelerateInterpolator.DEFAULT)
						.centreOn(findCentre(highlights.stream()
								.map(manager::coordinateAtNode)
								.collect(toList())));
//				gesturePane.translateTo(
//						findCentre(highlights.stream()
//								.map(manager::coordinateAtNode)
//								.collect(toList())),
//						Duration.millis(300), () -> {});
			}
		});

		reset.setOnAction(e -> gesturePane.zoomTo(0, Point2D.ZERO));
	}


	// TODO isn't this just some sort of fold?
	private static Point2D findCentre(Collection<Point2D> points) {
		double minX = Integer.MAX_VALUE;
		double maxX = Integer.MIN_VALUE;
		double minY = Integer.MAX_VALUE;
		double maxY = Integer.MIN_VALUE;
		for (Point2D item : points) {
			double x = item.getX();
			double y = item.getY();
			maxX = Math.max(x, maxX);
			minX = Math.min(x, minX);
			maxY = Math.max(y, maxY);
			minY = Math.min(y, minY);
		}
		return new Point2D(minX, minY).midpoint(maxX, maxY);
	}


	private static Optional<Integer> maybeInteger(String string) {
		try {
			return of(Integer.valueOf(string));
		} catch (NumberFormatException ignored) {
			return empty();
		}
	}

	@Override
	public Parent root() {
		return root;
	}
}
