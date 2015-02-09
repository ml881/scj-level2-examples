package javax.safetycritical.extension;

import javax.realtime.AbsoluteTime;
import javax.realtime.AperiodicParameters;
import javax.realtime.Clock;
import javax.realtime.HighResolutionTime;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;
import javax.safetycritical.Services;
import javax.safetycritical.StorageParameters;

public abstract class OneShotThread extends ManagedRealtimeThread {

	private HighResolutionTime startTime;
	private AperiodicParameters release;

	public OneShotThread(PriorityParameters priority, StorageParameters storage, AperiodicParameters release, HighResolutionTime startTime,
			boolean isAutoStart, String name) {
		super(priority, storage, isAutoStart, name);
		this.release = release;
		this.startTime = startTime;
	}

	@Override
	public final void run() {
		if (startTime instanceof AbsoluteTime) {
			Services.delay(new AbsoluteTime(((AbsoluteTime) startTime)));
		}
		if (startTime instanceof RelativeTime) {
			Services.delay(new AbsoluteTime(startTime.getMilliseconds(), startTime.getNanoseconds(), Clock.getRealtimeClock()));
		}
		work();
	}

	@Override
	public ReleaseParameters getReleaseParameter() {
		return release;
	}

	@Override
	public final void cleanUp() {
		super.cleanUp();
	}

	public HighResolutionTime getStartTime() {
		return startTime;
	}
}
