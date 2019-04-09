package uk.ac.bris.cs.gamekit.timer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StandardTimer implements Timer {

	private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

	public StandardTimer() {
		scheduler.setRemoveOnCancelPolicy(true);
	}

	@Override
	public ScheduledFuture<?> schedule(long duration, TimeUnit unit, Runnable runnable) {
		return scheduler.schedule(runnable, duration, unit);
	}

	@Override
	public void stopAll() {
		scheduler.getQueue().stream().map(ScheduledFuture.class::cast)
				.forEachOrdered(sf -> sf.cancel(true));
	}
}
