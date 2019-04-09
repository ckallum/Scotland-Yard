package uk.ac.bris.cs.scotlandyard.ui.controller;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.ai.AI;
import uk.ac.bris.cs.scotlandyard.ui.controller.GameSetup.Features;
import uk.ac.bris.cs.scotlandyard.ui.model.BoardProperty;
import uk.ac.bris.cs.scotlandyard.ui.model.ModelProperty;

@BindFXML(value = "layout/StartScreen.fxml", css = "style/startscreen.css")
public final class StartScreen implements Controller {

	@FXML private VBox root;
	@FXML private Tab gameSetup;
	@FXML private Tab savedConfigs;
	@FXML private Tab savedGames;
	@FXML private Button start;

	private final ResourceManager manager;
	private final BoardProperty config;

	StartScreen(ResourceManager manager, BoardProperty config,
	            Consumer<ModelProperty> consumer) {
		this.manager = manager;
		this.config = config;
		Controller.bind(this);

		ArrayList<AI> ais = new ArrayList<>(AI.scanClasspath());
		// add null for no ai(user select)
		ais.add(0, null);

		GameSetup setupController = new GameSetup(this.manager, this.config,
				ModelProperty.createDefault(manager), ais, EnumSet.allOf(Features.class));

		gameSetup.setContent(setupController.root());

		// TODO presets and saved games...
		savedConfigs.setDisable(true);
		// savedConfigs.setContent(new SavedConfigsController(consumer).root());
		savedGames.setDisable(true);
		// savedGames.setContent(new SavedGamesController(consumer).root());

		start.disableProperty().bind(setupController.readyProperty().not());
		start.setOnAction(e -> {
			ModelProperty property = setupController.createGameConfig();
			consumer.accept(property);
		});

	}

	@Override
	public Parent root() {
		return root;
	}
}
