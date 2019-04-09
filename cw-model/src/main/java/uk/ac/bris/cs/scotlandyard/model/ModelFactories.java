package uk.ac.bris.cs.scotlandyard.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.ac.bris.cs.gamekit.graph.Graph;

/**
 * Stores factories to use for unit testing. Multiple models can be tested concurrently.
 */
public class ModelFactories {

	/**
	 * A list of models to test
	 * @return A list of models; never null
	 */
	static List<Class<? extends ScotlandYardGameFactory>> factories() {
		return Collections.singletonList(ImperativeModelFactory.class);
	}

	/**
	 * A simple implementation that uses the {@link ScotlandYardModel}
	 */
	public static class ImperativeModelFactory implements ScotlandYardGameFactory {

		@Override
		public ScotlandYardGame createGame(List<Boolean> rounds, Graph<Integer, Transport> graph,
				PlayerConfiguration mrX, PlayerConfiguration firstDetective,
				PlayerConfiguration... restOfTheDetectives) {
			return new ScotlandYardModel(rounds, graph, mrX, firstDetective, restOfTheDetectives);
		}

		@Override
		public String toString() {
			return "ScotlandYardModel";
		}

	}

}
