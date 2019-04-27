package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.Transport;

import java.util.ArrayList;
import java.util.*;

public class Dijkstra {
        private DGraph graph;
        private Integer source; // i in distances[i][j]
        private int[]distances;
        private Set<Node<Integer>> visited = new HashSet<>();
        private PriorityQueue<Integer> maxPQ;


    public Dijkstra(DGraph Dgraph, Integer source) {
        this.graph = Dgraph;
        this.source = source;
        this.distances = new int[graph.getSize()];
        this.maxPQ = new PriorityQueue<>(graph.getSize());

    }

    public void dijkstra(){ //Creates a Map For Costs between Nodes Relative to the Source
        List<Node<Integer>> neighbourNodes = new ArrayList<>();
        Node<Integer> s = graph.getGraph().getNode(source);
        for(Edge<Integer, Transport> edge : graph.getGraph().getEdgesFrom(graph.getGraph().getNode(source))){
            neighbourNodes.add(edge.destination());
        }

        for(int i =0; i<graph.getSize(); i++){
            distances[i] = Integer.MIN_VALUE;
        }

        distances[s.value()] = 0;
        maxPQ.add(s.value());
        while(visited.size() != graph.getSize()){
            Integer temp = maxPQ.peek();
            Node<Integer> currentMax = graph.getGraph().getNode(temp);
            visited.add(currentMax);
            maxPQ.remove(temp);
            for(Node<Integer> neighbour : neighbourNodes){
                if(!visited.contains(neighbour)){
                    int distance = distances[neighbour.value()]+1;
                    if(distance > distances[neighbour.value()]){
                        distances[neighbour.value()] = distance;
                    }
                    maxPQ.add(neighbour.value());
                }
            }
        }
    }

    public double getCost(Integer location){
        return (distances[location]*(graph.getNode(location).getSafety()+graph.getNode(location).getFreedom()));
    }



}
