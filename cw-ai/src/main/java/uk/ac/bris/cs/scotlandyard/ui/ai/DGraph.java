package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;
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
            nodes.set(node.value(), temp);
        }
        Collection<Edge<Integer,Transport>> allEdges = graph.getEdges();
        for(Edge<Integer,Transport> edge : allEdges){
            DEdge temp = new DEdge(edge.source(), edge.destination());
            temp.setTicketValue(edge.data());
            edges.add(temp);
        }
        weightNodes(nodes, 1.0);
    }

    public void weightNodes(ArrayList<DNode> nodes, double M){
        for(Integer location:getDetectiveLocations()){
            double multiplier = M;
            nodes.get(location).setMultiplier(multiplier);
            multiplier-= 0.5;
            Collection<Edge<Integer,Transport>> neighbours = new ArrayList<>(graph.getEdgesFrom(new Node<>(location)));
            for (Edge<Integer,Transport> edge : neighbours){

            }


        }
    }

    private ArrayList<Integer> getDetectiveLocations(){
        ArrayList<Integer> detectiveLocations = new ArrayList<>();
        for(Colour colour: view.getPlayers()){
            if(!colour.isMrX()){
                detectiveLocations.add(view.getPlayerLocation(colour).orElse(0));
            }
        }
        return detectiveLocations;
    }
}
