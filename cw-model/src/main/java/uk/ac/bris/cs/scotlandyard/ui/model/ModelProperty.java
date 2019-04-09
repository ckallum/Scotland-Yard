package uk.ac.bris.cs.scotlandyard.ui.model;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.time.Duration;
import java.util.List;

import com.google.common.base.MoreObjects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.ImmutableGraph;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.StandardGame;
import uk.ac.bris.cs.scotlandyard.model.Transport;
import uk.ac.bris.cs.scotlandyard.ui.ModelConfiguration;

public class ModelProperty implements ModelConfiguration {

	private final ObjectProperty<Duration> timeout = new SimpleObjectProperty<>();
	private final ObservableList<Boolean> revealRounds = FXCollections.observableArrayList();
	private final ObservableList<PlayerProperty> players = FXCollections.observableArrayList();
	private final ObjectProperty<Graph<Integer, Transport>> graph = new SimpleObjectProperty<>();

	public ModelProperty(Duration timeout, List<Boolean> revealRounds, List<PlayerProperty> players,
			Graph<Integer, Transport> graph) {
		this.timeout.set(timeout);
		this.revealRounds.addAll(revealRounds);
		this.players.addAll(players);
		this.graph.set(graph);
	}

	public static ModelProperty createDefault(ResourceManager manager) {
		return new ModelProperty(Duration.ofMinutes(1), StandardGame.ROUNDS,
				of(Colour.values()).map(PlayerProperty::new).collect(toList()),
				new ImmutableGraph<>(manager.getGraph()));
	}

	@Override
	public ObjectProperty<Duration> timeoutProperty() {
		return timeout;
	}

	@Override
	public ObservableList<Boolean> revealRounds() {
		return revealRounds;
	}

	@Override
	public ObservableList<PlayerProperty> allPlayers() {
		return players;
	}

	@Override
	public ObservableList<PlayerProperty> players() {
		return players.filtered(PlayerProperty::enabled);
	}

	@Override
	public ObjectProperty<Graph<Integer, Transport>> graphProperty() {
		return graph;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("timeout", timeout)
				.add("revealRounds", revealRounds).add("players", players).toString();
	}

}
