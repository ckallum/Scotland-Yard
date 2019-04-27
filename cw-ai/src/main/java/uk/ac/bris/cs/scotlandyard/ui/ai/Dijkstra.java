package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.Transport;
import java.util.*;

public class Dijkstra {
        private DGraph graph;
        private int[]distances;
        private int source;


    public Dijkstra(DGraph Dgraph, int source) {
        this.graph = Dgraph;
        this.distances = new int[graph.getSize()+1];
        this.source = source;

    }

    public void calculateDistances(){
        Set<Integer> detectiveLocations = graph.findDetectiveLocations();
        Set<Node<Integer>> neighbours = getNeighbours(graph.getGraph().getNode(source));

        for(Node<Integer> neighbour:neighbours){
            for(int detectiveLocation : detectiveLocations){
                dijkstra(detectiveLocation, neighbour.value());
            }
        }
        for(int i = 1; i<distances.length; i++){
            distances[i] = distances[i]/(graph.findDetectiveLocations().size());
            System.out.println("Distance:" + i + " " + distances[i]);
        }
    }


    public void dijkstra(int source, int destination){
        //Creates a Map For Costs between Nodes Relative to the Source
        Set<Node<Integer>> visited = new HashSet<>();
        Set<Node<Integer>> minPQ = new HashSet<>();
        int[]temp = new int[distances.length];
        for(int i =1; i<distances.length; i++){
            temp[i] = Integer.MAX_VALUE;
        }
        Node<Integer> currentMin = graph.getGraph().getNode(source);
        temp[currentMin.value()] = 0;
        minPQ.add(currentMin);

        while(currentMin.value()!=destination){
            Integer minLocation = findMin(minPQ);
            currentMin = graph.getGraph().getNode(minLocation);
            visited.add(currentMin);
            minPQ.remove(currentMin);
            Set<Node<Integer>>neighbours = getNeighbours(currentMin);
            for(Node<Integer> neighbour : neighbours){
                if(!visited.contains(neighbour)){
                    int distance = temp[minLocation]+1;
                    if(distance < temp[neighbour.value()]){
                        temp[neighbour.value()] = distance;
                    }
                    minPQ.add(neighbour);
                }
            }
        }
        for(int i =1; i<distances.length;i++){
            if(temp[i]>0&&temp[i]<200){
                distances[i] += temp[i];
            }
        }
    }

    private  int findMin(Set<Node<Integer>> minPQ) {
        int current = Integer.MAX_VALUE;
        int node = -1;

        for (int n = 1; n<distances.length; n++) {

            if ((distances[n] < current) && minPQ.contains(graph.getGraph().getNode(n))){
                current = distances[n];
                node = n;
            }
        }
        return node;
    }

    private Set<Node<Integer>> getNeighbours(Node<Integer> location){
        Set<Node<Integer>> neighbours = new HashSet<>();
        Collection<Edge<Integer,Transport>> connections = graph.getGraph().getEdgesFrom(location);
        for(Edge<Integer,Transport> connection : connections){
            if(!graph.findDetectiveLocations().contains(connection.destination().value())){
                neighbours.add(connection.destination());
            }
        }
        return neighbours;
    }


    public double getCost(int location){
        return (distances[location]*(graph.getNode(location).getSafety()))+graph.getNode(location).getFreedom();
    }



}
