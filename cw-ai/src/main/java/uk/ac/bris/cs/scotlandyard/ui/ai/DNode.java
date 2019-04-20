package uk.ac.bris.cs.scotlandyard.ui.ai;
public class DNode {
    private Integer location;
    private double multiplier;

    public DNode(Integer location) {
        this.location = location;
    }

    public Integer getLocation() {
        return this.location;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier){
        this.multiplier = multiplier;
    }


}
