package uk.ac.bris.cs.scotlandyard.ui.ai;
public class DNode {
    private Integer location;
    private double danger;
    private double freedom;
    //Freedom of movement -- implement this in DGraph

    public DNode(Integer location) {
        this.location = location;
        this.danger = 0;
    }

    public Integer getLocation() {
        return this.location;
    }

    public double getDanger() {
        return danger;
    }

    public void setDanger(double danger){
        this.danger += danger;
    }

    public double getFreedom(){
        return freedom;
    }

    public void setFreedom(double freedom){
        this.freedom = freedom;
    }


}
