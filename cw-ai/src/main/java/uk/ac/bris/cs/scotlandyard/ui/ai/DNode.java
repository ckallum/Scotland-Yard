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
        this.freedom = 0;
    }

    public Set<Node<Integer>> findNeighbours(DGraph graph, Node<Integer> location){
        Set<Node<Integer>> neighbours = new HashSet<>();
        Collection<Edge<Integer, Transport>> connections = graph.getGraph().getEdgesFrom(location);
        for(Edge<Integer,Transport> connection : connections){
            if(graph.getNode(connection.destination().value())==null)throw new IllegalArgumentException("dGraph!=graph?");
            neighbours.add(connection.destination());
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
        if(safety>danger) {
            this.safety -= (danger);
        }/*
        These conditions occur when the node is already visited but is being visited by another detective
        that is one node away
        */
        /*One node away from 2 detectives
          - Score:2

          Three node away from a detective and one node away from another detective
          - Score : 12
        */
        else if(this.safety-(danger/5)>0){
            this.safety-=(danger/5);
        }
        /*One node away from 2 detectives
          - Score:1
        */
        else if(this.safety-(danger/10)>0){
            this.safety-=(danger/10);
        }
    }

    public int getFreedom(){
        return freedom;
    }

    public void setSafety(int safety) {
        this.safety = safety;
    }

    public void setFreedom(int freedom){
        this.freedom += freedom;
    }

}
