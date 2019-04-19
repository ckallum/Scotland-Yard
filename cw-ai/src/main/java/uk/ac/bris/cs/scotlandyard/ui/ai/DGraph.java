package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.*;
import java.util.*;

public class DGraph {
    private ScotlandYardView view;
    private ArrayList<DNode> nodes;
    private ArrayList<DEdge> edges;
    private Graph<Integer,Transport> graph;
    private int size;

    public DGraph(ScotlandYardView view) {
        this.view = view;
        this.graph = view.getGraph();
        this.size = graph.size();

        List<Node<Integer>> allNodes = graph.getNodes();
        for(Node<Integer> node : allNodes){
            DNode temp = new DNode(node.value());
            nodes.add(temp);
        }
        Collection<Edge<Integer,Transport>> allEdges = graph.getEdges();
        for(Edge<Integer,Transport> edge : allEdges){
            DEdge temp = new DEdge(edge.source(), edge.destination());
            temp.setScore(edge.data());
            edges.add(temp);
        }

    }
}
