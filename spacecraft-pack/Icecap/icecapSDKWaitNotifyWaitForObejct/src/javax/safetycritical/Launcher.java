/**************************************************************************
 * File name  : Launcher.java
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
 *   date   init  comment
 *
 *************************************************************************/
package javax.safetycritical;

import javax.realtime.ImmortalMemory;
import javax.scj.util.Const;

/**
 * This class is used by an application class to launch a Level 0 or a Level 1 application.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 * 
 * @scjComment - The class is not part of the SCJ specification.
 */
public class Launcher implements Runnable {
	Safelet<?> app;
	static int level;

	public Launcher(Safelet<?> app, int level) {
		this.app = app;
		Launcher.level = level;
		ManagedMemory.allocateBackingStore(Const.BACKING_STORE_SIZE);
		ImmortalMemory immortalMem = ImmortalMemory.instance();
		InterfaceProvider.instance().executeInImmortal(immortalMem, this);
		devices.Console.println("application finished");
	}

	public void run() {
		// get the desired immortal size and try to resize the immortal memory.
		if (app.immortalMemorySize() > ImmortalMemory.instance().memoryRemaining()) {
			devices.Console.println("immortal size is not enough");
			return;
		}

		app.initializeApplication();

		if (level != 0) {
			// insert idle process before the mission sequencer.
			PriorityScheduler sch = PriorityScheduler.instance();
			sch.insertReleaseQueue(ScjProcess.createIdleProcess());
			
			app.getSequencer();
			sch.start();
		} else {
			CyclicScheduler.instance().start(app.getSequencer());
		}

	}
}
