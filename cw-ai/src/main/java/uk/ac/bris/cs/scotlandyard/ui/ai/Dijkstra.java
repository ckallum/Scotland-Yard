package uk.ac.bris.cs.scotlandyard.ui.ai;
import java.util.ArrayList;
import java.util.*;

public class Dijkstra {
        private DGraph graph;
        private DNode source; // i in distances[i][j]
        private Map<DNode, Double> totalCosts;
        private ArrayList<DNode> visited = new ArrayList<>();


    public Dijkstra(DGraph graph, int source) {
        this.graph = graph;
        this.source = graph.getNode(source);
        this.totalCosts = dijkstra();
    }

    public Map<DNode,Double> dijkstra(){ //Creates a Map For Costs between Nodes Relative to the Source
        Map<DNode, Double> maxPriorityQueue = new HashMap<>(graph.getSize()+1);
        Map<DNode, Double> totalCosts = new HashMap<>(graph.getSize()+1);
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
        return totalCosts;
    }

    private DNode findMax(Map<DNode, Double> maxPQ){
        double tempMax = Double.MIN_VALUE;
        DNode current = null;
        for(DNode node : graph.getNodes()){
            if(maxPQ.containsKey(node) && maxPQ.get(node)>tempMax){
                tempMax = maxPQ.get(node);
            }
            current = node;
        }
        return current;
    }


    private double totalEdgeCost(DEdge edge, DNode destination){
        return (edge.getTransportValue()+destination.getFreedom())*destination.getSafety();
//        return (edge.getTransportValue()+destination.getFreedom());

    }

    public double getCost(DNode destination){
        return (totalCosts.get(destination));
    }



}
