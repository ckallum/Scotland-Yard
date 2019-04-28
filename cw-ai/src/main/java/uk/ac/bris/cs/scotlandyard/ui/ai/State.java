package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;


/*Class to implement the current State of the game and changes to the player and game view
    - Will Store current mrXLocation and DetectiveLocations and current graph?
    - Will update these values when a move is made. Eg change gamestate when a move is made ->call move.visit, willl call local visit functions from MoveVisitor implementation
*   - Possibly implement MoveVisitor to update MrXLocation.*/
public class State {
    private ScotlandYardView v;
    private int mrxLocation;
    private Map<Ticket, Integer> MrXTickets = new HashMap<>();
    private Graph<Integer, Transport> graph;
    private Set<Move> validMoves;

    public State(ScotlandYardView view, int location, Set<Move> moves) {
        this.v = view;
        this.mrxLocation = location;
        this.graph = view.getGraph();
        this.validMoves = moves;
        findMrXTickets();
    }

    public ScotlandYardView getView() {
        return v;
    }

    public int getMrxLocation() {
        return mrxLocation;
    }

    public Map<Ticket, Integer> getMrXTickets(){
        return this.MrXTickets;
    }

    public Graph<Integer, Transport> getGraph() {
        return graph;
    }

    private void findMrXTickets(){
        for(Ticket ticket : Ticket.values()){
            this.MrXTickets.put(ticket, v.getPlayerTickets(Colour.BLACK, ticket).orElse(0));
        }
    }

    public Set<Move> getValidMoves() {
        return validMoves;
    }
}
