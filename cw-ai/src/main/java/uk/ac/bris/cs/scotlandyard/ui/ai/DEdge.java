package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;

public class DEdge {
    private DNode source;
    private DNode destination;
    private int score;
    private int TicketValue;

    public DEdge(Node<Integer> source, Node<Integer> destination) {
        this.source = new DNode(source.value());
        this.destination = new DNode(destination.value());
        this.score = score;
    }

    public DNode getDestination() {
        return destination;
    }

    public void setScore(Transport t) {
        if(t.equals(Transport.BUS)){
            this.TicketValue = 5;
        }
        if(t.equals(Transport.TAXI)){
            this.TicketValue = 1;
        }
        if(t.equals(Transport.UNDERGROUND)){
            this.TicketValue = 10;
        }
    }
}
