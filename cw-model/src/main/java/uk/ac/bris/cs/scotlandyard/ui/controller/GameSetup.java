package uk.ac.bris.cs.scotlandyard.ui.controller;

import net.kurobako.gesturefx.GesturePane;
import net.kurobako.gesturefx.GesturePane.FitMode;

import static javafx.util.Duration.millis;

import java.awt.Color;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.Subscription;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;
import uk.ac.bris.cs.fxkit.LambdaStringConverter;
import uk.ac.bris.cs.fxkit.SpinnerTableCell;
import uk.ac.bris.cs.fxkit.interpolator.DecelerateInterpolator;
import uk.ac.bris.cs.gamekit.graph.ImmutableGraph;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.ai.AI;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.StandardGame;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.ui.ColourTableCell;
import uk.ac.bris.cs.scotlandyard.ui.MapPreviewPane;
import uk.ac.bris.cs.scotlandyard.ui.ModelConfiguration;
import uk.ac.bris.cs.scotlandyard.ui.model.BoardProperty;
import uk.ac.bris.cs.scotlandyard.ui.model.ModelProperty;
import uk.ac.bris.cs.scotlandyard.ui.model.PlayerProperty;
import uk.ac.bris.cs.scotlandyard.ui.model.TicketProperty;

/**
 * A controller that creates {@link ModelProperty} for the game
 */
@BindFXML(value = "layout/GameSetup.fxml", css = "style/localsetup.css")
public final class GameSetup implements Controller {

	private static final int RANDOM = -1;
	private final ResourceManager manager;
	private final BoardProperty boardConfig;

	@FXML private VBox root;

	// players config tab
	@FXML private GridPane playerEditor;
	@FXML private TableView<PlayerProperty> playerTable;
	@FXML private TableColumn<PlayerProperty, Boolean> enabled;
	@FXML private TableColumn<PlayerProperty, Colour> colour;
	@FXML private Label playerColour;
	@FXML private TextField playerName;
	@FXML private ComboBox<Integer> playerLocation;
	@FXML private ChoiceBox<AI> playerAI;
	@FXML private TableView<TicketProperty> playerTickets;
	@FXML private TableColumn<TicketProperty, Ticket> playerTicketType;
	@FXML private TableColumn<TicketProperty, Number> playerTicketCount;
	@FXML private StackPane playerLocationContainer;

	// round config tab
	@FXML private Slider timeout;
	@FXML private Label timeoutHint;

	@FXML private Spinner<Integer> roundCount;
	@FXML private FlowPane roundConfig;

	private ObservableList<PlayerProperty> playerEntries = FXCollections
			.observableArrayList(v -> new Observable[] { v.enabledProperty(), });
	private final SimpleBooleanProperty ready = new SimpleBooleanProperty();
	private final List<AI> availableAIs;
	private final EnumSet<Features> features;

	public enum Features {
		NAME, LOCATION, AI, TICKETS
	}

	GameSetup(ResourceManager manager,
	          BoardProperty boardConfig,
	          ModelProperty config,
	          List<AI> availableAIs,
	          EnumSet<Features> features) {
		Controller.bind(this);
		this.manager = manager;
		this.boardConfig = boardConfig;
		this.availableAIs = availableAIs;
		this.features = features;

		BooleanBinding blackSelected = Bindings.isNotEmpty(
				playerEntries.filtered(PlayerProperty::mrX).filtered(PlayerProperty::enabled));
		BooleanBinding atLeastTwoPlayer = Bindings
				.size(playerEntries.filtered(PlayerProperty::enabled)).greaterThan(1);
		ready.bind(blackSelected.and(atLeastTwoPlayer));

		bindRoundConfig(config);
		bindPlayersConfig(config);
	}

