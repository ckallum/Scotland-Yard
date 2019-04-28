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

    private void calculate() {
        Dijkstra dijkstra = new Dijkstra(new DGraph(this.state), this.source);
        dijkstra.calculateDistances();
        Set<Node<Integer>> neighbours = dGraph.getNode(source).findNeighbours(dGraph, graph.getNode(source));
        if (neighbours == null) throw new IllegalArgumentException("neighbours are null");
        for (Node<Integer> neighbour : neighbours) {
            dijkstraTable[neighbour.value()] = dijkstra.getCost(neighbour.value());
        }
    }

    public int getBestDestination(int location) {
        int bestDestination = -1;
        double max = -1;
        for (Node<Integer> neighbour : dGraph.getNode(source).findNeighbours(dGraph, graph.getNode(location))) {
            if (!detectiveLocations.contains(neighbour.value()) && dijkstraTable[neighbour.value()] > max) {
                max = dijkstraTable[neighbour.value()];
                bestDestination = neighbour.value();
            }
        }
        return bestDestination;
    }

    public Move getBestMove() {
        Ticket ticket = null;
        Move move1 = null;
        int firstDestination = getBestDestination(this.source);
        for (Edge<Integer, Transport> edge : graph.getEdgesFrom(graph.getNode(source))) {
            if (edge.destination().value() == firstDestination && (state.getMrXTickets().get(Ticket.fromTransport(edge.data())) > 0)) {
                if (toSecret(firstDestination)) {
                    ticket = Ticket.SECRET;
                }
                else {
                    ticket = Ticket.fromTransport(edge.data());
                }
                move1 = new TicketMove(Colour.BLACK, ticket, firstDestination);
            }

                if (dGraph.getNode(firstDestination).getSafety() <= 65 && state.getMrXTickets().get(Ticket.DOUBLE) > 0) {
                    Ticket ticket2;
                    Move move2;
                    int secondDestination = getBestDestination(firstDestination);
                    for (Edge<Integer, Transport> edge2 : graph.getEdgesFrom(graph.getNode(firstDestination))) {
                        if ((edge2.destination().value() == secondDestination) && (state.getMrXTickets().get(Ticket.fromTransport(edge2.data())) > 0)) {
                            if (dGraph.getNode(firstDestination).getSafety() < dGraph.getNode(secondDestination).getSafety()) {
                                if (toSecret(secondDestination)) {
                                    ticket2 = Ticket.SECRET;
                                }
                                else {
                                    ticket2= Ticket.fromTransport(edge2.data());
                                }

                                move2 = new DoubleMove(Colour.BLACK, ticket, firstDestination, ticket2, secondDestination);
                                if (state.getValidMoves().contains(move2)){
                                    return (move2);
                                }
                            }
                        }
                    }
                }
                if(state.getValidMoves().contains(move1)) {
                    return move1;
                }
            }
        return null;
    }

    private boolean toSecret(int destination) {
        return(dGraph.getNode(destination).getFreedom() >1);
    }

}
