/**************************************************************************
 * File name  : MemoryArea.java
 * 
 * This file is part a SCJ Level 0 and Level 1 implementation, 
 * based on SCJ Draft, Version 0.94 25 June 2013.
 *
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as  
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This SCJ Level 0 and Level 1 implementation is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the  
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this SCJ Level 0 and Level 1 implementation.  
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2012 
 * @authors  Anders P. Ravn, Aalborg University, DK
 *           Stephan E. Korsholm and Hans S&oslash;ndergaard, 
 *             VIA University College, DK
 *************************************************************************/

package javax.realtime;

import javax.safetycritical.ManagedSchedulable;
import javax.safetycritical.Services;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import reflect.ObjectInfo;
import vm.Memory;

/**
 * All allocation contexts are implemented by memory areas. This is the base-level class for all memory areas.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 */

@SCJAllowed(Level.INFRASTRUCTURE)
public abstract class MemoryArea implements AllocationContext {
	/** Singleton reference for the immortal memory. */
	protected static MemoryArea head;

	protected static int OverAllBackingStoreBase;
	protected static int OverAllBackingStoreEnd;

	private static boolean flag = true;

	private int remainingBackingstoreSize;
	private int backingstoreBase;
	private int backingstoreEnd;

	protected MemoryArea backingStoreProvider;

	/**
	 * link for an open list of allocated areas. The list head is <code>head</code>.
	 */
	protected MemoryArea next;

	protected Memory delegate;

	/**
	 * Dummy constructor for ImmortalMemory
	 */
	MemoryArea() {
	}

	/**
	 * Creates a new memory area.
	 * 
	 * @param size
	 *            The size of this new memory area.
	 */
	/*
	 * @ behavior requires size > 0; ensures size() == size && memoryConsumed() == 0;
	 * 
	 * @
	 */
	protected MemoryArea(int size, int backingstoreSize, MemoryArea backingStoreProvider) {
		// get the base of this memory
		int base = getBase(backingstoreSize, backingStoreProvider);
		// get the backing store end of its backingStore provider
		int backingStoreEnd = backingStoreProvider != null ? backingStoreProvider.getBackingstoreEnd() : OverAllBackingStoreEnd;

		if (backingstoreSize - size >= 0 && base + backingstoreSize <= backingStoreEnd) {
			this.backingStoreProvider = backingStoreProvider;

			delegate = new Memory(base, size);
			next = null;

			if (backingStoreProvider != null) {
				backingStoreProvider.setRemainingBackingstoreSize(backingStoreProvider.getRemainingBackingstoreSize() - backingstoreSize);
			}

			backingstoreBase = this.delegate.getBase();
			backingstoreEnd = backingstoreBase + backingstoreSize;
			this.remainingBackingstoreSize = backingstoreSize - size;
			linkMemoryArea(this);
		} else {
			throw new OutOfMemoryError("thrown from MemoryArea :: constructor : Out of backingstore exception: size: " + size
					+ " backingStoreSize: " + backingstoreSize + " base: " + base + " backingStoreEnd: " + backingStoreEnd);
		}
	}

	/**
	 * Finds the base for a new allocation
	 * 
	 * @return the base for a further allocation
	 */
	private int getBase(int backingstoreSize, MemoryArea backingStoreProvider) {
		// the backing store base and end of the backingStore provider.
		int endRange = backingStoreProvider == null ? OverAllBackingStoreEnd : backingStoreProvider.backingstoreEnd;
		int base = backingStoreProvider == null ? OverAllBackingStoreBase : backingStoreProvider.backingstoreBase;

		// get first memory are in the range of the backingStore. This could be
		// the backingStore provider or the immortal memory.
		MemoryArea first = backingStoreProvider == null ? head : backingStoreProvider;

		while (first != null) {
			// get a new backingStore base
			int baseRange = backingStoreProvider == first ? first.delegate.getSize() + first.delegate.getBase() : first.backingstoreEnd;
			// get the first memory in the new range.
			MemoryArea next = getFirstMemoryInRange(baseRange, endRange);

			// check whether there is a gap between the current memory and the
			// first memory in the range. If so and the gap is not less than the
			// required size, then the base of the range will be returned.
			if (next != null && next.delegate.getBase() - baseRange >= backingstoreSize) {
				return baseRange;
			}

			base = baseRange;
			first = next;
		}
		return base;
	}

