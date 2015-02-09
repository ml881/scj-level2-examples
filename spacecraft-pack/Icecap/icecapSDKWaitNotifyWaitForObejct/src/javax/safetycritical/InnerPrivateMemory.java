package javax.safetycritical;

public class InnerPrivateMemory extends ManagedMemory {  // HSO: not public

	ManagedMemory prev;

	public InnerPrivateMemory(int size, int BackingStoreOfThisMemory, ManagedMemory backingStoreProvider) {
		super(size, BackingStoreOfThisMemory, backingStoreProvider);
	}

}
