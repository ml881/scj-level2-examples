/** Spacecraft - Mode Change Example
 * 
 * Handler for responding to the pilot starting an engine burn
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;

public class BurnActivationHandler extends AperiodicEventHandler
{
	/**
	 * The controlling mission
	 */
	private final CruiseMission mission;

	/**
	 * Class Constructor
	 * 
	 * @param priority
	 *            PriportiyParamters for this handler
	 * @param release
	 *            ReleaseParameters for this handler
	 * @param storage
	 *            StorageParamters for this handler
	 * @param size
	 *            size if this handler's private memory
	 * @param mission
	 *            this handler's controlling mission
	 */
	public BurnActivationHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage,
			CruiseMission mission)
	{
		super(priority, release, storage, "Burn Activation Handler");

		this.mission = mission;
	}

	/**
	 * Called when the handler is fired
	 * 
	 * Checks with the mission to see if it is ok to activate the burn.
	 * Activates if <code>mission.isOkToCruise()</code> returns
	 * <code>true</code>
	 */
	@Override
	public void handleAsyncEvent()
	{
		if (mission.isOkToCruise())
		{
			System.out.println("Activate Burn");
			mission.activateBurn();
		}
		else
		{
			System.out.println("Burn Blocked");
		}

	}

}
