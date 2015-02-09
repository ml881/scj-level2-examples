package javax.realtime;

public class AsyncLongEventHandler extends AbstractAsyncEventHandler {
	/**
	 * In SCJ, all asynchronous events must have their handlers bound when they are created (during the initialization phase). The binding is
	 * permanent. Thus, the AsyncLongEventHandler constructors are hidden from public view in the SCJ specification. This class differs from
	 * AsyncEventHandler in that when it is fired, a long integer is provided for use by the released event handler(s).
	 */

	String name;
	PriorityParameters priority;
	ReleaseParameters release;
	protected long data = 0L;

	protected AsyncLongEventHandler(PriorityParameters priority, ReleaseParameters release, String name) {
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
		handleAsyncEvent(data);
	}

	/**
	 * This method must be overridden by the application to provide the handling code.
	 * 
	 * @param data
	 *            is the data that was passed when the associated event handler was released.
	 */
	public void handleAsyncEvent(long data) {
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
