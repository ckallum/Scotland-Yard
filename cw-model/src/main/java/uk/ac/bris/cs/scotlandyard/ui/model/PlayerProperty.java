package uk.ac.bris.cs.scotlandyard.ui.model;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.fxmisc.easybind.EasyBind;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import uk.ac.bris.cs.scotlandyard.ai.AI;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.StandardGame;
import uk.ac.bris.cs.scotlandyard.model.Ticket;

public class PlayerProperty {

	public static final int RANDOM = -1;

	private final BooleanProperty enabled = new SimpleBooleanProperty(true);
	private final ObjectProperty<Colour> colour = new SimpleObjectProperty<>();
	private final StringProperty name = new SimpleStringProperty();
	private final IntegerProperty location = new SimpleIntegerProperty(RANDOM);
	private final ObjectProperty<AI> ai = new SimpleObjectProperty<>();
	private final ObservableList<TicketProperty> tickets = FXCollections
			.observableArrayList(param -> new Observable[] { param.ticket, param.count });

	public PlayerProperty(PlayerProperty other) {
		enabled.set(other.enabled());
		colour.set(other.colour());

		name.set(other.name.get());

		other.name().ifPresent(name::set);
		location.set(other.location());
		other.ai().ifPresent(ai::set);
		tickets.setAll(other.tickets());
	}

	public PlayerProperty(Colour colour) {
		this.colour.set(colour);

		Map<Ticket, Integer> map = new TreeMap<>(colour == Colour.BLACK ?
				StandardGame.generateMrXTickets() :
				StandardGame.generateDetectiveTickets());
		map.forEach((t, c) -> tickets.add(new TicketProperty(t, c)));
		// if (colour == Colour.BLACK)
		this.enabled.set(true);
	}

	public boolean enabled() {
		return enabled.get();
	}

	public BooleanProperty enabledProperty() {
		return enabled;
	}

	public Colour colour() {
		return colour.get();
	}

	public Side side() {
		return colour().isMrX() ? Side.MRX : Side.DETECTIVE;
	}

	public boolean mrX() {
		return colour.get().isMrX();
	}

	public boolean detective() {
		return colour.get().isDetective();
	}

	public ObjectProperty<Colour> colourProperty() {
		return colour;
	}

	public BooleanExpression mrXProperty() {
		return BooleanBinding.booleanExpression(EasyBind.map(colour, Colour::isMrX));
	}

	public Optional<String> name() {
		return Optional.ofNullable(name.get())
				.flatMap(s -> Strings.isNullOrEmpty(s) ? Optional.empty() : Optional.of(s));
	}

	public StringProperty nameProperty() {
		return name;
	}

	public int location() {
		return location.get();
	}

	public IntegerProperty locationProperty() {
		return location;
	}

	public boolean randomLocation() {
		return location.isEqualTo(RANDOM).get();
	}

	public Optional<AI> ai() {
		return Optional.ofNullable(ai.get());
	}

	public Optional<Class<? extends AI>> aiClass() {
		return ai().map(AI::getClass);
	}

	public ObjectProperty<AI> aiProperty() {
		return ai;
	}

	public ObservableList<TicketProperty> tickets() {
		return tickets;
	}

	public Map<Ticket, Integer> ticketsAsMap() {
		return tickets().stream().collect(toMap(TicketProperty::ticket, TicketProperty::count));
	}

	public List<Property<?>> observables() {
		return Arrays.asList(enabled, colour, name, location, ai);
	}

	public static List<PlayerProperty> allDetectives(Collection<PlayerProperty> configs) {
		return configs.stream().filter(PlayerProperty::detective).collect(Collectors.toList());
	}

	public static Optional<PlayerProperty> mrX(Collection<PlayerProperty> configs) {
		return configs.stream().filter(PlayerProperty::mrX).findFirst();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("enabled", enabled).add("colour", colour)
				.add("name", name).add("location", location).add("ai", ai).toString();
	}
}
