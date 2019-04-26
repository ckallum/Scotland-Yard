package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.lang.reflect.Array;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class DGraph {
    private int size;
    private Graph<Integer,Transport> graph;
    private ArrayList<DNode> nodes = new ArrayList<>(size+1);
    private ArrayList<DEdge> edges = new ArrayList<>();
    private ArrayList<Integer> detectiveLocations;
    private ArrayList<Integer> visited=new ArrayList<>(size+1);
    private ScotlandYardView view;

    public DGraph(State state) {
        this.graph = state.getView().getGraph();
        this.view = state.getView();
        this.size = graph.size();
        this.detectiveLocations = findDetectiveLocations();


        List<Node<Integer>> allNodes = graph.getNodes();
        for(Node<Integer> node : allNodes){
            this.nodes.add(new DNode(requireNonNull(node.value())));
        }
        Collection<Edge<Integer,Transport>> allEdges = graph.getEdges();
        for(Edge<Integer,Transport> edge : allEdges){
            DEdge temp = new DEdge(edge);
            edges.add(requireNonNull(temp));
        }
        for (DNode node : nodes){
            node.addSafety(0);
        }
        weightNodeSafety(findDetectiveLocations(),0);
        weightNodeFreedom();
    }

    private void weightNodeSafety(ArrayList<Integer> n, double safety){
        //Higher the Safety the safer the node is
        for(Integer location:n){
            if(!detectiveLocations.contains(location)){
                if(this.visited.contains(location)){
                    if(safety < 0.8) { //If the node has been visited before and it is 3 moves away from another detective: subtract 0.1 from it's safety
                        addSafety(location, -0.1);
                    }
                }
                 else {
                     addSafety(location,safety);
                }
            }
            Collection<Edge<Integer,Transport>> connectingEdges = graph.getEdgesFrom(graph.getNode(location));
            ArrayList<Integer>neighbourNodes = new ArrayList<>();
            for (Edge<Integer,Transport> edge : connectingEdges){
                neighbourNodes.add(edge.destination().value());
            }
            if(safety<2.0) {
                weightNodeSafety(neighbourNodes, safety + 0.2);
            }
        }
    }

    private void addSafety(int location, double safety){
        for(DNode node : nodes){
            if(node.getLocation().equals(location)){
                node.addSafety(safety);
                visited.add(location);
            }
        }
    }

    private void weightNodeFreedom(){
        for (DNode node : nodes){
            int count = 0;
            for (DEdge edge : edges){
                if(edge.getSource().getLocation().equals(node.getLocation())){
                    if (!detectiveLocations.contains(edge.getDestination().getLocation())) {
                        count++;
                    }
                }
                node.setFreedom(count);
            }
        }
    }

    private ArrayList<Integer> findDetectiveLocations() {
        ArrayList<Integer> detectiveLocations = new ArrayList<>();
        for(Colour colour: view.getPlayers()){
            if(colour!=Colour.BLACK){
                detectiveLocations.add(view.getPlayerLocation(colour).orElse(0));
            }
        }
        return (detectiveLocations);
    }

    public ArrayList<DEdge> getEdges() {
        return requireNonNull(edges);
    }

    public ArrayList<DNode> getNodes() {
        return requireNonNull(nodes);
    }

    public DNode getNode(int location){
        return nodes.get(location);
    }

    public int getSize() {
        return size;
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
