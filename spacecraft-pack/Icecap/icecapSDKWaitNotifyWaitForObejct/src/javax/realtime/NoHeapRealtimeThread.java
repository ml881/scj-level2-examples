package javax.realtime;

public class NoHeapRealtimeThread extends RealtimeThread{

	protected NoHeapRealtimeThread(PriorityParameters priority, Runnable logic, String name) {
		super(priority, logic, name);
	}

}
