package javax.safetycritical.extension;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

public abstract class ManagedRealtimeThread extends ManagedThread {

	public ManagedRealtimeThread(PriorityParameters priority, StorageParameters storage, boolean isAutoStart, String name) {
		this(priority, storage, isAutoStart, null, name);
	}

	public ManagedRealtimeThread(PriorityParameters priority, StorageParameters storage, boolean isAutoStart, Runnable logic, String name) {
		super(priority, storage, logic, name);
		this.isAutoStart = isAutoStart;
	}

	public abstract void work();
	
	public void start(){
		super.start();
	}

}
