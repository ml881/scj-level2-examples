package javax.safetycritical;

import icecaptools.IcecapCompileMe;
import vm.InterruptHandler;

final class Monitor extends javax.realtime.Monitor{
	private int ceiling;
	private int synchCounter;
	private int priority;
	private ManagedSchedulable owner;
	private InterruptHandler clock;

	protected Monitor(int ceiling) {
		this.ceiling = ceiling;
		clock = vm.ClockInterruptHandler.instance;
	}
	
	@IcecapCompileMe
	@Override
	protected void lock() {
		clock.disable();
		ManagedSchedulable ms = PriorityScheduler.instance().getCurrentProcess().getTarget();
		if (owner == null) {
			owner = ms;
		}
		if (owner == ms) {
			synchCounter++;
			if (synchCounter == 1) {
				priority = ms.getPriorityParameter().getPriority();
				ms.getPriorityParameter().setPriority(max(priority, ceiling) + 1);
			}
		} else {
			// insert the process to the lock set.
			PriorityScheduler.instance().addProcessToLockQueue(this, ms.getScjProcess());

			// get the next process and set the state.
			PriorityScheduler.instance().moveToNext();

			// transfer to the process
			vm.ClockInterruptHandler.instance.enable();
			vm.ClockInterruptHandler.instance.handle();
		}
		clock.enable();
	}

	@IcecapCompileMe
	@Override
	protected void unlock() {
		clock.disable();
		ManagedSchedulable ms = PriorityScheduler.instance().getCurrentProcess().getTarget();

		if (owner == ms) {
			synchCounter--;
			if (synchCounter == 0) {
				ms.getPriorityParameter().setPriority(priority);
				// get the next process that needs the lock in lock set and
				// assign the lock to this process.
				ScjProcess process = PriorityScheduler.instance().getProcessFromLockQueue(this);;
				if (process != null) {
					owner = process.getTarget();
					synchCounter++;
					priority = owner.getPriorityParameter().getPriority();
					owner.getPriorityParameter().setPriority(max(priority, ceiling) + 1);
					PriorityScheduler.instance().insertReleaseQueue(process);
				} else {
					owner = null;
				}
				clock.enable();
				clock.handle();
			}
		} else {
			clock.enable();
			throw new IllegalMonitorStateException();
		}
	}

	protected void lockWithOutEnable() {
		vm.ClockInterruptHandler.instance.disable();
		ManagedSchedulable ms = PriorityScheduler.instance().getCurrentProcess().getTarget();
		if (owner == null) {
			owner = ms;
		}

		if (owner == ms) {
			synchCounter++;
			if (synchCounter == 1) {
				priority = owner.getPriorityParameter().getPriority();
				owner.getPriorityParameter().setPriority(max(priority, ceiling) + 1);
			}
		} else {
			// insert the process to the lock set.
			PriorityScheduler.instance().addProcessToLockQueue(this, ms.getScjProcess());

			// get the next process and set the state.
			PriorityScheduler.instance().moveToNext();

			// transfer to the process
			vm.ClockInterruptHandler.instance.enable();
			vm.ClockInterruptHandler.instance.handle();
		}
	}

	protected void unlockWithOutEnable() {
		ManagedSchedulable ms = PriorityScheduler.instance().getCurrentProcess().getTarget();

		if (owner == ms) {
			synchCounter--;
			if (synchCounter == 0) {
				ms.getPriorityParameter().setPriority(priority);
				// get the next process that needs the lock in lock set and
				// assign the lock to this process.
				ScjProcess nextProcess = PriorityScheduler.instance().getProcessFromLockQueue(this);
				if (nextProcess != null) {
					owner = nextProcess.getTarget();
					synchCounter++;
					priority = owner.getPriorityParameter().getPriority();
					owner.getPriorityParameter().setPriority(max(priority, ceiling) + 1);
					PriorityScheduler.instance().insertReleaseQueue(nextProcess);
				} else {
					owner = null;
				}
			}
		} else {
			clock.enable();
			throw new IllegalMonitorStateException();
		}
	}
	
	private static int max(int x, int y) {
		if (x > y)
			return x;
		else
			return y;
	}

}
