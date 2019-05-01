package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class State {
    private ScotlandYardView v;
    private int mrxLocation;
    private Map<Ticket, Integer> MrXTickets = new HashMap<>();
    private Graph<Integer, Transport> graph;
    private Set<Move> validMoves;
    private Set<Integer> detectiveLocations;

    public State(ScotlandYardView view, int location, Set<Move> moves) {
        this.v = view;
        this.mrxLocation = location;
        this.graph = view.getGraph();
        this.validMoves = moves;
        this.detectiveLocations = findDetectiveLocations();
        findMrXTickets();

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

    public Set<Integer> findDetectiveLocations() {
        Set<Integer> detectiveLocations = new HashSet<>();
        for(Colour colour: v.getPlayers()){
            if(colour!=Colour.BLACK){
                detectiveLocations.add(v.getPlayerLocation(colour).orElse(0));
            }
        }
        return (detectiveLocations);
    }

    public Set<Integer> getDetectiveLocations() {
        return detectiveLocations;
    }
}
