package javax.safetycritical;

import javax.realtime.MemoryArea;
import javax.realtime.NoHeapRealtimeThread;
import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

public class ManagedThread extends NoHeapRealtimeThread implements ManagedSchedulable {

	StorageParameters storage;
	ScjProcess process;
	Mission mission = null;
	private PriorityScheduler sch;
	protected boolean isAutoStart = true;
	
	ManagedMemory privateMemory;
	MemoryArea currentMemory;
	MemoryArea topMemory;

	public ManagedThread(PriorityParameters priority, StorageParameters storage, String name) {
		this(priority, storage, null, name);
	}

	public ManagedThread(PriorityParameters priority, StorageParameters storage, Runnable logic, String name) {
		super(priority, logic, name);
		if (storage == null)
			throw new IllegalArgumentException("storage is null");
		
		this.storage = storage;
		this.mission = Mission.getCurrentMission();

		int backingStoreOfThisMemory = mission == null ? MemoryArea.getRemainingMemorySize() : (int) this.storage.totalBackingStore;
		ManagedMemory backingStoreProvider = mission == null ? null : mission.missionSeq.missionMemory;
		this.privateMemory = new PrivateMemory((int) this.storage.getMaxMemoryArea(), backingStoreOfThisMemory, backingStoreProvider);

		this.currentMemory = privateMemory;
		this.topMemory = privateMemory;
	}

	/**
	 * This method will cause the thread to execute i.e. put into the ready queue and wait for be scheduled. This is very similar with the release
	 * method for AperiodicEventHandler in the PriorityScheduler class.
	 */
	
	protected void start() {
		// to prevent the null pointer exception when the start method is called
		// after the thread is removed, especially for the OneShotThread.
		if (this.process == null) {
			return;
		}

		if (!isAutoStart && this.process.getState() == ScjProcess.State.BLOCKED) {
			vm.ClockInterruptHandler.instance.disable();
			process.setState(ScjProcess.State.READY);
			sch.insertReleaseQueue(process);
			vm.ClockInterruptHandler.instance.enable();
		}
	}

	public boolean isAutoStart() {
		return isAutoStart;
	}

	/**
	 * Registers this thread with the current mission. This method will just create a SCJProcess object for the thread, not put the thread in the
	 * schedule queue..
	 */
	@SCJAllowed(Level.INFRASTRUCTURE)
	@SCJRestricted(Phase.INITIALIZE)
	public final void register() {
		ManagedSchedulableSet hs = mission.MSSetForMission;
		hs.addMS(this);
		sch = PriorityScheduler.instance();
	}

	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(Phase.CLEANUP)
	public void cleanUp() {
		privateMemory.removeArea();
	}

	@Override
	public StorageParameters getStorageParameter() {
		return storage;
	}

	@Override
	public ManagedMemory getManagedMemory() {
		return privateMemory;
	}

	@Override
	public ScjProcess getScjProcess() {
		return process;
	}

	@Override
	public void setScjProcess(ScjProcess process) {
		this.process = process;
	}

	@Override
	public ReleaseParameters getReleaseParameter() {
		return null;
	}

	@Override
	public Mission getMission() {
		return mission;
	}

	@Override
	public void setMission(Mission m) {
		this.mission = m;
	}

	@Override
	public void signalTermination() {
	}

	@Override
	public void setCurrentMemory(MemoryArea current) {
		this.currentMemory = current;
	}

	@Override
	public MemoryArea getCurrentMemory() {
		return currentMemory;
	}

	@Override
	public void setTopMemory(MemoryArea topMemory) {
		this.topMemory = topMemory;
	}

	@Override
	public MemoryArea getTopMemory() {
		return this.topMemory;
	}

}