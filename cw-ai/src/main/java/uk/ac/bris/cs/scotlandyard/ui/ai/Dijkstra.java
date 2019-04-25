package uk.ac.bris.cs.scotlandyard.ui.ai;
import com.google.common.collect.MinMaxPriorityQueue;
import java.util.ArrayList;
import java.util.*;

public class Dijkstra {
        private DGraph graph;
        private int source;
        private int destination;
        private Map<DNode, Integer> totalCosts = new HashMap<>(graph.getSize());
        private MinMaxPriorityQueue<Integer> maxPriorityQueue;
        private Set<Integer> visited = new HashSet<>();


    public Dijkstra(DGraph graph, int source, int destination) {
        this.graph = graph;
        this.source = source;
        this.destination = destination;
    }

    public void dijkstra(){ //Creates a Map For Costs between Nodes Relative to the Source
        totalCosts.put(graph.getNode(source),0);
        maxPriorityQueue.add(source);
        ArrayList<DNode> nodes = graph.getNodes();
        for(DNode node : nodes){
            if (node.getLocation()!= source){
                totalCosts.put(node, Integer.MAX_VALUE);
            }
        }

        while(!maxPriorityQueue.isEmpty()){
            visited.add(maxPriorityQueue.peekFirst());

            for(DEdge edge:graph.getEdges()){
                if(edge.getSource().getLocation().equals(maxPriorityQueue.peekFirst())){
                    if(!visited.contains(edge.getDestination().getLocation())){

                    }
                }
            }
            maxPriorityQueue.removeFirst();
        }
    }

}
