package javax.realtime;

public class AsyncEventHandler extends AbstractAsyncEventHandler {
	/**
	 * In SCJ, all asynchronous events must have their handlers bound to a thread when they are created (during the initialization phase). The binding
	 * is perma- nent. Thus, the AsyncEventHandler constructors are hidden from public view in the SCJ specification.
	 */
	String name;
	PriorityParameters priority;
	ReleaseParameters release;

	protected AsyncEventHandler(PriorityParameters priority, ReleaseParameters release, String name) {
		if (priority == null)
			throw new IllegalArgumentException("priority is null");
		if (release == null)
			throw new IllegalArgumentException("release is null");
		this.name = name;
		this.priority = priority;
		this.release = release;
	}

	@Override
	public void run() {
		handleAsyncEvent();
	}

	/**
	 * This method must be overridden by the application to provide the handling code.
	 */
	public void handleAsyncEvent() {
	}

	@Override
	public PriorityParameters getPriorityParameter() {
		return priority;
	}

	@Override
	public ReleaseParameters getReleaseParameter() {
		return release;
	}

	@Override
	public String getName() {
		return name;
	}

}
