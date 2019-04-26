package uk.ac.bris.cs.scotlandyard.ui.ai;
import java.util.ArrayList;
import java.util.*;

public class Dijkstra {
        private DGraph graph;
        private DNode source; // i in distances[i][j]
        private int[]distances;
        private Set<DNode> visited = new HashSet<>();
        private PriorityQueue<DNode> maxPQ;


    public Dijkstra(DGraph graph, int source) {
        this.graph = graph;
        this.source = graph.getNode(source);
        this.distances = new int[graph.getSize()];

    }

    public void dijkstra(){ //Creates a Map For Costs between Nodes Relative to the Source
        List<DNode> neighbourNodes = new ArrayList<>();
        for(DEdge edge : graph.getEdges()){
            if(edge.getSource().equals(source)){
                neighbourNodes.add(edge.getDestination());
            }
        }

        for(int i =0; i<graph.getSize(); i++){
            distances[i] = Integer.MIN_VALUE;
        }
        distances[source.getLocation()] = 0;
        maxPQ.add(source);
        while(visited.size() != graph.getSize()){
            DNode currentMax = maxPQ.remove();
            visited.add(currentMax);
            for(int i = 0; i<neighbourNodes.size(); i++){
                DNode neighbour = neighbourNodes.get(i);
                if(!visited.contains(neighbour)){
                    int distance = distances[neighbour.getLocation()]+1;
                    if(distance > distances[neighbour.getLocation()]){
                        distances[neighbour.getLocation()] = distance;
                    }
                    maxPQ.add(neighbour);
                }
            }
        }
    }

    public double getCost(DNode destination){
        return (distances[destination.getLocation()]*(destination.getFreedom()+destination.getSafety()));
    }



}
