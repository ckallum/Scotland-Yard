package uk.ac.bris.cs.gamekit.timer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface Timer {

	ScheduledFuture<?> schedule(long duration, TimeUnit unit, Runnable runnable);

	void stopAll();

}