	private void bindPlayersConfig(ModelProperty initialValue) {
		playerTable.setItems(playerEntries);
		playerEntries.addAll(initialValue.allPlayers());
		enabled.setCellValueFactory(p -> p.getValue().enabledProperty());
		enabled.setCellFactory(tc -> new CheckBoxTableCell<>());
		colour.setCellValueFactory(p -> p.getValue().colourProperty());
		colour.setCellFactory(tc -> new ColourTableCell<>());

		playerEntries.filtered(p -> p.ai() == null)
				.forEach(p -> p.aiProperty().set(availableAIs.get(0)));

		TableViewSelectionModel<PlayerProperty> model = playerTable.getSelectionModel();
		model.setSelectionMode(SelectionMode.SINGLE);
		playerEditor.visibleProperty().bind(model.selectedIndexProperty().isNotEqualTo(-1));
		playerTicketType.setCellValueFactory(p -> p.getValue().ticketProperty());
		playerTicketCount.setCellValueFactory(p -> p.getValue().countProperty());
		playerTicketCount.setCellFactory(cb -> new SpinnerTableCell<>(0, 100));
		model.selectedItemProperty().addListener((o, p, c) -> bindPlayerConfig(p, c));

		model.select(0);

	}

	private void bindPlayerConfig(PlayerProperty previous, PlayerProperty current) {
		if (current == null) return;
		if (previous != null) previous.observables().forEach(Property::unbind);
		selections.unsubscribe();

		FadeTransition transition = new FadeTransition(millis(250), playerEditor);
		transition.setInterpolator(new DecelerateInterpolator(2f));
		transition.setFromValue(0);
		transition.setToValue(1);
		transition.play();

		playerEditor.disableProperty().bind(current.enabledProperty().not());
		playerColour.setText(current.colour().toString() + " player");

		playerName.setText(current.name().orElse(""));
		playerName.setDisable(!features.contains(Features.NAME));
		current.nameProperty().bind(playerName.textProperty());

		playerAI.setItems(FXCollections.observableArrayList(availableAIs));
		playerAI.setConverter(LambdaStringConverter.forwardOnly("NA", AI::getName));
		playerAI.setDisable(!features.contains(Features.AI));
		if (current.ai() == null)
			playerAI.getSelectionModel().select(availableAIs.get(0));
		else playerAI.getSelectionModel().select(current.ai().orElse(null));
		current.aiProperty().bind(playerAI.getSelectionModel().selectedItemProperty());

		bindPlayerLocation(current);
		playerTickets.setItems(current.tickets());
		playerTickets.setDisable(!features.contains(Features.TICKETS));
	}

	private Subscription selections = Subscription.EMPTY;

	private void bindPlayerLocation(PlayerProperty current) {
		MapPreviewPane preview;
		Node previewNode = playerLocationContainer.lookup("#locationPreview");
		if (previewNode == null) {
			preview = new MapPreviewPane(manager);
			preview.setId("locationPreview");
			GesturePane pane = new GesturePane(preview);
			pane.scrollModeProperty().bind(boardConfig.scrollModeProperty());
			pane.setMinScale(Double.NEGATIVE_INFINITY);
			pane.setFitMode(FitMode.FIT);
			playerLocationContainer.getChildren().add(0, pane);
			Platform.runLater(() -> pane.zoomTo(0, Point2D.ZERO));
		} else {
			preview = (MapPreviewPane) previewNode;
		}

		boolean disabled = !features.contains(Features.LOCATION);
		playerLocation.setDisable(disabled);
		playerLocationContainer.setDisable(disabled);

		current.locationProperty().unbind();
		selections.unsubscribe();
		preview.reset();

		List<PlayerProperty> otherPlayers = playerEntries.stream().filter(p -> p != current)
				.filter(p -> !p.randomLocation()).filter(PlayerProperty::enabled)
				.collect(Collectors.toList());

		Set<Integer> occupiedLocation = otherPlayers.stream().map(PlayerProperty::location)
				.collect(Collectors.toSet());
		otherPlayers.forEach(p -> preview.annotate(p.location(), p.colour()));

		List<Integer> locations = new ArrayList<>(current.colour() == Colour.BLACK
				? StandardGame.MRX_LOCATIONS : StandardGame.DETECTIVE_LOCATIONS);

		List<Integer> availableLocations = new ArrayList<>(locations);
		availableLocations.removeAll(occupiedLocation);

		preview.highlight(availableLocations);

		ArrayList<Integer> selectableLocations = new ArrayList<>(availableLocations);
		selectableLocations.add(0, RANDOM);
		LambdaStringConverter<Integer> converter = LambdaStringConverter
				.forwardOnly(i -> {
					if(i == null) return "???";
					return i == RANDOM ? "Random" : i.toString();
				});
		playerLocation.setItems(FXCollections.observableList(selectableLocations));
		playerLocation.setConverter(converter);
		playerLocation.setCellFactory(cb -> {
			TextFieldListCell<Integer> cell = new TextFieldListCell<>(converter);
			EasyBind.subscribe(cell.hoverProperty(),
					hovered -> preview.annotate(cell.getItem(), current.colour()));
			return cell;
		});
		SingleSelectionModel<Integer> model = playerLocation.getSelectionModel();
		selections = EasyBind.subscribe(model.selectedItemProperty(), location -> {
			if (location != null && location != RANDOM)
				preview.annotate(location, current.colour());
		});

		model.select((Integer) current.location());
		current.locationProperty().bind(model.selectedItemProperty());
	}

