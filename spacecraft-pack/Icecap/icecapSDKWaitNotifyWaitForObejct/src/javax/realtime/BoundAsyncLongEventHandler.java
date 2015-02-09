package javax.realtime;

public class BoundAsyncLongEventHandler extends AsyncLongEventHandler implements BoundAbstractAsyncEventHandler {
	/**
	 * The BoundAsyncLongEventHandler class is not directly available to the safety- critical Java application developers. Hence none of its methods
	 * or constructors are publicly available. This class differs from BoundAsyncEventHandler in that when it is released, a long integer is provided
	 * for use by the released event handler(s).
	 */

	protected BoundAsyncLongEventHandler(PriorityParameters priority, ReleaseParameters release, String name) {
		super(priority, release, name);
	}
}
