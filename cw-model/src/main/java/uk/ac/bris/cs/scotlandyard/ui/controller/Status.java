package uk.ac.bris.cs.scotlandyard.ui.controller;

import java.util.List;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.ui.GameControl;
import uk.ac.bris.cs.scotlandyard.ui.ModelConfiguration;
import uk.ac.bris.cs.scotlandyard.ui.model.BoardProperty;

/**
 * Controller for status bar
 */
@BindFXML("layout/Status.fxml")
public final class Status implements Controller, GameControl {

	@FXML private ToolBar root;
	@FXML private Label round;
	@FXML private Label player;
	@FXML private Label time;
	@FXML private Label status;
	@FXML private Slider volume;

	private final ResourceManager manager;

	Status(ResourceManager manager, BoardProperty config) {
		Controller.bind(this);
		this.manager = manager;
	}

	@Override
	public void onGameAttach(ScotlandYardView view, ModelConfiguration configuration) {
		bindView(view);
	}

	@Override
	public void onRoundStarted(ScotlandYardView view, int round) {
		bindView(view);
	}

	@Override
	public void onMoveMade(ScotlandYardView view, Move move) {
		bindView(view);
	}

	private void bindView(ScotlandYardView view) {
		int round = view.getCurrentRound();
		this.round.setText(round == 0 ? "N/A" : round + " of " + view.getRounds().size());
		this.player.setText(view.getCurrentPlayer().toString());
		this.status.setText(String.format("Waiting move(%s)", view.getCurrentPlayer()));
	}

	@Override
	public void onGameOver(ScotlandYardView view, Set<Colour> winningPlayers) {
		status.setText("Game completed, winning player:" + view.getWinningPlayers());
	}

	@Override
	public Parent root() {
		return root;
	}
}
