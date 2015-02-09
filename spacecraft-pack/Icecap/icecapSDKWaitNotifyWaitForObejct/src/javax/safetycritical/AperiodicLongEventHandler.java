package javax.safetycritical;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

public class AperiodicLongEventHandler extends ManagedLongEventHandler {
	private PriorityScheduler sch;

	public AperiodicLongEventHandler(PriorityParameters priority, AperiodicParameters release, StorageParameters storage, String name) {
		super(priority, release, storage, name);
	}

	@SCJAllowed(Level.INFRASTRUCTURE)
	@SCJRestricted(Phase.INITIALIZE)
	public final void register() {
		super.register();
		sch = PriorityScheduler.instance();
	}

	/**
	 * Release this aperiodic event handler
	 */
	@SCJAllowed
	public final void release(long data) {
		vm.ClockInterruptHandler.instance.disable();
		this.data = data;
		if (process.getState() == ScjProcess.State.EXECUTING) {
			; // do nothing, - is already running
		}

		else if (process.getState() == ScjProcess.State.BLOCKED) {
			process.setState(ScjProcess.State.READY);
			sch.insertReleaseQueue(process);
		} else {
			; // it is already ready
		}
		vm.ClockInterruptHandler.instance.enable();
	}
}
