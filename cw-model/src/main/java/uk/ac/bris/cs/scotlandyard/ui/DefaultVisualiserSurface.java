package uk.ac.bris.cs.scotlandyard.ui;

import org.fxmisc.easybind.EasyBind;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import uk.ac.bris.cs.scotlandyard.ai.AI;
import uk.ac.bris.cs.scotlandyard.ai.AIPool.VisualiserSurface;

/**
 * An implementation of the visualiser surface that AIs can use during game
 */
public final class DefaultVisualiserSurface implements VisualiserSurface {

	private final Stage parent;
	private final Pane mapPane;

	private VisualiserSurface provider;

	public DefaultVisualiserSurface(Stage parent, Pane mapPane) {
		this.parent = parent;
		this.mapPane = mapPane;
	}

	@Override
	public Pane onCreate(AI ai) {
		switch (ai.getVisualiserType()) {
		case MAP_OVERLAP:
			provider = new PaneSurface(mapPane);
			break;
		case WINDOWED:
			provider = new ToolWindowSurface(parent);
			break;
		case NONE:
			provider = new NoopSurface();
			break;
		}
		return provider.onCreate(ai);
	}

	@Override
	public void onDestroy() {
			if (provider != null) provider.onDestroy();
	}

	private static class PaneSurface implements VisualiserSurface {

		private final Pane pane;

		PaneSurface(Pane pane) {
			this.pane = pane;
		}

		@Override
		public Pane onCreate(AI ai) {
			return pane;
		}

		@Override
		public void onDestroy() {
			pane.getChildren().clear();
		}
	}

	private static class NoopSurface extends PaneSurface {

		public NoopSurface() {
			super(new Pane());
		}

		@Override
		public Pane onCreate(AI ai) {
			Pane pane = super.onCreate(ai);
			pane.setManaged(false);
			pane.setVisible(false);
			return pane;
		}
	}

	private static class ToolWindowSurface implements VisualiserSurface {

		private final Stage stage;

		private ToolWindowSurface(Window owner) {
			this.stage = new Stage(StageStyle.UTILITY);
			this.stage.initOwner(owner);
		}

		@Override
		public Pane onCreate(AI ai) {
			Pane root = new Pane();
			stage.setTitle(ai.getName());
			stage.setScene(new Scene(root));

			EasyBind.subscribe(stage.iconifiedProperty(), v -> {
				if (!v)
					stage.show();
				else stage.hide();
			});
			stage.setOnCloseRequest(e -> {
				Alert warn = new Alert(AlertType.WARNING);
				warn.setTitle("Visualiser locked");
				warn.setContentText("Visualiser window cannot be closed as the AI depends on it");
				warn.showAndWait();
				e.consume();
			});
			stage.show();
			Platform.runLater(stage::sizeToScene);
			return root;
		}

		@Override
		public void onDestroy() {
			stage.close();
		}
	}

}
