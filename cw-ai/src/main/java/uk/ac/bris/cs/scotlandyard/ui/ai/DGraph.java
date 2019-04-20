package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class DGraph {
    private ScotlandYardView view;
    private ArrayList<DNode> nodes = new ArrayList<>(this.size);
    private ArrayList<DEdge> edges = new ArrayList<>();
    private Graph<Integer,Transport> graph;
    private int size;

    public DGraph(ScotlandYardView view) {
        this.view = view;
        this.graph = view.getGraph();
        this.size = graph.size();

        List<Node<Integer>> allNodes = graph.getNodes();
        for(Node<Integer> node : allNodes){
            this.nodes.add(new DNode(requireNonNull(node.value())));
        }
        Collection<Edge<Integer,Transport>> allEdges = graph.getEdges();
        for(Edge<Integer,Transport> edge : allEdges){
            DEdge temp = new DEdge(edge.source(), edge.destination());
            temp.setTicketValue(edge.data());
            edges.add(requireNonNull(temp));
        }
        weightNodes(getDetectiveLocations(), 0);
    }

    private void weightNodes(ArrayList<Integer> nodes, double multiplier){
        for(Integer location:nodes){
            if(this.nodes.get(location).getMultiplier()>0){
                this.nodes.get(location).setMultiplier(multiplier-0.25);
            }
            else {
                this.nodes.get(location).setMultiplier(multiplier);
            }
            double m = multiplier+ 0.5;
            Collection<Edge<Integer,Transport>> neighbours = new ArrayList<>(graph.getEdgesFrom(new Node<>(location)));
            for (Edge<Integer,Transport> edge : neighbours){
                ArrayList<Integer>neighbourNodes = new ArrayList<>();
                neighbourNodes.add(edge.destination().value());
                weightNodes(neighbourNodes, m);
            }
        }
    }

    public ArrayList<Integer> getDetectiveLocations(){
        ArrayList<Integer> detectiveLocations = new ArrayList<>();
        for(Colour colour: view.getPlayers()){
            if(!colour.isMrX()){
                detectiveLocations.add(view.getPlayerLocation(colour).orElse(0));
            }
        }
        return detectiveLocations;
    }

    public ArrayList<DEdge> getEdges() {
        return edges;
    }

    public ArrayList<DNode> getNodes() {
        return nodes;
    }
//
//    public static void main(String[] args) {
//
//        DGraph graph = new DGraph();
//        ArrayList<DNode> nodes = graph.getNodes();
//        for(DNode node :nodes){
//            System.out.print(node.getMultiplier());
//        }
//    }
}
