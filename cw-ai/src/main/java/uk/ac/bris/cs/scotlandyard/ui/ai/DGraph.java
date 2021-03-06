package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;
import java.util.*;

public class DGraph {
    private Graph<Integer,Transport> graph;
    private List<DNode> nodes = new ArrayList<>();
    private Collection<Edge<Integer, Transport>> edges;
    private Set<Integer> detectiveLocations;

    public DGraph(State state) {
        this.graph = state.getGraph();
        this.detectiveLocations = state.getDetectiveLocations();
        this.edges = graph.getEdges();


        List<Node<Integer>> allNodes = graph.getNodes();
        for(Node<Integer> node : allNodes){
            this.nodes.add(new DNode(node.value()));
        }
        for(DNode node:nodes){
            if(detectiveLocations.contains(node.getLocation())){
                node.setSafety(0);
            }
        }
        for(DNode node:nodes){
            node.setFreedom(node.findNeighbours(this, graph.getNode(node.getLocation())).size());
        }
        weightNodeSafety(detectiveLocations,90, Collections.emptySet());
        subtractNodeFreedom();

        //Test to assert all detective locations have 0 safety;
        int count = 0;
        for(DNode node:nodes){
            if(node.getSafety()==0){
                count++;
            }
        }
        assert(count==detectiveLocations.size());
    }

    private void weightNodeSafety(Set<Integer> ns, int danger, Set<Integer>visited){
        //Higher the danger the more dangerous the node is
        Set<Integer>v=new HashSet<>(visited);//Creates a set of visited nodes so it doesn't repeat itself
        for(Integer location:ns) {
            if (!detectiveLocations.contains(location)) {
                /*if the location is 2 nodes away from a detective and is 1 node away from another detective,
                * increase the amount that is taken away*/
                if (danger > 80 && getNode(location).getSafety()<40) {
                    subtractSafety(location, danger);
                }
                else if(!v.contains(location)){
                    subtractSafety(location, danger);
                    v.add(location);
                }
            }
            Collection<Edge<Integer,Transport>> connectingEdges = graph.getEdgesFrom(graph.getNode(location));
            Set<Integer>neighbourNodes = new HashSet<>();
            for (Edge<Integer,Transport> edge : connectingEdges){
                neighbourNodes.add(edge.destination().value());
            }
            //Only subtract more safety if the node is 3 nodes away from a detective - recursive call.
            if(danger>60){
                weightNodeSafety(neighbourNodes, (danger-10),v);
            }
        }
    }

    private void subtractSafety(int location, double safety){
        for(DNode node : nodes){
            if(node.getLocation()==location){
                node.subtractSafety(safety);
            }
        }
    }

    //Subtracts freedom based on how many detectives are close by.
    private void subtractNodeFreedom(){
        for (DNode node : nodes){
            int count = 0;
            for (Edge<Integer, Transport> edge : edges){
                if(edge.source().value().equals(node.getLocation())){
                    if (detectiveLocations.contains(edge.destination().value())) {
                        count ++;
                    }
                }
            }
            node.setFreedom(-count); //Decrease freedom based on number of detectives that are beside the node
        }
    }

    //Returns detective locations

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

    public Set<Integer> getDetectiveLocations() {
        return detectiveLocations;
    }
}
