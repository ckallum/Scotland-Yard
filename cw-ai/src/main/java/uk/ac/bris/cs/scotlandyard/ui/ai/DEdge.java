package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;

public class DEdge {
    private DNode source;
    private DNode destination;
    private Ticket ticket;
    private double TransportValue;
    private Transport transport;

    public DEdge(Edge<Integer, Transport> edge) {
        this.source = new DNode(edge.source().value());
        this.destination = new DNode(edge.destination().value());
        setTransportValue(edge.data());
        setTransport(edge.data());
    }

    public DNode getDestination() {
        return destination;
    }

    public void setTransportValue(Transport t) {
        if(t.equals(Transport.BUS)){
            this.TransportValue = 1.25;
        }
        if(t.equals(Transport.TAXI)){
            this.TransportValue = 1;
        }
        if(t.equals(Transport.UNDERGROUND)){
            this.TransportValue = 1.75;
        }
    }

    public double getTransportValue() {
        return TransportValue;
    }

    public void setTransport(Transport t) {
        this.transport = t;
    }

    public Transport getTransport() {
        return transport;
    }

    public DNode getSource() {
        return source;
    }
}
