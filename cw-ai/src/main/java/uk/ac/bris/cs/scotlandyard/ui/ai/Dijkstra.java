package uk.ac.bris.cs.scotlandyard.ui.ai;
import com.google.common.collect.MinMaxPriorityQueue;
import java.util.ArrayList;
import java.util.*;

public class Dijkstra {
        private DGraph graph;
        private int source;
        private int destination;
        private Map<DNode, Double> totalCosts = new HashMap<>(graph.getSize());
        private MinMaxPriorityQueue<Integer> maxPriorityQueue;
        private Set<Integer> visited = new HashSet<>();


    public Dijkstra(DGraph graph, int source, int destination) {
        this.graph = graph;
        this.source = source;
        this.destination = destination;
    }

    public void dijkstra(){ //Creates a Map For Costs between Nodes Relative to the Source
        totalCosts.put(graph.getNode(source),0.0);
        maxPriorityQueue.add(source);
        ArrayList<DNode> nodes = graph.getNodes();
        for(DNode node : nodes){
            if (node.getLocation()!= source){
                totalCosts.put(node, Double.MIN_VALUE);
            }
        }

        while(!maxPriorityQueue.isEmpty()){
            visited.add(maxPriorityQueue.peekFirst());
            int currentMax = maxPriorityQueue.peekFirst();
            maxPriorityQueue.removeFirst();
            for(DEdge edge:graph.getEdges()){
                if(edge.getSource().getLocation().equals(currentMax)){
                    DNode neighbour= edge.getDestination();
                    if(!visited.contains(neighbour.getLocation())){
                        double tempCost = totalCosts.get(neighbour)+totalEdgeCost(edge, neighbour);
                        if(tempCost>totalCosts.get(neighbour)){
                            totalCosts.put(neighbour, tempCost);
                            maxPriorityQueue.add(neighbour.getLocation());
                        }
                    }
                }
            }
        }
    }

    public double totalEdgeCost(DEdge edge, DNode destination){
        double cost = (edge.getTicketValue()+destination.getFreedom())*destination.getSafety();
        return cost;
    }

}
