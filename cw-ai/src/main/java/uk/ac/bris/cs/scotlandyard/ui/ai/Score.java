package uk.ac.bris.cs.scotlandyard.ui.ai;

//NEED TO DO SOMETHING ON DOUBLE MOVE

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingDeque;

public class Score {
    private Graph<Integer, Transport> graph;
    private DGraph dGraph;
    private State state;
    private int source;
    private Set<Integer> detectiveLocations = new HashSet<>();
    private final double[] dijkstraGraph = new double[200];/* Stores the maximum distances/weightings from two nodes
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
        dijkstra.dijkstra();
        for (int j = 1; j<200; j++){
            if(source !=j) {
                dijkstraGraph[j] = dijkstra.getCost(j);
            }
            else dijkstraGraph[j] = 0;
        }
    }

    public int getBestDestination(int location){
        int bestDestination = -1;
        double max = -1;
        for (Edge<Integer, Transport> edge:graph.getEdges()){
            if(edge.source().value() == location){
                System.out.println(dijkstraGraph[edge.destination().value()]);
                if(!detectiveLocations.contains(edge.destination().value()) && dijkstraGraph[edge.destination().value()] > max){
                    max = dijkstraGraph[edge.destination().value()];
                    bestDestination = edge.destination().value();
                }
            }
        }
        System.out.println("Max: " + max);
        System.out.println("Safety: " + dGraph.getNode(bestDestination).getSafety());
        return bestDestination;
    }

    public Move getBestMove(){
        Transport transport = null;
        int bestDestination = getBestDestination(this.source);
        for (Edge<Integer, Transport> edge:graph.getEdges()) {
            if (edge.source().value()== this.source && edge.destination().value() == bestDestination) {
                transport = edge.data();
            }
        }
        if(state.getMrXDoubleTickets()>0 && dGraph.getNode(bestDestination).getSafety()<0.45){
            int secondDestination = getBestDestination(bestDestination);
            Transport transport2 = null;
            for (Edge<Integer, Transport> edge:graph.getEdges()) {
                if (edge.source().value()==bestDestination && edge.destination().value() == secondDestination) {
                    transport2 = edge.data();
                }
            }

            if (dGraph.getNode(secondDestination).getSafety() > dGraph.getNode(bestDestination).getSafety()){
                System.out.println("Second move location:" + secondDestination);
                return (new DoubleMove(Colour.BLACK, Ticket.fromTransport(transport), bestDestination, Ticket.fromTransport(transport2), secondDestination));
            }
        }
        System.out.println("First move location " + bestDestination);
        return (new TicketMove(Colour.BLACK, Ticket.fromTransport(transport), bestDestination));
    }


    //FindMax to get best move- move has to be in withing valid moves?

    /*
    * Double for loop to find each node, dijkstraGraph[i][j] = dijkstraCalculate(DGraph graph, i, j)*/

    /*Use gamestate/view to find the mrX location. Write a function to find the best available score based on mrX's location in dijkstraGraph and detective locations in Dgraph*/
}
