package javax.realtime;

public abstract class ScopedMemory extends MemoryArea {

	public ScopedMemory(int size, int BackingStoreOfThisMemory, MemoryArea backingStoreProvider) {
		super(size, BackingStoreOfThisMemory, backingStoreProvider);
	}

}
