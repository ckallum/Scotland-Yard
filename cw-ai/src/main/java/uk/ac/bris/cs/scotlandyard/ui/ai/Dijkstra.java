package uk.ac.bris.cs.scotlandyard.ui.ai;
import java.util.ArrayList;
import java.util.*;

public class Dijkstra {
        private DGraph graph;
        private Integer source; // i in distances[i][j]
        private int[]distances;
        private Set<DNode> visited = new HashSet<>();
        private PriorityQueue<Integer> maxPQ;


    public Dijkstra(DGraph Dgraph, Integer source) {
        this.graph = Dgraph;
        this.source = source;
        this.distances = new int[graph.getSize()];
        this.maxPQ = new PriorityQueue<>(graph.getSize());

    }

    public void dijkstra(){ //Creates a Map For Costs between Nodes Relative to the Source
        List<DNode> neighbourNodes = new ArrayList<>();
        DNode s = graph.getNode(source);
        for(DEdge edge : graph.getEdges()){
            if(edge.getSource().getLocation().equals(source)){
                neighbourNodes.add(edge.getDestination());
            }
        }

        for(int i =0; i<graph.getSize(); i++){
            distances[i] = Integer.MIN_VALUE;
        }

        if(s == null) throw new IllegalArgumentException("uh");
        distances[s.getLocation()] = 0;
        maxPQ.add(s.getLocation());
        while(visited.size() != graph.getSize()){
            Integer temp = maxPQ.remove();
            DNode currentMax = graph.getNode(temp);
            visited.add(currentMax);
            for(int i = 0; i<neighbourNodes.size(); i++){
                DNode neighbour = neighbourNodes.get(i);
                if(!visited.contains(neighbour)){
                    int distance = distances[neighbour.getLocation()]+1;
                    if(distance > distances[neighbour.getLocation()]){
                        distances[neighbour.getLocation()] = distance;
                    }
                    maxPQ.add(neighbour.getLocation());
                }
            }
        }
    }

    public double getCost(Integer location){
        return (distances[location]*(graph.getNode(location).getSafety()+graph.getNode(location).getFreedom()));
    }



}