	/**
	 * get the first memory area in the given range.
	 * 
	 * @param base
	 *            the base of the range.
	 * @param end
	 *            the end of the range.
	 * @return the first memory in this range.
	 */
	private MemoryArea getFirstMemoryInRange(int base, int end) {
		MemoryArea current = head;
		while (current != null) {
			if (current.delegate.getBase() >= base && (current.delegate.getBase() + current.delegate.getSize()) <= end) {
				return current;
			}
			current = current.next;
		}
		return null;
	}

	/**
	 * Appends an area to the end of the open list with head <code>head</code>.
	 * 
	 * @param memoryArea
	 *            is the area to be appended to the list.
	 */
	private static void linkMemoryArea(MemoryArea memoryArea) {
		if (head == null) {
			head = memoryArea;
		} else {
			MemoryArea current = head;
			while (current.next != null && current.next.delegate.getBase() < memoryArea.delegate.getBase()) {
				current = current.next;
			}
			MemoryArea temp = current.next;
			current.next = memoryArea;
			memoryArea.next = temp;
		}
	}

	/**
	 * Removes <code>this</code> area from the open list with head <code>head</code>. The head is ImmortalMemory and is never removed.
	 */
	protected void removeMemArea() {
		if (this != head) {
			MemoryArea current = head;
			while (current.next != this) {
				current = current.next;
			}
			current.next = next;
			if (backingStoreProvider != null) {
				backingStoreProvider.setRemainingBackingstoreSize(backingStoreProvider.getRemainingBackingstoreSize() + backingstoreEnd
						- backingstoreBase);
			}
		}
	}

	/**
	 * @param object
	 *            An object.
	 * @return The memory area in which <code>object</code> is allocated.
	 */
	/*
	 * @ public behavior requires object != null; ensures \result != null; // is tested elsewhere, see Test Suite paper, section 3.3
	 * 
	 * @
	 */
	@SCJAllowed
	public static MemoryArea getMemoryArea(Object object) {
		int ref = ObjectInfo.getAddress(object);
		MemoryArea current = head;
		while (current != null) {
			if ((current.delegate.getBase() <= ref) && (ref < current.delegate.getBase() + current.delegate.getSize())) {
				return current;
			}
			current = current.next;
		}
		return null;
	}

	/**
	 * @return The memory consumed (in bytes) in this memory area.
	 */
	/*
	 * @ public behaviour requires true; assignable \nothing;
	 * 
	 * @
	 */
	@SCJAllowed
	public abstract long memoryConsumed();

	/**
	 * @return The memory remaining (in bytes) in this memory area.
	 */
	/*
	 * @ public behaviour requires true; assignable \nothing;
	 * 
	 * @
	 */
	@SCJAllowed
	public abstract long memoryRemaining();

	/**
	 * @return The size of the current memory area in bytes.
	 */
	@SCJAllowed
	/*
	 * @ public behaviour requires true; assignable \nothing;
	 * 
	 * @
	 */
	public abstract long size();

	protected void resizeMemArea(long newSize) {

		// to resize a memory, the new size should be more than the current
		// consumed size and less than its backing store size. It's backing
		// store should be empty also.
		if (memoryConsumed() <= newSize && (this.delegate.getBase() + newSize) <= backingstoreEnd
				&& remainingBackingstoreSize == getBackingStoreSize() - size()) {
			// change its remaining backing store size
			// backingstoreBase = backingstoreBase - (int) (delegate.getSize() -
			// newSize);
			remainingBackingstoreSize = remainingBackingstoreSize + (int) (delegate.getSize() - newSize);
			delegate.resize((int) newSize);
		} else {
			throw new OutOfMemoryError("thrown from MemoryArea :: resizeMem : Out of backingstore exception ");
		}
	}

