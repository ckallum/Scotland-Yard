package uk.ac.bris.cs.scotlandyard.ui;

import java.time.Duration;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.Transport;
import uk.ac.bris.cs.scotlandyard.ui.model.PlayerProperty;

public interface ModelConfiguration {

	ObjectProperty<Duration> timeoutProperty();

	ObservableList<Boolean> revealRounds();

	ObjectProperty<Graph<Integer, Transport>> graphProperty();

	ObservableList<PlayerProperty> allPlayers();

	ObservableList<PlayerProperty> players();
}
