package uk.ac.bris.cs.scotlandyard.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import uk.ac.bris.cs.gamekit.graph.Graph;

/**
 * An interface that represents a view of a Scotland Yard game.
 */
public interface ScotlandYardView {

	/**
	 * Initial round value immediately after the game is constructed, see
	 * {@link #getCurrentRound()}
	 */
	int NOT_STARTED = 0;

	/**
	 * A list of the colours of players who are playing the game in the initial
	 * order of play. The length of this list should be the number of players
	 * that are playing, the first element should be Colour.BLACK, since Mr X
	 * always starts.
	 *
	 * @return An immutable list of players; never empty and never null
	 */
	List<Colour> getPlayers();

	/**
	 * Returns the colours of the winning players or an empty set if no players
	 * have won yet. If Mr X it should contain a single colour, else it should
	 * send the list of detective colours
	 *
	 * @return An immutable set containing the colours of the winning players;
	 * could be empty but never null
	 */
	Set<Colour> getWinningPlayers();

	/**
	 * The location of a player with a given colour in its last known location.
	 *
	 * @param colour The colour of the player whose location is requested; not
	 * null
	 * @return The location of the player whose location is requested. If BLACK,
	 * then this returns 0 if MrX has never been revealed, otherwise
	 * returns the location of MrX in his last known location. MrX is
	 * revealed in round n where calling {@link #getRounds()} with n
	 * returns true. Only returns empty if the game does not have a player with the specified
	 * colour
	 */
	Optional<Integer> getPlayerLocation(Colour colour);

	/**
	 * The number of a particular ticket that a player with the specified colour
	 * has.
	 *
	 * @param colour The colour of the player whose tickets are requested; not
	 * null
	 * @param ticket The type of tickets that is being requested; not null
	 * @return The number of tickets of the given player, zero or greater. Only returns empty if
	 * the game does not have a player with the specified colour
	 */
	Optional<Integer> getPlayerTickets(Colour colour, Ticket ticket);

	/**
	 * The game is over when Mr.X has been found or that all detectives are
	 * stuck.
	 *
	 * @return true when the game is over, false otherwise.
	 */
	boolean isGameOver();

	/**
	 * The player whose turn it is. Should be {@link Colour#BLACK} at the start
	 * of game
	 *
	 * @return The colour of the current player; never null
	 */
	Colour getCurrentPlayer();

	/**
	 * The round number is determined by the number of moves MrX has played.
	 * Initially this value is {@link #NOT_STARTED}, and is incremented for each
	 * move MrX makes. A {@link DoubleMove} counts as two moves.
	 *
	 * @return the number of moves MrX has played; zero or greater
	 */
	int getCurrentRound();

	/**
	 * An immutable list whose length is the maximum number of moves that MrX
	 * can play in a game. True means that Mr.X reveals its location where as
	 * False conceals it.
	 * *
	 *
	 * @return an immutable list of booleans that indicate the turns where MrX
	 * reveals himself; never empty and never null
	 */
	List<Boolean> getRounds();

	/**
	 * An immutable view of the graph the game is using.
	 *
	 * @return an immutable graph; never empty and never null
	 */
	Graph<Integer, Transport> getGraph();

}
