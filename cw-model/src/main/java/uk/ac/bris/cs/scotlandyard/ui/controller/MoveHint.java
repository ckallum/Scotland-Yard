package uk.ac.bris.cs.scotlandyard.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.fxmisc.easybind.EasyBind;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;
import uk.ac.bris.cs.scotlandyard.ResourceManager;
import uk.ac.bris.cs.scotlandyard.model.DoubleMove;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.MoveVisitor;
import uk.ac.bris.cs.scotlandyard.model.TicketMove;
import uk.ac.bris.cs.scotlandyard.ui.Utils;

/**
 * Controller for move hints with highlighting
 */
@BindFXML("layout/MoveHint.fxml")
final class MoveHint implements Controller {

	private static final String HIGHLIGHTED = "highlighted";

	private final IntegerProperty node = new SimpleIntegerProperty();
	private final BooleanProperty highlight = new SimpleBooleanProperty();

	private final ResourceManager manager;
	private final Board board;

	private final List<Move> moves = new ArrayList<>();

	@FXML private Pane root;
	@FXML private Circle piece;
	private boolean discarded = false;

	MoveHint(ResourceManager manager, Board board, int target, Consumer<Move> moveConsumer) {
		Controller.bind(this);
		this.manager = manager;
		this.board = board;
		this.node.set(target);

		EasyBind.subscribe(highlight, v -> {
			ObservableList<String> styles = piece.getStyleClass();
			Function<String, Boolean> function = (v ? styles::add : styles::remove);
			function.apply(HIGHLIGHTED);
		});

		if (moveConsumer != null) setupMoveOptions(moveConsumer);

		piece.setOnMouseEntered(e -> {
			Utils.scaleTo(piece, 1.25);
		});

		piece.setOnMouseExited(e -> {
			Utils.scaleTo(piece, 1);
		});

		// Platform.runLater(() -> {
		Point2D location = board.coordinateAtNode(node.get());
		piece.setTranslateX(location.getX());
		piece.setTranslateY(location.getY());
		// });
	}

	private ContextMenu lastMenu = null;
	private void setupMoveOptions(Consumer<Move> moveConsumer) {
		piece.setOnMouseClicked(e -> {
			if (lastMenu != null) lastMenu.hide();
			lastMenu = new ContextMenu();
			for (Move move : moves) {
				MenuItem item = new MenuItem();
				Node graphic = createOption(move);
				item.setGraphic(graphic);

				EasyBind.subscribe(graphic.hoverProperty(), hover -> {
					if (discarded) return;
					move.visit(new TicketVisitor() {
						@Override
						public void visit(TicketMove move) {
							board.hintAt(move.destination()).highlight.set(hover);
						}
					});
				});
				item.setOnAction(a -> moveConsumer.accept(move));
				lastMenu.getItems().add(item);
				lastMenu.setOpacity(0.8);

			}
			Point2D p = piece.localToScreen(piece.getCenterX(), piece.getCenterY());
			lastMenu.show(piece, p.getX(), p.getY());
			lastMenu.setOnHidden(a -> lastMenu = null);
		});
	}

	private Node createOption(Move move) {
		if (move instanceof TicketMove) {
			TicketMove mt = (TicketMove) move;
			return new ImageView(manager.getTicket(mt.ticket()));
		} else if (move instanceof DoubleMove) {
			DoubleMove md = (DoubleMove) move;
			HBox box = new HBox();
			box.getChildren().add(new ImageView(manager.getTicket(md.firstMove().ticket())));
			box.getChildren().add(new ImageView(manager.getTicket(md.secondMove().ticket())));
			return box;
		} else {
			throw new AssertionError();
		}
	}

	public BooleanProperty highlightProperty() {
		return highlight;
	}

	// @Override
	public void discard() {
		this.discarded = true;
	}

	void addMove(Move move) {
		this.moves.add(move);
	}

	@Override
	public Parent root() {
		return root;
	}

	private interface TicketVisitor extends MoveVisitor {

		@Override
		default void visit(DoubleMove move) {
			visit(move.firstMove());
			visit(move.secondMove());
		}
	}

}
