package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.Set;
import java.util.function.Consumer;

public class MrXAI implements Player {


        /*Idea is to weight each edge based on method of transportation, where taxi has least value(1),
         * bus has second most value(5) and train has most value (10). Then we can add a multiplier of that
         * edge based on how close the destination node(source node) is to nearby detectives.
         * 	- if the multiplier is below a certain value then use DoubleMove or Boat if possible*/
        @Override
        public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                             Consumer<Move> callback) {
            // TODO do something interesting here; find the best move

            State state = new State(view, location);
            Score score = new Score(state);
            Move bestMove = score.getBestMove();
            if (bestMove == null){
                throw new IllegalArgumentException("HIHIHI");

            }
            callback.accept(bestMove);

    }
}
