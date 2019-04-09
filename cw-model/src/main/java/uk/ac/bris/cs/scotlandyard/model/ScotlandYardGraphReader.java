package uk.ac.bris.cs.scotlandyard.model;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.ImmutableGraph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.gamekit.graph.UndirectedGraph;

//TODO test should be in same module, currently in scotlandyard-model
/**
 * A collection of utility methods that reads in the a scotlandyard node map
 */
public class ScotlandYardGraphReader {

	private static final String DELIMITER = " ";

	private ScotlandYardGraphReader() {
		// nope
	}

	/**
	 * Converts lines of strings into a Scotland Yard game map
	 * 
	 * @param lines the lines
	 * @return a graph for {@link ScotlandYardGame} to use
	 */
	public static ImmutableGraph<Integer, Transport> fromLines(List<String> lines) {
		if (lines == null) throw new NullPointerException("lines == null");
		if (lines.isEmpty()) throw new IllegalArgumentException("Lines must not be empty!");

		String[] topLine = parseLine(0, lines, DELIMITER, 2);
		int numberOfNodes;
		int numberOfEdges;
		try {
			numberOfNodes = Integer.parseInt(topLine[0]);
			numberOfEdges = Integer.parseInt(topLine[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid symbol at line 1:\n\t" + lines.get(0));
		}

		if (numberOfNodes + numberOfEdges > lines.size() - 1)
			throw new IllegalArgumentException("Line count < (edge count + node count)");

		UndirectedGraph<Integer, Transport> graph = new UndirectedGraph<>();

		// we read the first line already
		for (int i = 1; i <= numberOfNodes; i++) {
			if (lines.get(i).isEmpty())
				throw new IllegalArgumentException("Expected non-empty line at line " + i);
			String value = parseLine(i, lines, DELIMITER, 1)[0];
			try {
				graph.addNode(new Node<>(Integer.parseInt(value)));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Expected integer at line " + i + "\n\t" + lines.get(i));
			}
		}

		for (int i = numberOfNodes + 1; i <= numberOfNodes + numberOfEdges; i++) {
			if (lines.get(i).isEmpty())
				throw new IllegalArgumentException("Expected non-empty line at line " + i);
			String[] segments = parseLine(i, lines, DELIMITER, 3);

			Node<Integer> source;
			Node<Integer> destination;
			Transport data;
			try {
				source = graph.getNode(Integer.parseInt(segments[0]));
				destination = graph.getNode(Integer.parseInt(segments[1]));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Expected integer at line " + i + "\n\t" + lines.get(i));
			}
			try {
				data = Transport.valueOf(segments[2].toUpperCase(Locale.ENGLISH));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Expected enum with value of " + Arrays.toString(Transport.values())
								+ " at line " + i + "\n\t" + lines.get(i));
			}
			if (source == null) throw new IllegalArgumentException(
					"Expected source node to exist in graph  at line " + i + "\n\t" + lines.get(i));
			if (destination == null) throw new IllegalArgumentException(
					"Expected destination node to exist in graph  at line " + i + "\n\t"
							+ lines.get(i));
			graph.addEdge(new Edge<>(source, destination, data));
		}
		return new ImmutableGraph<>(graph);
	}

	private static String[] parseLine(int line, List<String> lines, String delimiter,
			int expectedSegments) {
		String currentLine = lines.get(line);
		if (currentLine == null) throw new NullPointerException("Line " + line + " is null");
		String[] segments = currentLine.split(Pattern.quote(delimiter));
		if (segments.length != expectedSegments) throw new IllegalArgumentException(
				"Expected " + expectedSegments + " occurrences of delimiter \"" + delimiter
						+ "\" on line " + line + ":\n\t" + line);
		return segments;

	}

}
