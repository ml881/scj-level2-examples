package javax.safetycritical.extension;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;
import javax.safetycritical.StorageParameters;

public abstract class AperiodicThread extends ManagedRealtimeThread {

	AperiodicParameters releaseParameter;

	public AperiodicThread(PriorityParameters priority, StorageParameters storage, AperiodicParameters release, boolean isAutoStart, String name) {
		super(priority, storage, isAutoStart, name);

		if (priority == null || release == null)
			throw new IllegalArgumentException("null argument");

		this.releaseParameter = release;
	}

	@Override
	public void run() {
		work();
	}

	@Override
	public ReleaseParameters getReleaseParameter() {
		return releaseParameter;
	}
}
