package uk.ac.bris.cs.scotlandyard.ui.ai;
import com.google.common.collect.MinMaxPriorityQueue;
import java.util.ArrayList;
import java.util.*;

public class Dijkstras {
        private DGraph graph;
        private int source;
        private Map<DNode, Integer> totalCosts = new HashMap<>(graph.getNodes().size());
        private MinMaxPriorityQueue<Integer> maxPriorityQueue;
        private Set<Integer> visited = new HashSet<>();


    public Dijkstras(DGraph graph, int source) {
        this.graph = graph;
        this.source = source;
        this.totalCosts.put(graph.getNode(source),0);
        maxPriorityQueue.add(source);
    }

    public int dijkstras(){
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
        return 0;

    }
}
