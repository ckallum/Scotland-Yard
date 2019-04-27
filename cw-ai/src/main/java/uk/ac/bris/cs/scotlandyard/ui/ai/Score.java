package uk.ac.bris.cs.scotlandyard.ui.ai;

//NEED TO DO SOMETHING ON DOUBLE MOVE

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.HashSet;
import java.util.Set;

public class Score {
    private Graph<Integer, Transport> graph;
    private DGraph dGraph;
    private State state;
    private int source;
    private Set<Integer> detectiveLocations = new HashSet<>();
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
        for (int j = 1; j<200; j++){
            if(source !=j) {
                dijkstraTable[j] = dijkstra.getCost(j);
            }
            else dijkstraTable[j] = 0;
        }
    }

    public int getBestDestination(int location){
        int bestDestination = -1;
        double max = -1;
        int count = 0;
        for (Edge<Integer, Transport> edge:graph.getEdgesFrom(graph.getNode(location))){
                count++;
                System.out.println("Value:" + dijkstraTable[edge.destination().value()]);
                System.out.println("Safety: "+count+" "+dGraph.getNode(location).getSafety());

                if(!detectiveLocations.contains(edge.destination().value()) && dijkstraTable[edge.destination().value()] > max){
                    max = dijkstraTable[edge.destination().value()];
                    bestDestination = edge.destination().value();
                }
            }
        System.out.println("Max: " + max);
        System.out.println("Final Safety: " + dGraph.getNode(bestDestination).getSafety());
        return bestDestination;
    }

    public Move getBestMove() {
        Transport transport = null;
        int firstDestination = getBestDestination(this.source);
        for (Edge<Integer, Transport> edge : graph.getEdges()) {
            if (edge.source().value() == this.source && edge.destination().value() == firstDestination)
            System.out.println("Transport: "+edge.data().toString());{
                transport = edge.data();
            }
        }
        if (state.getMrXDoubleTickets() > 0 && dGraph.getNode(firstDestination).getSafety() < 0.45) {
            int secondDestination = getBestDestination(firstDestination);
            Transport transport2 = null;
            for (Edge<Integer, Transport> edge : graph.getEdges()) {
                if (edge.source().value() == firstDestination && edge.destination().value() == secondDestination) {
                    transport2 = edge.data();
                    System.out.println("Second move location:" + secondDestination);
                    return (new DoubleMove(Colour.BLACK, Ticket.fromTransport(transport), firstDestination, Ticket.fromTransport(transport2), secondDestination));
                }

            }
        }
        System.out.println("First move location " + firstDestination);
        return (new TicketMove(Colour.BLACK, Ticket.fromTransport(transport), firstDestination));
    }
}
