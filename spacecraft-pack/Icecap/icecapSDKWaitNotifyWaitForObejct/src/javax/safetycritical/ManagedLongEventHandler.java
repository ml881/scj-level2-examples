package javax.safetycritical;

import javax.realtime.AbsoluteTime;
import javax.realtime.BoundAsyncLongEventHandler;
import javax.realtime.MemoryArea;
import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

public abstract class ManagedLongEventHandler extends BoundAsyncLongEventHandler implements ManagedSchedulable {

	StorageParameters storage;
	Mission mission = null;
	/**
	 * The memory area in which the handler will execute
	 */
	ManagedMemory privateMemory;
	MemoryArea currentMemory;
	MemoryArea topMemory;
	/**
	 * Process for use by scheduler
	 */
	ScjProcess process;

	public ManagedLongEventHandler(PriorityParameters priority, ReleaseParameters release, StorageParameters storage, String name) {
		super(priority, release, name);
		if (storage == null)
			throw new IllegalArgumentException("storage is null");

		this.storage = storage;
		this.mission = Mission.getCurrentMission();
		
		int backingStoreOfThisMemory = (int) this.storage.totalBackingStore;
		ManagedMemory backingStoreProvider = mission == null ? null : mission.missionSeq.missionMemory;
		this.privateMemory = new PrivateMemory((int) this.storage.getMaxMemoryArea(), backingStoreOfThisMemory, backingStoreProvider);

		this.currentMemory = privateMemory;
		this.topMemory = currentMemory;
	}

	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(Phase.CLEANUP)
	public void cleanUp() {
		privateMemory.removeArea();
	}

	/**
	 * Registers this event handler with the current mission.
	 */
	@SCJAllowed(Level.INFRASTRUCTURE)
	@SCJRestricted(Phase.INITIALIZE)
	public void register() {
		ManagedSchedulableSet hs = mission.MSSetForMission;
		hs.addMS(this);
	}

	@SCJAllowed(Level.LEVEL_1)
	public AbsoluteTime getLastReleaseTime() {
		// ToDo: implementation
		return null;
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
	public void setScjProcess(ScjProcess scjProcess) {
		this.process = scjProcess;
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
