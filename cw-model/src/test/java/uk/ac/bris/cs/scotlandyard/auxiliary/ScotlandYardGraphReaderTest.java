package uk.ac.bris.cs.scotlandyard.auxiliary;

import org.junit.Test;

import uk.ac.bris.cs.gamekit.graph.ImmutableGraph;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGraphReader;
import uk.ac.bris.cs.scotlandyard.model.Transport;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYardGraphReader.*;

/**
 * Tests for {@link ScotlandYardGraphReader}
 */
public class ScotlandYardGraphReaderTest {

	@Test
	public void testValidFile() {
		ImmutableGraph<Integer, Transport> graph =
				fromLines(asList("3 1", "1", "2", "3", "1 2 Ferry"));
		assertThat(graph.getNodes()).hasSize(3);
		assertThat(graph.getEdges()).hasSize(2);
		assertThat(graph.getNode(1)).isNotNull();
		assertThat(graph.getEdges().iterator().next().data())
				.isEqualByComparingTo(Transport.FERRY);
	}

	@Test
	public void testEmptyInputShouldThrow() {
		assertThatThrownBy(() -> fromLines(emptyList()));
	}

	@Test
	public void testBadFirstLine() {
		assertThatThrownBy(() -> fromLines(singletonList("Foo Bar Baz")));
	}

	@Test
	public void testBadNodeCount() {
		assertThatThrownBy(() -> fromLines(asList("4 1", "1", "2", "3", "1 2 Ferry")));
	}

	@Test
	public void testBadEdgeCount() {
		assertThatThrownBy(() -> fromLines(asList("3 5", "1", "2", "3", "1 2 Ferry")));
	}

	@Test
	public void testBadNode() {
		assertThatThrownBy(() -> fromLines(asList("1 0", "Foo")));
	}

	@Test
	public void testBadEdge() {
		assertThatThrownBy(() -> fromLines(asList("2 1", "1", "2", "Foo Bar Baz")));
	}

}