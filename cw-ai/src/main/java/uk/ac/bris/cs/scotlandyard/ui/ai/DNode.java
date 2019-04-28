package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.Transport;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DNode {
    private int location;
    private int safety;
    private int freedom;
    //Freedom of movement -- implement this in DGraph

    public DNode(Integer location) {
        this.location = location;
        this.safety = 100;
    }

    public Set<Node<Integer>> findNeighbours(DGraph graph, Node<Integer> location){
        Set<Node<Integer>> neighbours = new HashSet<>();
        Collection<Edge<Integer, Transport>> connections = graph.getGraph().getEdgesFrom(location);
        for(Edge<Integer,Transport> connection : connections){
            if(!graph.findDetectiveLocations().contains(connection.destination().value())){
                if(graph.getNode(connection.destination().value())==null)throw new IllegalArgumentException("dGraph!=graph?");
                neighbours.add(connection.destination());
            }
        }
        return neighbours;
    }

    public int getLocation() {
        return this.location;
    }

    public double getSafety() {
        return safety;
    }

    public void subtractSafety(double danger){
        if(safety>danger) this.safety -= danger;
    }

    public int getFreedom(){
        return freedom;
    }

    public void setSafety(int safety) {
        this.safety = safety;
    }

    public void setFreedom(int freedom){
        this.freedom = freedom;
    }

}
