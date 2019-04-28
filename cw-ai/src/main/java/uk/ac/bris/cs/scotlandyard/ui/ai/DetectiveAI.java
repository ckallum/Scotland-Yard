package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.function.Consumer;

import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.ai.ResourceProvider;
import uk.ac.bris.cs.scotlandyard.ai.Visualiser;
import uk.ac.bris.cs.scotlandyard.model.*;

// TODO name the AI
@ManagedAI("Detective")
public class DetectiveAI implements PlayerFactory {

    // TODO create a new player here
    @Override
    public Player createPlayer(Colour colour) {
        return new MyPlayer();
    }

    // TODO A sample player that selects a random move
    private static class MyPlayer implements Player {
        Random random = new Random();

        /*Idea is to weight each edge based on method of transportation, where taxi has least value(1),
         * bus has second most value(5) and train has most value (10). Then we can add a multiplier of that
         * edge based on how close the destination node(source node) is to nearby detectives.
         * 	- if the multiplier is below a certain value then use DoubleMove or Boat if possible*/
        @Override
        public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                             Consumer<Move> callback) {
            // TODO do something interesting here; find the best move


            callback.accept(new ArrayList<>(moves).get(random.nextInt(moves.size())));
        }
    }
    @Override
    public List<Spectator> createSpectators(ScotlandYardView view){
        return Collections.emptyList();
    }


}
