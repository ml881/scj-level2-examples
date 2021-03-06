/**************************************************************************
 * File name  : ImmortalMemory.java
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

import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.scj.util.Const;

/**
 * This class represents immortal memory. Objects allocated in immortal memory are never reclaimed during the lifetime of the application.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 * 
 * @scjComment Redundant in SCJ.
 */
@SCJAllowed(Level.INFRASTRUCTURE)
public final class ImmortalMemory extends MemoryArea {

	private ImmortalMemory() {
		super(Const.IMMORTAL_MEM_SIZE, Const.IMMORTAL_MEM_SIZE, null);
	}

	public static ImmortalMemory instance() {
		if (head == null) {
			new ImmortalMemory();
		}
		return (ImmortalMemory) head;
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
	
//	protected void executeInArea(Runnable logic){
//		super.executeInArea(logic);
//	}
}
