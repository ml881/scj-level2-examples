/** Aircraft - Mode Change Example
* 
* This class simulates the aperiodic firing of an 
* external event (e.g. a button press) by simply firing the event periodically
* 
* @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;

import javax.realtime.*;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;


public class AperiodicSimulator extends PeriodicEventHandler
{
	/**
	 * The aperiodic event to be fired each period
	 */
	private AperiodicEventHandler event;
	
	/**
	 * Class constructor 
	 * 
	 * @param priority	the priority of the handler
	 * @param periodic  the periodic parameters of the handler
	 * @param storage	the storage parameters of the handler	
	 * @param aperiodicEvent the aperiodic event handler to be fired each period
	 */
	public AperiodicSimulator(PriorityParameters priority, PeriodicParameters periodic,
			StorageParameters storage, AperiodicEventHandler aperiodicEvent)
	{
		super(priority, periodic, storage);
		event = aperiodicEvent;
	}
	
	/**
	 * The method the infrastructure calls when it is fired
	 * 
	 * This method fires the <code>event</code>
	 */
	@Override
	public void handleAsyncEvent()
	{
			System.out.println("Simulating AperiodicEvent");
			event.release();			
	}
}
