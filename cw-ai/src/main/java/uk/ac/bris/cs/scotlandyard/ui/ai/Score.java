package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;
import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Score {
    private ScotlandYardView view;
    private DGraph graph;
    private int source;
    private final int[][] dijkstraGraph = new int[200][200]; /* Stores the minimum distances/weightings from two nodes
                                                                Use Dijkstras to calculate minimum score between two nodes
    */
    public Score(ScotlandYardView view, int mrxLocation, DGraph graph) {
        this.view = view;
        this.graph = graph;
        this.source = mrxLocation;
        //fillDijkstraGraph on initiation using method below
    }

    /*
    * Double for loop to find each node, dijkstraGraph[i][j] = dijkstraCalculate(DGraph graph, i, j)*/

    /*Use gamestate/view to find the mrX location. Write a function to find the best available score based on mrX's location in dijkstraGraph and detective locations in Dgraph*/
}