	/**
	 * get the overall remaining backing store size.
	 * 
	 * @return the overall remaining backing store size.
	 */
	public static int getRemainingMemorySize() {
		if (head == null) {
			return OverAllBackingStoreEnd - OverAllBackingStoreBase;
		} else {
			int maxEnd = 0;
			MemoryArea current = head;
			while (current != null) {
				maxEnd = maxEnd > current.backingstoreEnd ? maxEnd : current.backingstoreEnd;
				current = current.next;
			}
			return OverAllBackingStoreEnd - maxEnd;
		}
	}

	protected int getBackingstoreEnd() {
		return backingstoreEnd;
	}

	public int getRemainingBackingstoreSize() {

		return remainingBackingstoreSize;
	}

	public void setRemainingBackingstoreSize(int BackingstoreSizeOfThisMemory) {
		this.remainingBackingstoreSize = BackingstoreSizeOfThisMemory;
	}

	private int getBackingStoreSize() {
		return this.backingstoreEnd - this.backingstoreBase;
	}

	/**
	 * Makes this memory area the allocation context for the execution of the <code>run()</code> method of the instance of
	 * <code>Runnable</code> given in the constructor. <br>
	 * During this period of execution, this memory area becomes the default allocation context until another allocation context is selected
	 * or the <code>Runnable</code>'s <code>run</code> method exits.
	 * <p>
	 * This method is like the <code>executeInArea</code> method, but extended with cleanup and pointer reset.
	 * 
	 * @throws IllegalArgumentException
	 *             if the caller is a schedulable object and <code>logic</code> is null.
	 * 
	 * @param logic
	 *            is the <code>Runnable</code> object whose <code>run()</code> method shall be executed.
	 */
	@SCJAllowed(Level.INFRASTRUCTURE)
	protected void enter(Runnable logic) throws IllegalArgumentException {
		if (logic == null || !(logic instanceof ManagedSchedulable))
			throw new IllegalArgumentException();
		ManagedSchedulable ms = (ManagedSchedulable) logic;
		MemoryArea outer = ms.getCurrentMemory();
		ms.setCurrentMemory(this);
		ms.setTopMemory(this);

		outer.delegate.switchToArea(this.delegate);
		logic.run();
		this.delegate.switchToArea(outer.delegate);

		this.delegate.reset(0);
		ms.setCurrentMemory(outer);
		ms.setTopMemory(outer);
	}

	/**
	 * Executes <code>logic</code> in this memory area, with no cleanup and no pointer reset at the end.
	 * 
	 * @param logic
	 *            The Runnable object whose <code>run()</code> method shall be executed.
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>logic</code> is null.
	 */
	@SCJAllowed
	protected void executeInArea(Runnable logic) throws IllegalArgumentException {
		if (logic == null)
			throw new IllegalArgumentException("executeInArea: logic is null");

		if (flag) {
			flag = false;
			Memory currentMem = vm.Memory.getHeapArea();
			currentMem.switchToArea(this.delegate);
			logic.run();
			this.delegate.switchToArea(currentMem);
		} else {
			RtProcess currProcess = Services.getCurrentProcess();

			if (currProcess == null)
				throw new IllegalArgumentException("executeInArea: process is null");

			ManagedSchedulable currSO = currProcess.target;
			MemoryArea outer = currSO.getCurrentMemory();
			currSO.setCurrentMemory(this);

			outer.delegate.switchToArea(this.delegate);
			logic.run();
			this.delegate.switchToArea(outer.delegate);

			currSO.setCurrentMemory(outer);
		}
	}

	// for test purpose
	public static void showLink() {
		MemoryArea current = head;
		while (current != null) {
			devices.Console.println("									                                 	 	***" + current.delegate.getBase());
			current = current.next;
		}
	}

}
