package uk.ac.bris.cs.scotlandyard.harness;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.security.Permission;
import java.util.List;

/**
 * This is an internal class designed only for use with the test harness. This class is not
 * stable and may change anytime without notice.
 */
final class Assertions {
	private Assertions() {}

	static void assertFailure(String message, List<StackTraceElement> stack,
	                          String callingClass) {
		int cl = Iterables.indexOf(stack, s -> s.getClassName().equals(callingClass));
		if (cl < 0) {
			AssertionError cause = new AssertionError();
			cause.setStackTrace(stack.toArray(new StackTraceElement[]{}));
			throw new AssertionError("Unable to derive calling class location in stacktrace, " +
					"the assumption that calling class will always be at index 1 is violated!",
					cause);
		}
		final StackTraceElement[] stackTrace = stack.stream()
				.skip(cl)
				.toArray(StackTraceElement[]::new);
		final StackTraceElement stackHead = stackTrace[0];
		final String className = stackHead.getClassName();
		AssertionError error = new AssertionError(String.format("Error at %s.%s(%s:%d):\n\n%s",
				className.substring(className.lastIndexOf('.') + 1),
				stackHead.getMethodName(),
				stackHead.getFileName(),
				stackHead.getLineNumber(),
				message));
		error.setStackTrace(stackTrace);
		throw error;
	}

	static List<StackTraceElement> capture() {
		return ImmutableList.copyOf(Thread.currentThread().getStackTrace());

	}

	static void disableSystemExit() {
		System.setSecurityManager(new SecurityManager() {
			@Override public void checkPermission(Permission perm) {
				if (perm.getName().startsWith("exitVM")) {
					throw new AssertionError("Calling System.exit() is not allowed");
				}
			}
		});
	}

	static void enableSystemExit() {
		System.setSecurityManager(null);
	}

}
