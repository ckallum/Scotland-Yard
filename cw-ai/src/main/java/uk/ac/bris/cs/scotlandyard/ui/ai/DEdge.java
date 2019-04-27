package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.scotlandyard.model.*;

public class DEdge {
    private DNode source;
    private DNode destination;

    public DEdge(Edge<Integer, Transport> edge) {
        this.source = new DNode(edge.source().value());
        this.destination = new DNode(edge.destination().value());
    }

    public DNode getDestination() {
        return destination;
    }

    public DNode getSource() {
        return source;
    }
}
