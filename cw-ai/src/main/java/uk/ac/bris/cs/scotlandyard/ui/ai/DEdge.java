package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;

public class DEdge {
    private DNode source;
    private DNode destination;
    private double TicketValue;

    public DEdge(Node<Integer> source, Node<Integer> destination) {
        this.source = new DNode(source.value());
        this.destination = new DNode(destination.value());
    }

    public DNode getDestination() {
        return destination;
    }

    public void setTicketValue(Transport t) {
        if(t.equals(Transport.BUS)){
            this.TicketValue = 1.25;
        }
        if(t.equals(Transport.TAXI)){
            this.TicketValue = 1;
        }
        if(t.equals(Transport.UNDERGROUND)){
            this.TicketValue = 1.75;
        }
    }

    public double getTicketValue() {
        return TicketValue;
    }

    public DNode getSource() {
        return source;
    }
}
