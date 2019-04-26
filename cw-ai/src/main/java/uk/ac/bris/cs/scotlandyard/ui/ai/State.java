package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

import static java.util.Objects.requireNonNull;

/*Class to implement the current State of the game and changes to the player and game view
    - Will Store current mrXLocation and DetectiveLocations and current graph?
    - Will update these values when a move is made. Eg change gamestate when a move is made ->call move.visit, willl call local visit functions from MoveVisitor implementation
*   - Possibly implement MoveVisitor to update MrXLocation.*/
public class State {
    private ArrayList<Integer> detectiveLocations;
    private ScotlandYardView view;
    private Integer mrxLocation;
    private DGraph graph;
    private boolean currentRound;

    public State(ScotlandYardView view, int location) {
        this.view = view;
        this.mrxLocation = location;
        this.graph = new DGraph(this);
        currentRound = findCurrentRound();

    }

    public ScotlandYardView getView() {
        return view;
    }

    private boolean findCurrentRound(){
        return (this.view.getRounds().get(view.getCurrentRound()));
    }

    public Integer getMrxLocation() {
        return mrxLocation;
    }

    public DGraph getGraph() {
        return graph;
    }
}