	private void bindRoundConfig(ModelConfiguration initialValue) {
		// timeout
		timeoutHint.textProperty().bind(EasyBind.map(timeout.valueProperty(), Number::doubleValue)
				.map(Math::round).map(String::valueOf));
		timeout.valueProperty().setValue(initialValue.timeoutProperty().get().getSeconds());

		IntFunction<ToggleButton> mapper = i -> {
			ToggleButton button = new ToggleButton(String.valueOf(i + 1));
			button.setPrefWidth(45);
			boolean b = i >= initialValue.revealRounds().size() ? false
					: initialValue.revealRounds().get(i);
			button.setSelected(b);
			return button;
		};

		// rounds
		ObservableList<Node> roundToggles = roundConfig.getChildren();
		roundToggles.addAll(IntStream.range(0, initialValue.revealRounds().size()).mapToObj(mapper)
				.collect(Collectors.toList()));

		roundCount.setValueFactory(new IntegerSpinnerValueFactory(1, 99, roundToggles.size()));
		EasyBind.subscribe(roundCount.valueProperty(), count -> {
			int modelCount = roundToggles.size();
			if (count == 0) {
				roundToggles.clear();
			} else if (count < modelCount) {
				roundToggles.remove(count, modelCount);
			} else if (count > modelCount) {
				IntStream.range(modelCount, count).mapToObj(mapper)
						.collect(Collectors.toCollection(() -> roundToggles));
			}
		});

	}

	ModelProperty createGameConfig() {

		Set<Integer> locationSelected = playerEntries.stream().filter(PlayerProperty::enabled)
				.filter(PlayerProperty::detective).filter(p -> !p.randomLocation())
				.map(PlayerProperty::location).collect(Collectors.toSet());

		// fill in all the random locations
		ArrayList<Integer> availableLocation = new ArrayList<>(StandardGame.DETECTIVE_LOCATIONS);
		availableLocation.removeAll(locationSelected);
		Collections.shuffle(availableLocation);
		ArrayDeque<Integer> deque = new ArrayDeque<>(availableLocation);
		playerEntries.forEach(p -> p.locationProperty().unbind());
		playerEntries.filtered(PlayerProperty::randomLocation).forEach(p -> {
			if (p.mrX()) {
				p.locationProperty().set(StandardGame.MRX_LOCATIONS
						.get(new Random().nextInt(StandardGame.MRX_LOCATIONS.size())));
			} else {
				p.locationProperty().set(deque.pop());
			}
		});

		return new ModelProperty(Duration.ofSeconds(Math.round(timeout.getValue())),
				roundConfig.getChildren().stream().map(ToggleButton.class::cast)
						.map(ToggleButton::isSelected).collect(Collectors.toList()),
				playerEntries, new ImmutableGraph<>(manager.getGraph()));
	}

	ReadOnlyBooleanProperty readyProperty() {
		return ready;
	}

	@Override
	public Parent root() {
		return root;
	}

}
