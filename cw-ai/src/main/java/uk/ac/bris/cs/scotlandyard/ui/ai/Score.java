package uk.ac.bris.cs.scotlandyard.ui.ai;


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
    private final double[] dijkstraTable = new double[200];

    Score(State state) {
        this.graph = state.getGraph();
        this.source = state.getMrxLocation();
        this.state = state;
        this.dGraph = new DGraph(state);
        calculate();
    }

    private void calculate() {
        Dijkstra dijkstra = new Dijkstra(new DGraph(this.state), this.source);
        dijkstra.calculateDistances();
        Set<Node<Integer>> neighbours = dGraph.getNode(source).findNeighbours(dGraph, graph.getNode(source));
        for (Node<Integer> neighbour : neighbours) {
            dijkstraTable[neighbour.value()] = dijkstra.getCost(neighbour.value());
            //Quick test to check all neighbour nodes are properly scored
            assert(dijkstraTable[neighbour.value()]>-1);
        }
    }

    private int getBestDestination(int location) {
        int bestDestination = -1;
        double max = -1;
        for (Node<Integer> neighbour : dGraph.getNode(source).findNeighbours(dGraph, graph.getNode(location))) {
            if (dijkstraTable[neighbour.value()] > max) {
                max = dijkstraTable[neighbour.value()];
                bestDestination = neighbour.value();
            }
        }
        return bestDestination;
    }

    public Move getBestMove() {
        Ticket ticket1 = null;
        Move move1 = null;
        DNode dSource = dGraph.getNode(source);
        int firstDestination = getBestDestination(this.source);
        for (Edge<Integer, Transport> edge : graph.getEdgesFrom(graph.getNode(source))) {
            if (edge.destination().value() == firstDestination && (state.getMrXTickets().get(Ticket.fromTransport(edge.data())) > 0)) {
                if (toSecret(source)) {
                    ticket1 = Ticket.SECRET;
                } else {
                    ticket1 = Ticket.fromTransport(edge.data());
                }
                move1 = new TicketMove(Colour.BLACK, ticket1, firstDestination);
            }
        }
        if (dSource.getSafety() <= 20 || dGraph.getNode(firstDestination).getSafety()<=20){//If the current node or the next best mode is relatively unsafe consider using a doubleMove
            if (state.getMrXTickets().get(Ticket.DOUBLE) > 0) {
                Ticket ticket2;
                Move move2 = null;
                int secondDestination = getBestDestination(firstDestination);
                if (dijkstraTable[firstDestination] < dijkstraTable[secondDestination]) {//Only create the double move if the second move in the move is better than the first move. This should always be the case if the first move is a pass move
                    for (Edge<Integer, Transport> edge2 : graph.getEdgesFrom(graph.getNode(firstDestination))) {
                        if ((edge2.destination().value() == secondDestination) && (state.getMrXTickets().get(Ticket.fromTransport(edge2.data())) > 0)) {
                            if (toSecret(firstDestination)) {
                                ticket2 = Ticket.SECRET;
                            } else {
                                ticket2 = Ticket.fromTransport(edge2.data());
                            }
                            move2 = new DoubleMove(Colour.BLACK, ticket1, firstDestination, ticket2, secondDestination);
                        }
                    }
                    if (state.getValidMoves().contains(move2)) {
                        return (move2);
                    }
                }
            }
        }
        if(state.getValidMoves().contains(move1)) {
            return move1;
        }
        return null;
    }

    private boolean toSecret(int location) {
        return(dGraph.getNode(location).getSafety()<10 && state.getMrXTickets().get(Ticket.SECRET) > 0);
    }

}
