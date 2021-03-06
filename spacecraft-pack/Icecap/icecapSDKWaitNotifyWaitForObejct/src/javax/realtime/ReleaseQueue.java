/**************************************************************************
 * File name  : ReleaseQueue.java
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
 *   
 * Description: 
 * 
 * Revision history:
 *  date    init  comment
 *  31Jan12 HSO   In this impl., the place with index 0 (zero) is not used.
 *                The methods and algorithms are from Cormen et al.
 *                Introduction to Algorithms. MIT Press. 1990.
 *  29Jan13 HSO   Uses indexes instead of references to ScjProcesses
 *************************************************************************/

package javax.realtime;

import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

/**
 * This <code>ReleaseQueue</code> class holds the processes in the <code>PriorityScheduler</code>. <br>
 * Time complexity of the methods <code>insert</code> and <code>extractMin</code> are O(log n).<br>
 * The class is package protected because it is not part of the SCJ specification.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 * 
 * @scjComment - implementation issue: infrastructure class; not part of the SCJ specification.
 */
@SCJAllowed(Level.INFRASTRUCTURE)
class ReleaseQueue {
	int heapSize;

	protected int[] tree; // index to ScjProcesses

	public ReleaseQueue(int size) {
		heapSize = 0;
		tree = new int[size + 1];
		makeEmptyTree(this.tree);
	}

	private void makeEmptyTree(int[] tree) {
		for (int i = 0; i < tree.length; i++)
			tree[i] = -999;
	}

	int parent(int i) {
		return i / 2;
	}

	int left(int i) {
		return 2 * i;
	}

	int right(int i) {
		return 2 * i + 1;
	}

	void exchange(int a, int b) {
		int temp = tree[a];
		tree[a] = tree[b];
		tree[b] = temp;
	}

	// this method will be called when a process is taken out of the queue. It
	// will reorder the remaining process to the desired order.
	void heapify(int i) {
		int l = left(i);
		int r = right(i);

		int smallest;

		if (l <= heapSize && getRtProcess(tree[l]).compareTo(getRtProcess(tree[r])) < 0)
			smallest = l;
		else
			smallest = i;

		if (r <= heapSize && getRtProcess(tree[r]).compareTo(getRtProcess(tree[smallest])) < 0)
			smallest = r;

		if (smallest != i) {
			exchange(i, smallest);
			heapify(smallest);
		}
	}

	public void insert(RtProcess obj) {
		if (heapSize + 1 == tree.length)
			throw new IndexOutOfBoundsException("ReleaseQueue: too small");

		heapSize++;
		int i = heapSize;
		while (i > 1 && getRtProcess(tree[parent(i)]).compareTo(obj) > 0) {
			tree[i] = tree[parent(i)];
			i = parent(i);
		}
		tree[i] = obj.index;
	}

	public RtProcess extractMin() {
		if (heapSize < 1)
			return null;

		RtProcess min = getRtProcess(tree[1]);

		tree[1] = tree[heapSize];
		heapSize--;
		heapify(1);

		return min;
	}

	private RtProcess getRtProcess(int processIdx) {
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

	public void remove(RtProcess obj) {
		if (obj == null)
			return;
		int i = find(obj.index);
		if (i != -999) {
			tree[i] = tree[heapSize];
			heapSize--;
			heapify(i);
		}
	}

	private int find(int value) {
		for (int i = 1; i <= heapSize; i++) {
			if (tree[i] == value)
				return i;
		}
		return -999;
	}

	/**
	 * Print out the contents of the priority queue. For testing only.
	 */
	void print() {
		devices.Console.println("Queue size = " + heapSize);
		for (int i = 0; i < tree.length; i++) {
			devices.Console.print("[ " + tree[i] + " ] ");
		}
		devices.Console.println("");
		
		for (int i = 1; i <= heapSize; i++) {
			RtProcess process = getRtProcess(tree[i]);
			
			if (process != null)
				devices.Console.println("name: " + process.target.getName());
			else
				devices.Console.println("null process");
		}
	}

}
