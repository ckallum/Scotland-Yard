package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.*;
import java.util.*;
public class DNode {
    private Integer location;
    private int multiplier;
    private ScotlandYardView view;

    public DNode(Integer location) {
        this.location = location;
    }

    public Integer getLocation() {
        return location;
    }

    public int getMultiplier() {
        return multiplier;
    }

}
