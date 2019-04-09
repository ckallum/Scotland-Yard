package uk.ac.bris.cs.scotlandyard.ui;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uk.ac.bris.cs.scotlandyard.model.Colour;

public class ColourTableCell<S> extends TableCell<S, Colour> {

	@Override
	protected void updateItem(Colour item, boolean empty) {
		if (!empty) {
			Rectangle rectangle = new Rectangle(40, 20);
			rectangle.setFill(Color.valueOf(item.name()));
			rectangle.setStroke(Color.LIGHTGRAY);
			rectangle.setStrokeWidth(1);
			setGraphic(rectangle);
		}
		super.updateItem(item, empty);
	}
}
