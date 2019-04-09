package uk.ac.bris.cs.scotlandyard.ui.model;

import com.google.common.base.MoreObjects;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import uk.ac.bris.cs.scotlandyard.model.Ticket;

public class TicketProperty {

	public final ObjectProperty<Ticket> ticket = new SimpleObjectProperty<>();
	public final IntegerProperty count = new SimpleIntegerProperty();

	public TicketProperty(Ticket ticket, int count) {
		this.ticket.set(ticket);
		this.count.set(count);
	}

	public Ticket ticket() {
		return ticket.get();
	}

	public ObjectProperty<Ticket> ticketProperty() {
		return ticket;
	}

	public int count() {
		return count.get();
	}

	public IntegerProperty countProperty() {
		return count;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				       .add("ticket", ticket)
				       .add("count", count)
				       .toString();
	}
}
