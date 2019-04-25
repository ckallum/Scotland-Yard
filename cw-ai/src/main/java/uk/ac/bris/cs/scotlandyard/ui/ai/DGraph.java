package uk.ac.bris.cs.scotlandyard.ui.ai;
import org.checkerframework.checker.nullness.qual.NonNull;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class DGraph {
    private ArrayList<DNode> nodes = new ArrayList<>(this.size);
    private ArrayList<DEdge> edges = new ArrayList<>();
    private Graph<Integer,Transport> graph;
    private int size;
    private ArrayList<Integer> detectiveLocations;

    public DGraph(State state) {
        this.graph = state.getView().getGraph();
        this.size = graph.size();
        this.detectiveLocations = state.DetectiveLocations();

        List<Node<Integer>> allNodes = graph.getNodes();
        for(Node<Integer> node : allNodes){
            this.nodes.add(node.value(),new DNode(requireNonNull(node.value())));
        }
        Collection<Edge<Integer,Transport>> allEdges = graph.getEdges();
        for(Edge<Integer,Transport> edge : allEdges){
            DEdge temp = new DEdge(edge.source(), edge.destination());
            temp.setTicketValue(edge.data());
            edges.add(requireNonNull(temp));
        }
        for (DNode node : nodes){
            node.addSafety(0);
        }
        weightNodeSafety(state.DetectiveLocations(),Collections.emptyList(),0);
    }

    private void weightNodeSafety(ArrayList<Integer> nodes, List<Integer> visited, double safety){
        //Higher the Safety the safer the node is
        for(Integer location:nodes){
            if(!detectiveLocations.contains(location)){
                if(visited.contains(location)){
                    if(safety < 0.8) { //If the node has been visited before and it is 3 moves away from another detective: subtract 0.1 from it's safety
                        this.nodes.get(location).addSafety(-0.1);
                    }
                }
                 else {
                    this.nodes.get(location).addSafety(safety);
                    visited.add(location);
                }
            }
            Collection<Edge<Integer,Transport>> neighbours = new ArrayList<>(graph.getEdgesFrom(new Node<>(location)));
            for (Edge<Integer,Transport> edge : neighbours){
                ArrayList<Integer>neighbourNodes = new ArrayList<>();
                neighbourNodes.add(edge.destination().value());
                weightNodeSafety(neighbourNodes,visited, safety+0.2);
            }
        }
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
