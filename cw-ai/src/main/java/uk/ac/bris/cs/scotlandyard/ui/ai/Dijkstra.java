package uk.ac.bris.cs.scotlandyard.ui.ai;
import com.google.common.collect.MinMaxPriorityQueue;
import java.util.ArrayList;
import java.util.*;

public class Dijkstra {
        private DGraph graph;
        private DNode source; // i in distances[i][j]
        private Map<DNode, Double> totalCosts = new HashMap<>(graph.getSize());
        private Map<DNode, Double> maxPriorityQueue = new HashMap<>(graph.getSize());
        private Set<DNode> visited = new HashSet<>();


    public Dijkstra(DGraph graph, int source) {
        this.graph = graph;
        this.source = graph.getNode(source);
    }

    public void dijkstra(){ //Creates a Map For Costs between Nodes Relative to the Source
        totalCosts.put(source,0.0);
        maxPriorityQueue.put(source, 0.0);
        ArrayList<DNode> nodes = graph.getNodes();
        for(DNode node : nodes){
            if (node!= source){
                totalCosts.put(node, Double.MIN_VALUE);
            }
        }

        while(!maxPriorityQueue.isEmpty()){
            visited.add(findMax(maxPriorityQueue));
            DNode currentMax = findMax(maxPriorityQueue);
            maxPriorityQueue.remove(currentMax);
            for(DEdge edge:graph.getEdges()){
                if(edge.getSource().equals(currentMax)){
                    DNode neighbour= edge.getDestination();
                    if(!visited.contains(neighbour)){
                        double tempCost = totalCosts.get(neighbour)+totalEdgeCost(edge, neighbour);
                        if(tempCost>totalCosts.get(neighbour)){
                            totalCosts.put(neighbour, tempCost);
                            maxPriorityQueue.put(neighbour, totalCosts.get(neighbour));
                        }
                    }
                }
            }
        }
    }

    private DNode findMax(Map<DNode, Double> maxPQ){
        double tempMax = Double.MIN_VALUE;
        DNode current = null;
        for(DNode node : graph.getNodes()){
            if(maxPQ.get(node)>tempMax){
                tempMax = maxPQ.get(node);
            }
            current = node;
        }
        return current;
    }


    private double totalEdgeCost(DEdge edge, DNode destination){
        return (edge.getTicketValue()+destination.getFreedom())*destination.getSafety();
    }

    public double getCost(DNode destination){
        return (totalCosts.get(destination));
    }



}
