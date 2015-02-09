package javax.realtime;

/**
 * This class is a two dimension set, which contains pairs of clock and process. The infrastructure will use this class to model the wait set and lock
 * set.
 */
class PriorityQueue {
	int[] queue;
	int tail;

	PriorityQueue(int size) {
		queue = new int[size];
		tail = -1;
		makeEmptyQueue(queue);
	}

	private void makeEmptyQueue(int[] set) {
		for (int i = 0; i < set.length; i++)
			set[i] = -999;
	}

	/**
	 * Add a relation between the lock and the process into the set.
	 * 
	 * @param target
	 *            The lock associate with the process
	 * @param process
	 *            The process
	 */
	protected synchronized void addProcess(Object target, RtProcess process) {
		if (tail < queue.length - 1) {
			tail++;
			int index = tail;
			// find the place in the set for this process
			for (int i = 0; i < tail; i++) {
				RtProcess temp = getScjProcess(queue[i]);
				if (temp == null)
					throw new IllegalArgumentException("1");
				if (process.target.getPriorityParameter().getPriority() > temp.target.getPriorityParameter().getPriority()) {
					index = i;
					break;
				}
			}
			// reserve the place in the set for this process
			if (index != tail) {
				for (int i = tail; i > index; i--) {
					queue[i] = queue[i - 1];
				}
			}
			// add the index of the process into the set and set the required
			// lock
			process.lockRequired = target;
			queue[index] = process.index;
		} else {
			throw new IndexOutOfBoundsException("set: too small");
		}
	}

	/**
	 * Get the first process who needs to lock. Rules: 1. the highest priority process who needs the lock will be returned. 2. If there are more than
	 * one processes who have the same priority, then FIFO.
	 * 
	 * @param target
	 *            The lock
	 * @return The process who needs to lock.
	 */
	protected synchronized RtProcess getNextProcess(Object target) {
		for (int i = 0; i <= tail; i++) {
			RtProcess process = getScjProcess(queue[i]);
			if (process.lockRequired == target) {
				process.lockRequired = null;
				reorderSet(i);
				return process;
			}
		}
		return null;
	}

	public synchronized void removeProcess(RtProcess process) {
		for (int i = 0; i <= tail; i++) {
			if (queue[i] == process.index) {
				reorderSet(i);
				process.lockRequired = null;
			}
		}
	}

	private void reorderSet(int index) {
		for (int i = index; i <= tail - 1; i++) {
			queue[i] = queue[i + 1];
		}
		queue[tail] = -999;
		tail--;
	}

	private RtProcess getScjProcess(int processIdx) {
		if (processIdx == -999) {
			return null;
		}
		if (processIdx == -2) {
			return PriorityScheduler.instance().outerMostSeqProcess;
		}
		if (processIdx == -1) {
			return RtProcess.idleProcess;
		}

		int missionIndex = processIdx / 20;
		int scjProcessIndex = processIdx % 20;
		return PriorityScheduler.instance().getProcess(missionIndex, scjProcessIndex);
	}

	/**
	 * For testing purpose.
	 */
	public void print() {
		devices.Console.println("Set size = " + (tail + 1));
		for (int i = 0; i <= tail; i++) {
			RtProcess temp = getScjProcess(queue[i]);
			if (temp != null)
				devices.Console.println(temp.print());
		}

		for (int i = 0; i < queue.length; i++) {
			devices.Console.print("[ " + queue[i] + " ] ");
		}
		devices.Console.println("");
	}
}
