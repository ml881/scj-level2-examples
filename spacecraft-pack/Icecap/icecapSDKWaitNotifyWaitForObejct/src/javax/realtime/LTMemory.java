package javax.realtime;

public class LTMemory extends ScopedMemory {

	public LTMemory(int size, int BackingStoreOfThisMemory, MemoryArea backingStoreProvider) {
		super(size, BackingStoreOfThisMemory, backingStoreProvider);
	}

	@Override
	public long memoryConsumed() {
		return (long) delegate.consumedMemory();
	}

	@Override
	public long memoryRemaining() {
		return size() - memoryConsumed();
	}

	@Override
	public long size() {
		return this.delegate.getSize();
	}
}
