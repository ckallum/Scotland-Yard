package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.ArrayList;

/*Class to implement the current State of the game and changes to the player and game view
    - Will Store current mrXLocation and DetectiveLocations and current graph?
    - Will update these values when a move is made. Eg change gamestate when a move is made ->call move.visit, willl call local visit functions from MoveVisitor implementation
*   - Possibly implement MoveVisitor to update MrXLocation.*/
public class State {
    private ArrayList<Integer> detectiveLocations;
//    private Integer mrxLocation;
    private ScotlandYardView view;
    //validMoves?

    public State(ScotlandYardView view) {
        this.detectiveLocations = findDetectiveLocations(view);
//        this.mrxLocation = view.getPlayerLocation(Colour.BLACK);
        this.view = view;
    }

    public ScotlandYardView getView() {
        return view;
    }

    public ArrayList<Integer> findDetectiveLocations(ScotlandYardView view) {
        ArrayList<Integer> detectiveLocations = new ArrayList<>();
        for(Colour colour: view.getPlayers()){
            if(!colour.isMrX()){
                detectiveLocations.add(view.getPlayerLocation(colour).orElse(0));
            }
        }
        return detectiveLocations;
    }

    public ArrayList<Integer> DetectiveLocations() {
        return detectiveLocations;
    }
}
