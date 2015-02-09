package javax.safetycritical;

import javax.realtime.AbsoluteTime;
import javax.realtime.AperiodicParameters;
import javax.realtime.HighResolutionTime;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;

public abstract class DeadlineMissHandler extends OneShotEventHandler {

	private AbsoluteTime CurrentDeadline = null;

	public DeadlineMissHandler(PriorityParameters priority, HighResolutionTime releaseTime, AperiodicParameters release, StorageParameters storage,
			String name) {
		super(priority, new RelativeTime(), release, storage, name);
	}

	public void scheduleNextReleaseTime(AbsoluteTime nextRelease) {
		this.CurrentDeadline = nextRelease;
	}

	public AbsoluteTime getNextDeadline() {
		return CurrentDeadline;
	}
}
