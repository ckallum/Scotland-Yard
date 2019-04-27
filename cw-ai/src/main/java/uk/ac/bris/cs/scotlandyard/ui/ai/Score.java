package uk.ac.bris.cs.scotlandyard.ui.ai;

//NEED TO DO SOMETHING ON DOUBLE MOVE

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.concurrent.BlockingDeque;

public class Score {
    private Graph<Integer, Transport> graph;
    private State s;
    private Integer source;
    private final double[] dijkstraGraph = new double[200];/* Stores the maximum distances/weightings from two nodes
                                                                Use Dijkstra's to calculate maximum score between two nodes
    */
    //ValidMoves?
    public Score(State state) { //Use state as input?
        this.graph = state.getGraph();
        this.source = state.getMrxLocation();
        this.s = state;
        calculate();
    }

    private void calculate(){
        Dijkstra dijkstra = new Dijkstra(new DGraph(this.s), source);
        for (int j = 0; j<200; j++){
            dijkstra.dijkstra();
            if(source !=j) {
                dijkstraGraph[j] = dijkstra.getCost(j);
            }
            else dijkstraGraph[j] = 0;
        }
    }

    private Integer getBestDestination(){
        Integer bestMove = Integer.MIN_VALUE;
        double max = 0;
        for (Edge<Integer, Transport> edge:graph.getEdges()){
            if(edge.source().value().equals(source)){
                if(dijkstraGraph[edge.destination().value()] > max){
                    max = dijkstraGraph[edge.destination().value()];
                    bestMove = edge.destination().value();
                }
            }
        }
        if(bestMove!=null) throw new NullPointerException("testing");
        return bestMove;
    }

    public Move getMove(){
        //if node score is at certain limit create double mnove instead.
        for(Edge<Integer, Transport> edge:graph.getEdges()){
            if(edge.source().value().equals(source) && edge.destination().value().equals(getBestDestination())){
//                if() {
//                    return (new TicketMove(Colour.BLACK, Ticket.fromTransport(edge.getTransport()), getBestDestination()));
//                }
//                else {
//                    Integer location1 = getBestDestination()
//                    this.source = location1;
//                    //Work on this.
//                    return (new DoubleMove())
//                }
                return (new TicketMove(Colour.BLACK, Ticket.fromTransport(edge.data()), getBestDestination()));
            }
        }
        return null;
    }

    //FindMax to get best move- move has to be in withing valid moves?

    /*
    * Double for loop to find each node, dijkstraGraph[i][j] = dijkstraCalculate(DGraph graph, i, j)*/

    /*Use gamestate/view to find the mrX location. Write a function to find the best available score based on mrX's location in dijkstraGraph and detective locations in Dgraph*/
}
