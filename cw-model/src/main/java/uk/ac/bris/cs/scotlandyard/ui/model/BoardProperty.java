package uk.ac.bris.cs.scotlandyard.ui.model;

import net.kurobako.gesturefx.GesturePane.ScrollMode;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BoardProperty {

	private final ObjectProperty<ScrollMode> scrollMode = new SimpleObjectProperty<>(ScrollMode.PAN);
	private final BooleanProperty animation = new SimpleBooleanProperty(true);
	private final BooleanProperty focusPlayer = new SimpleBooleanProperty(false);
	private final BooleanProperty history = new SimpleBooleanProperty(false);

	public ScrollMode getScrollMode() {
		return scrollMode.get();
	}

	public ObjectProperty<ScrollMode> scrollModeProperty() {
		return scrollMode;
	}

	public BooleanProperty animationProperty() {
		return animation;
	}

	public boolean isFocusPlayer() {
		return focusPlayer.get();
	}

	public BooleanProperty focusPlayerProperty() {
		return focusPlayer;
	}

	public boolean isHistory() {
		return history.get();
	}

	public BooleanProperty historyProperty() {
		return history;
	}

}
