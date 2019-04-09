package uk.ac.bris.cs.scotlandyard.harness;

import java.util.List;

import uk.ac.bris.cs.scotlandyard.harness.TestHarness.AssertionContext;

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
class Interactions {
	private Interactions() {}

	static <T> void assertEach(AssertionContext sra, List<Requirement<T>> rs, T t) {
		rs.forEach(v -> v.check(sra, t));
	}

}
