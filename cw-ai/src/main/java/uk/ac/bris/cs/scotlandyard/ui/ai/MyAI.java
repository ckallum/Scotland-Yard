package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.function.Consumer;

import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.ai.ResourceProvider;
import uk.ac.bris.cs.scotlandyard.ai.Visualiser;
import uk.ac.bris.cs.scotlandyard.model.*;

// TODO name the AI
@ManagedAI("MrXAI")
public class MyAI implements PlayerFactory {
	private Score scorer;

	// TODO create a new player here
	@Override
	public Player createPlayer(Colour colour) {
		return new MyPlayer();
	}

	// TODO A sample player that selects a random move
	private static class MyPlayer implements Player {

		private final Random random = new Random();
		/*Idea is to weight each edge based on method of transportation, where taxi has least value(1),
		* bus has second most value(5) and train has most value (10). Then we can add a multiplier of that
		* edge based on how close the destination node(source node) is to nearby detectives.
		* 	- if the multiplier is below a certain value then use DoubleMove or Boat if possible*/
		@Override
		public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
				Consumer<Move> callback) {

			State state = new State(view);
			Score score = new Score(state);
			//moves = all valid moves?

			/*view has all game info*/
			/*callback is given some move, doesn't have to be array move*/
			// TODO do something interesting here; find the best move
			// picks a random move
			//callback accept some chosen move
			callback.accept(score.getMove());
		}
	}
	@Override
	public List<Spectator> createSpectators(ScotlandYardView view){
		return Collections.emptyList();
	}

	@Override
	public void finish(){

	}

	@Override
	public void ready(Visualiser visualiser, ResourceProvider provider){
	}

}
