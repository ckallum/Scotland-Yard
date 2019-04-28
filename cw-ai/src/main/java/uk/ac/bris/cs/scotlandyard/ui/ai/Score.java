package uk.ac.bris.cs.scotlandyard.ui.ai;

//NEED TO DO SOMETHING ON DOUBLE MOVE

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.HashSet;
import java.util.Set;

public class Score {
    private Graph<Integer, Transport> graph;
    private DGraph dGraph;
    private State state;
    private int source;
    private Set<Integer> detectiveLocations;
    private final double[] dijkstraTable = new double[200];/* Stores the maximum distances/weightings from two nodes
                                                                Use Dijkstra's to calculate maximum score between two nodes
    */
    //ValidMoves?
    public Score(State state) { //Use state as input?
        this.graph = state.getGraph();
        this.source = state.getMrxLocation();
        this.state = state;
        this.dGraph = new DGraph(state);
        this.detectiveLocations = dGraph.findDetectiveLocations();
        calculate();
    }

    private void calculate(){
        Dijkstra dijkstra = new Dijkstra(new DGraph(this.state), this.source);
        dijkstra.calculateDistances();
        Set<Node<Integer>>neighbours = dGraph.getNode(source).findNeighbours(dGraph,graph.getNode(source));
        if(neighbours==null)throw new IllegalArgumentException("neighbours are null");
        for (Node<Integer> neighbour : neighbours){
            dijkstraTable[neighbour.value()] = dijkstra.getCost(neighbour.value());
        }
    }

    public int getBestDestination(int location){
        int bestDestination = -1;
        double max = -1;
        for (Node<Integer> neighbour: dGraph.getNode(source).findNeighbours(dGraph,graph.getNode(location))){
            if(!detectiveLocations.contains(neighbour.value()) && dijkstraTable[neighbour.value()] > max){
                max = dijkstraTable[neighbour.value()];
                bestDestination = neighbour.value();
            }
        }
        return bestDestination;
    }

    public Move getBestMove() {
        Ticket ticket = null;
        int firstDestination = getBestDestination(this.source);
        for (Edge<Integer, Transport> edge : graph.getEdgesFrom(graph.getNode(source))) {
            if (edge.destination().value() == firstDestination && (state.getMrXTickets().get(Ticket.fromTransport(edge.data()))>0)) {
                if(dGraph.getNode(firstDestination).getFreedom() <= 6){
                    ticket = Ticket.SECRET;
                }
                else ticket = Ticket.fromTransport(edge.data());
            }
        }
        if (dGraph.getNode(firstDestination).getSafety() <= 65) {
            Ticket ticket2;
            int secondDestination = getBestDestination(firstDestination);
            for (Edge<Integer, Transport> edge : graph.getEdgesFrom(graph.getNode(firstDestination))) {
                if ((edge.destination().value() == secondDestination)&& (state.getMrXTickets().get(Ticket.fromTransport(edge.data()))>0)) {
                    if(dGraph.getNode(firstDestination).getSafety()<dGraph.getNode(secondDestination).getSafety()){
                        if(dGraph.getNode(firstDestination).getFreedom() <= 6){
                            ticket2 = Ticket.SECRET;
                        }
                        else ticket2 = Ticket.fromTransport(edge.data());

                        return (new DoubleMove(Colour.BLACK, ticket, firstDestination, ticket2, secondDestination));
                    }
                }
            }
        }
        return (new TicketMove(Colour.BLACK, ticket, firstDestination));
    }
}
