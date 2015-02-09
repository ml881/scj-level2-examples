package javax.realtime;

import vm.ClockInterruptHandler;

public class RealtimeThread implements Schedulable {
	
	Runnable logic;
	PriorityParameters priority;
	String name;

	protected RealtimeThread(PriorityParameters priority, Runnable logic, String name) {
		//super();
		if (priority == null)
			throw new IllegalArgumentException("priority is null");
		
		this.priority = priority;
		this.logic = logic;
		this.name = name;
	}

	@Override
	public void run() {
		if (logic != null) {
			logic.run();
		}
	}

	@Override
	public PriorityParameters getPriorityParameter() {
		return priority;
	}

	@Override
	public ReleaseParameters getReleaseParameter() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	public void sleep(HighResolutionTime time) throws java.lang.InterruptedException {
		vm.ClockInterruptHandler.instance.disable();
		// get current process and reset the boolean value
		RtProcess current = PriorityScheduler.instance().current;
		// get current time.
		AbsoluteTime abs = Clock.getRealtimeClock().getTime(current.next);

		// set the next release time for current process
		if (time instanceof RelativeTime) {
			current.next = abs.add((RelativeTime) time, abs);
		} else if (time instanceof AbsoluteTime) {
			current.next = new AbsoluteTime((AbsoluteTime) time);
		} else {
			throw new UnsupportedOperationException();
		}

		// get the next process and set appropriate state.
		RtProcess nextProcess = PriorityScheduler.instance().pFrame.queue.extractMin();
		nextProcess.state = RtProcess.State.EXECUTING;
		PriorityScheduler.instance().current = nextProcess;
		// insert the current process into the the release queue
		PriorityScheduler.instance().pFrame.queue.insert(current);

		// transfer to the current process
		vm.ClockInterruptHandler.instance.enable();
		ClockInterruptHandler.instance.yield();
	}

}
