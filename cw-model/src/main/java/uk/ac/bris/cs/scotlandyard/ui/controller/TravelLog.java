package uk.ac.bris.cs.scotlandyard.ui.controller;

import java.util.List;

import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.model.DoubleMove;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.MoveVisitor;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.TicketMove;
import uk.ac.bris.cs.scotlandyard.ui.GameControl;
import uk.ac.bris.cs.scotlandyard.ui.ModelConfiguration;
import uk.ac.bris.cs.scotlandyard.ui.model.BoardProperty;

/**
 * Controller for travel log the records Mr.X move
 */
@BindFXML("layout/TravelLog.fxml")
public final class TravelLog implements Controller, GameControl {

	private static final SimpleStringProperty EMPTY_STRING = new SimpleStringProperty("");

	@FXML private StackPane root;
	@FXML private TableView<Entry> logTable;
	@FXML private TableColumn<Entry, Number> logRound;
	@FXML private TableColumn<Entry, Ticket> logTicket;
	@FXML private TableColumn<Entry, String> logLocation;

	private final ResourceManager manager;
	private final ObservableList<Entry> entries = FXCollections.observableArrayList();

	TravelLog(ResourceManager manager, BoardProperty config) {
		Controller.bind(this);
		this.manager = manager;
		logRound.setCellValueFactory(param -> param.getValue().round);
		logTicket.setCellValueFactory(param -> param.getValue().ticket);
		logTicket.setCellFactory(param -> new TicketTableCell(manager));
		logLocation.setCellValueFactory(param -> {
			Entry value = param.getValue();
			return new When(value.reveal).then(value.location.asString()).otherwise(EMPTY_STRING);
		});

		root.managedProperty().bind(root.visibleProperty());
		logTable.setItems(entries);
	}

	@Override
	public void onGameAttach(ScotlandYardView view, ModelConfiguration configuration) {
		List<Boolean> rounds = view.getRounds();
		for (int i = 0; i < rounds.size(); i++)
			entries.add(new Entry(i + 1, rounds.get(i)));
	}

	@Override
	public void onGameDetached() {
		entries.clear();
	}

	@Override
	public void onMoveMade(ScotlandYardView view, Move move) {
		if (move.colour().isDetective()) return;
		move.visit(new MoveVisitor() {
			@Override
			public void visit(TicketMove move) {
				setTicket(move, view.getCurrentRound());
			}

			@Override
			public void visit(DoubleMove move) {
				// setTicket(move.firstMove(), view.getCurrentRound() );
				// setTicket(move.secondMove(), view.getCurrentRound()+1);
			}

			private void setTicket(TicketMove move, int round) {
				Entry entry = entries.get(round - 1);
				entry.ticket.set(move.ticket());
				entry.location.set(move.destination());
			}
		});
	}

	@Override
	public Parent root() {
		return root;
	}

	public static class Entry {

		private final IntegerProperty round = new SimpleIntegerProperty();
		private final ObjectProperty<Ticket> ticket = new SimpleObjectProperty<>();
		private final IntegerProperty location = new SimpleIntegerProperty();
		private final BooleanProperty reveal = new SimpleBooleanProperty();

		Entry(int round, boolean reveal) {
			this.round.set(round);
			this.reveal.set(reveal);
		}

	}

	private static class TicketTableCell extends TableCell<Entry, Ticket> {

		private final ImageView imageView = new ImageView();
		private final ResourceManager manager;

		TicketTableCell(ResourceManager manager) {
			this.manager = manager;
			imageView.setFitWidth(50);
			// imageView.setFitHeight(10);
			imageView.setPreserveRatio(true);
			setGraphic(imageView);
		}

		@Override
		protected void updateItem(Ticket item, boolean empty) {
			if (!empty) imageView.setImage(manager.getTicket(item));
			super.updateItem(item, empty);
		}

	}
}
