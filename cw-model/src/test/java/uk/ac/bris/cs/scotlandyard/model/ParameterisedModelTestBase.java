package uk.ac.bris.cs.scotlandyard.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import uk.ac.bris.cs.gamekit.graph.Graph;

import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.DEFAULT_REVEAL;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.doNothingBlue;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.doNothingGreen;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.doNothingMrX;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.doNothingRed;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.doNothingWhite;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.doNothingYellow;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.ofRounds;

/**
 * Base class for all tests. Contains various helper methods for convenience
 */
@RunWith(Parameterized.class)
public abstract class ParameterisedModelTestBase implements ScotlandYardGameFactory {

	/**
	 * A list of game factories to use
	 */
	static final List<Class<? extends ScotlandYardGameFactory>> FACTORIES = ModelFactories
			.factories();

	@SuppressWarnings("WeakerAccess") @Parameter public ScotlandYardGameFactory factory;

	@Parameters(name = "{0}")
	public static ScotlandYardGameFactory[] data() {
		return ScotlandYardGameFactory.instantiate(FACTORIES)
				.toArray(new ScotlandYardGameFactory[0]);
	}

	@BeforeClass
	public static void setUp() {
		try {
			defaultGraph = ScotlandYardGraphReader.fromLines(Files.readAllLines(
					Paths.get(ParameterisedModelTestBase.class.getResource("/game_graph.txt")
							.toURI())));

		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private static Graph<Integer, Transport> defaultGraph;
	/**
	 * Returns the default graph used in the actual game
	 *
	 * @return the graph; never null
	 */
	static Graph<Integer, Transport> defaultGraph() {
		return defaultGraph;
	}


	@Override
	public ScotlandYardGame createGame(List<Boolean> rounds, Graph<Integer, Transport> graph,
	                                   PlayerConfiguration mrX, PlayerConfiguration firstDetective,
	                                   PlayerConfiguration... restOfTheDetectives) {
		return factory.createGame(rounds, graph, mrX, firstDetective, restOfTheDetectives);
	}

	/**
	 * Creates a new game with 1..23 rounds and the default graph
	 *
	 * @param mrX Mr.X
	 * @param firstDetective first detective
	 * @param restOfTheDetectives the rest of the detectives
	 * @return the created game; never null
	 */
	ScotlandYardGame createGame(PlayerConfiguration mrX, PlayerConfiguration firstDetective,
	                            PlayerConfiguration... restOfTheDetectives) {
		return createGame(ofRounds(23, DEFAULT_REVEAL), defaultGraph, mrX,
				firstDetective,
				restOfTheDetectives);
	}

	/**
	 * Creates a new game with given rounds and the default graph
	 *
	 * @param rounds rounds
	 * @param mrX Mr.X
	 * @param firstDetective first detective
	 * @param restOfTheDetectives the rest of the detectives
	 * @return the created game; never null
	 */
	ScotlandYardGame createGame(List<Boolean> rounds, PlayerConfiguration mrX,
	                            PlayerConfiguration firstDetective,
	                            PlayerConfiguration... restOfTheDetectives) {
		return createGame(rounds, defaultGraph, mrX, firstDetective, restOfTheDetectives);
	}

	/**
	 * Like {@link #createGame(PlayerConfiguration, PlayerConfiguration, PlayerConfiguration...)}
	 * but with valid(no duplicate location/colour, not null, etc.) players already configured
	 *
	 * @return the created game with all six players
	 */
	ScotlandYardGame createValidSixPlayerGame() {
		return createGame(
				doNothingMrX(),
				doNothingRed(),
				doNothingGreen(),
				doNothingBlue(),
				doNothingWhite(),
				doNothingYellow());
	}

}
