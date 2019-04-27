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
    private Set<DNode> nodes = new HashSet<>();
    private Collection<Edge<Integer, Transport>> edges;
    private Set<Integer> detectiveLocations;
    private ArrayList<Integer> visited=new ArrayList<>();
    private ScotlandYardView view;

    public DGraph(State state) {
        this.graph = state.getGraph();
        this.view = state.getView();
        this.size = state.getGraph().size();
        this.detectiveLocations = findDetectiveLocations();
        this.edges = graph.getEdges();


        List<Node<Integer>> allNodes = graph.getNodes();
        for(Node<Integer> node : allNodes){
            this.nodes.add(new DNode(node.value()));
        }
        weightNodeSafety(findDetectiveLocations(),0.95);
        weightNodeFreedom();
    }

    private void weightNodeSafety(Set<Integer> n, double danger){
        //Higher the danger the more dangerous the node is
        for(Integer location:n){
            if(!detectiveLocations.contains(location)){
                if(this.visited.contains(location)){
                    if(danger >=0.55) { //If the node has been visited before and it is 3 moves away from another detective: subtract 0.1 from it's safety
                        subtractSafety(location, danger + 0.1);
                    }
                }
                 else {
                     subtractSafety(location,danger);
                }
            }
            Collection<Edge<Integer,Transport>> connectingEdges = graph.getEdgesFrom(graph.getNode(location));
            Set<Integer>neighbourNodes = new HashSet<>();
            for (Edge<Integer,Transport> edge : connectingEdges){
                neighbourNodes.add(edge.destination().value());
            }
            if(danger>=0.35) {
                weightNodeSafety(neighbourNodes, danger - 0.2);
            }
        }
    }

    private void subtractSafety(int location, double safety){
        for(DNode node : nodes){
            if(node.getLocation()==location){
                node.subtractSafety(safety);
                visited.add(location);
            }
        }
    }

    private void weightNodeFreedom(){
        for (DNode node : nodes){
            int count = 0;
            for (Edge<Integer, Transport> edge : edges){
                if(edge.source().value().equals(node.getLocation())){
                    if (!detectiveLocations.contains(edge.destination().value())) {
                        count++;
                    }
                }
                node.setFreedom(count);
            }
        }
    }

    private Set<Integer> findDetectiveLocations() {
        Set<Integer> detectiveLocations = new HashSet<>();
        for(Colour colour: view.getPlayers()){
            if(colour!=Colour.BLACK){
                detectiveLocations.add(view.getPlayerLocation(colour).orElse(0));
            }
        }
        return (detectiveLocations);
    }

    public Graph<Integer, Transport> getGraph() {
        return graph;
    }

    public DNode getNode(int location){
        for(DNode node : this.nodes){
            if (node.getLocation()==location){
                return node;
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

}
