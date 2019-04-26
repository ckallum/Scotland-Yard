package uk.ac.bris.cs.scotlandyard.ui.ai;
public class DNode {
    private Integer location;
    private double safety;
    private int freedom;
    //Freedom of movement -- implement this in DGraph

    public DNode(Integer location) {
        this.location = location;
        this.safety = 1.0;
    }

    public Integer getLocation() {
        return this.location;
    }

    public double getSafety() {
        return safety;
    }

    public void subtractSafety(double danger){
        this.safety -= danger;
    }

    public int getFreedom(){
        return freedom;
    }

    public void setFreedom(int freedom){
        this.freedom = freedom;
    }


    public boolean equals(DNode node){
        return (node.getLocation().equals(this.location));
    }

}
