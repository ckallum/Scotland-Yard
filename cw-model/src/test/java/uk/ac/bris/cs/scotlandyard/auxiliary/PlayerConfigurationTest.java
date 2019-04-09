package uk.ac.bris.cs.scotlandyard.auxiliary;

import org.junit.Test;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.PlayerConfiguration;
import uk.ac.bris.cs.scotlandyard.model.PlayerConfiguration.Builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.ac.bris.cs.scotlandyard.auxiliary.TestGames.mrXTickets;

/**
 * Tests for {@link PlayerConfiguration}
 */
public class PlayerConfigurationTest {

	@Test
	public void testProducesCorrectOutput() {
		Player player = (v, l, moves, callback) -> callback.accept(moves.iterator().next());
		PlayerConfiguration configuration = new Builder(Colour.BLACK).using(player)
				.with(mrXTickets()).at(10).build();
		assertThat(configuration.colour).isEqualTo(Colour.BLACK);
		assertThat(configuration.player).isSameAs(player);
		assertThat(configuration.tickets).isEqualTo(mrXTickets());
		assertThat(configuration.location).isEqualTo(10);
	}

	@Test
	public void testNullColourThrows() {
		assertThatThrownBy(() -> new Builder(null))
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	public void testMissingPlayerThrows() {
		assertThatThrownBy(() -> new Builder(Colour.BLACK).with(mrXTickets()).build())
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	public void testMissingTicketThrows() {
		Player player = (v, l, moves, callback) -> callback.accept(moves.iterator().next());
		assertThatThrownBy(() -> new Builder(Colour.BLACK).using(player).build())
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	public void testNullPlayerThrows() {
		assertThatThrownBy(() -> new Builder(Colour.BLACK).using(null).with(mrXTickets()).build())
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	public void testNullTicketThrows() {
		Player player = (v, l, moves, callback) -> callback.accept(moves.iterator().next());
		assertThatThrownBy(() -> new Builder(Colour.BLACK).using(player).with(null).build())
				.isInstanceOf(NullPointerException.class);
	}

}
