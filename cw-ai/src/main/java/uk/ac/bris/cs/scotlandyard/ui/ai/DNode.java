package uk.ac.bris.cs.scotlandyard.ui.ai;
public class DNode {
    private Integer location;
    private double danger;
    //Freedom of movement

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


}
