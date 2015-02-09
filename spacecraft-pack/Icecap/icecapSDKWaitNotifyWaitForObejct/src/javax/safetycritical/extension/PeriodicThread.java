package javax.safetycritical.extension;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;
import javax.safetycritical.DeadlineMissHandler;
import javax.safetycritical.Services;
import javax.safetycritical.StorageParameters;

public abstract class PeriodicThread extends ManagedRealtimeThread {

	private AbsoluteTime nextRelease;
	private AbsoluteTime nextDeadline;
	private int period;
	private int deadline;
	private DeadlineMissHandler deadlineMissDetection;
	boolean started = false;

	public PeriodicThread(PriorityParameters priority, StorageParameters storage, int peroid, int deadline,
			DeadlineMissHandler deadlineMissDetection, boolean isAutoStart, String name) {
		super(priority, storage, isAutoStart, name);
		this.period = peroid;
		this.deadline = deadline;
		nextRelease = new AbsoluteTime();
		nextDeadline = new AbsoluteTime();
		this.deadlineMissDetection = deadlineMissDetection;
	}

	public synchronized void firstRelease() {
		// Services.notify(this);
		nextRelease = Clock.getRealtimeClock().getTime(nextRelease);
		nextDeadline.set(nextRelease.getMilliseconds() + deadline);
		if (this.deadlineMissDetection != null) {
			deadlineMissDetection.scheduleNextReleaseTime(nextDeadline);
		}
	}

	/**
	 * the Services.wait method will not throw InterruptedException. It also seems that the wait and notify is not implemented.
	 * 
	 * @return
	 */
	// ** private synchronized boolean waitFirstRelease() {
	// ** try {
	// ** Services.wait(this);
	// ** } catch (InterruptedException e) {
	// ** return false;
	// ** }
	// ** return true;
	// ** }

	@Override
	public void run() {
		firstRelease();
		// ** if (waitFirstRelease()) {
		while (!this.getMission().terminationPending()) {
			nextRelease = nextRelease.add(period, 0);
			work();
			nextDeadline = nextDeadline.add(period, 0);
			if (this.deadlineMissDetection != null) {
				deadlineMissDetection.scheduleNextReleaseTime(nextDeadline);
			}
			Services.delay(nextRelease); // waitForNextPeriod
		}
		// ** }
	}

	@Override
	public ReleaseParameters getReleaseParameter() {
		return null;
	}

}
