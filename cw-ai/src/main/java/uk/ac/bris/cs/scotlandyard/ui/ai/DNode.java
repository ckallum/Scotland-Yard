package uk.ac.bris.cs.scotlandyard.ui.ai;
public class DNode {
    private int location;
    private int safety;
    private double freedom;
    //Freedom of movement -- implement this in DGraph

    public DNode(Integer location) {
        this.location = location;
        this.safety = 100;
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

    public double getFreedom(){
        return freedom;
    }

    public void setSafety(int safety) {
        this.safety = safety;
    }

    public void setFreedom(double freedom){
        this.freedom = freedom*0.05;
    }

}
