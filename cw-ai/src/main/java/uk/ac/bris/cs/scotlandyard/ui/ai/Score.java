package uk.ac.bris.cs.scotlandyard.ui.ai;

//NEED TO DO SOMETHING ON DOUBLE MOVE

import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.concurrent.BlockingDeque;

public class Score {
    private DGraph graph;
    private Integer source;
    private final double[][] dijkstraGraph = new double[200][200];/* Stores the maximum distances/weightings from two nodes
                                                                Use Dijkstra's to calculate maximum score between two nodes
    */
    //ValidMoves?
    public Score(State state) { //Use state as input?
        this.graph = state.getGraph();
        this.source = state.getMrxLocation();
        calculate();
    }

    private void calculate(){
        for (int i = 0; i<200; i++){
            Dijkstra dijkstra = new Dijkstra(this.graph, i);
            for (int j = 0; j<200; j++){
                dijkstra.dijkstra();
                if(i!=j){
                    dijkstraGraph[i][j] = dijkstra.getCost(graph.getNode(j));
                }
                else dijkstraGraph[i][j] = 0;
            }
        }
    }

    private Integer getBestDestination(){
        Integer bestMove = Integer.MIN_VALUE;
        double max = 0;
        for (DEdge edge:graph.getEdges()){
            if(edge.getSource().getLocation().equals(source)){
                if(dijkstraGraph[source][edge.getDestination().getLocation()] > max){
                    max = dijkstraGraph[source][edge.getDestination().getLocation()];
                    bestMove = edge.getSource().getLocation();
                }
            }
        }
        if(bestMove!=null) throw new NullPointerException("testing");
        return bestMove;
    }

    public Move getMove(){
        //if node score is at certain limit create double mnove instead.
        for(DEdge edge:graph.getEdges()){
            if(edge.getSource().getLocation().equals(source) && edge.getDestination().getLocation().equals(getBestDestination())){
//                if() {
//                    return (new TicketMove(Colour.BLACK, Ticket.fromTransport(edge.getTransport()), getBestDestination()));
//                }
//                else {
//                    Integer location1 = getBestDestination()
//                    this.source = location1;
//                    //Work on this.
//                    return (new DoubleMove())
//                }
                return (new TicketMove(Colour.BLACK, Ticket.fromTransport(edge.getTransport()), getBestDestination()));
            }
        }
        return null;
    }

    //FindMax to get best move- move has to be in withing valid moves?

    /*
    * Double for loop to find each node, dijkstraGraph[i][j] = dijkstraCalculate(DGraph graph, i, j)*/

    /*Use gamestate/view to find the mrX location. Write a function to find the best available score based on mrX's location in dijkstraGraph and detective locations in Dgraph*/
}
