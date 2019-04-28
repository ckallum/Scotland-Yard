package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Node;

import java.util.*;

public class Dijkstra {
    private DGraph graph;
    private Map<Integer,Integer> distances = new HashMap<>();
    private int source;


    public Dijkstra(DGraph dGraph, int source) {
        this.graph = dGraph;
        this.source = source;

    }

    public void calculateDistances(){
        Set<Integer> detectiveLocations = graph.findDetectiveLocations();
        Set<Node<Integer>> neighbours = graph.getNode(source).findNeighbours(graph, graph.getGraph().getNode(source));
        for(Node<Integer> neighbour:neighbours){
            distances.put(neighbour.value(),0);
            for(int detectiveLocation : detectiveLocations){
                dijkstra(detectiveLocation, neighbour.value());
            }
            distances.put(neighbour.value(), distances.get(neighbour.value())/detectiveLocations.size());
        }
    }


    public void dijkstra(int source, int destination){
        //Creates a Map For Costs between Nodes Relative to the Source
        Set<Node<Integer>> visited = new HashSet<>();
        Set<Node<Integer>> minPQ = new HashSet<>();
        Map<Integer, Integer> temp = new HashMap<>();
        for(int i =1; i<graph.getGraph().size()+1; i++){
            temp.put(i, Integer.MAX_VALUE);
        }
        Node<Integer> currentMin = graph.getGraph().getNode(source);
        temp.put(currentMin.value(), 0);
        minPQ.add(currentMin);

        while(currentMin.value()!=destination){
            int minLocation = findMin(temp, minPQ);
            currentMin = graph.getGraph().getNode(minLocation);
            visited.add(currentMin);
            minPQ.remove(currentMin);
            Set<Node<Integer>>neighbours = graph.getNode(minLocation).findNeighbours(graph,graph.getGraph().getNode(minLocation));
            for(Node<Integer> neighbour : neighbours){
                if(!visited.contains(neighbour)){
                    int distance = temp.get(minLocation)+1;
                    if(distance < temp.get(neighbour.value())){
                        temp.put(neighbour.value(), distance);
                    }
                    minPQ.add(neighbour);
                }
            }
        }
        distances.put(destination, distances.get(destination)+temp.get(destination));
    }

    private int findMin(Map<Integer, Integer> temp, Set <Node<Integer>> minPQ) {
        int current = Integer.MAX_VALUE;
        int node = -1;
        for (int n = 1; n<temp.size()+1; n++) {
            if ((temp.get(n) < current) && minPQ.contains(graph.getGraph().getNode(n))){
                current = temp.get(n);
                node = n;
            }
        }
        return node;
    }


    public double getCost(int location){
        return (distances.get(location)-graph.getNode(location).getFreedom())*(graph.getNode(location).getSafety());
    }



}