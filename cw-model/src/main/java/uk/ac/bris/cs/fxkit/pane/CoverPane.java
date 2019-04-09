package uk.ac.bris.cs.fxkit.pane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Pane with automatic children scaling that does not require a layout run
 */
public class CoverPane extends Pane implements ChangeListener<Bounds> {

	private final ChangeListener<Bounds> boundChanged = (o, p,
			c) -> resizeAllChildren(getLayoutBounds());

	{
		setCacheHint(CacheHint.QUALITY);
		layoutBoundsProperty().addListener(this);
		getChildren().addListener(new WeakListChangeListener<>(c -> {
			while (c.next()) {
				if (c.wasRemoved()) c.getRemoved()
						.forEach(n -> n.layoutBoundsProperty().removeListener(boundChanged));
				if (c.wasAdded()) c.getAddedSubList()
						.forEach(n -> n.layoutBoundsProperty().addListener(boundChanged));
			}
		}));
	}

	@Override
	public void changed(ObservableValue<? extends Bounds> o, Bounds p, Bounds c) {
		if (c != null) resizeAllChildren(c);
	}

	private void resizeAllChildren(Bounds bounds) {
		getChildren().forEach(n -> resizeNode(n, bounds));
	}

	private static void resizeNode(Node node, Bounds bounds) {
		Bounds nodeBounds = node.getLayoutBounds();
		double minScale = Math.min(bounds.getWidth() / nodeBounds.getWidth(),
				bounds.getHeight() / nodeBounds.getHeight());

		node.setTranslateX(bounds.getWidth() / 2 - nodeBounds.getWidth() / 2);
		node.setTranslateY(bounds.getHeight() / 2 - nodeBounds.getHeight() / 2);

		if (minScale > 0) {
			// node.setTranslateX(-(nodeBounds.getWidth() -
			// nodeBounds.getWidth() * minScale) / 2);
			// node.setTranslateY(-(nodeBounds.getHeight() -
			// nodeBounds.getHeight() * minScale) / 2);
			node.setScaleX(minScale);
			node.setScaleY(minScale);
		}
	}
}
