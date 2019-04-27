package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.Transport;


/*Class to implement the current State of the game and changes to the player and game view
    - Will Store current mrXLocation and DetectiveLocations and current graph?
    - Will update these values when a move is made. Eg change gamestate when a move is made ->call move.visit, willl call local visit functions from MoveVisitor implementation
*   - Possibly implement MoveVisitor to update MrXLocation.*/
public class State {
    private ScotlandYardView v;
    private int mrxLocation;
    private int mrXDoubleTickets;
    private Graph<Integer, Transport> graph;

    public State(ScotlandYardView view, int location) {
        this.v = view;
        this.mrxLocation = location;
        this.graph = view.getGraph();
        this.mrXDoubleTickets = view.getPlayerTickets(Colour.BLACK, Ticket.DOUBLE).orElse(0);
    }

    public ScotlandYardView getView() {
        return v;
    }

    public int getMrxLocation() {
        return mrxLocation;
    }

    public int getMrXDoubleTickets() {
        return mrXDoubleTickets;
    }

    public Graph<Integer, Transport> getGraph() {
        return graph;
    }
}
