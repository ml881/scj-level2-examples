/** Simple Nested Sequencer
 * 
 *   
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.simpleNestedSequencer;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.OneShotEventHandler;
import javax.scj.util.Const;

import devices.Console;

public class NestedMission extends Mission
{

	@Override
	protected void initialize()
	{
		Console.println("Launch Mission: Init ");

		// Initially false because the conditions haven't been checked yet
		new OneShotEventHandler(new PriorityParameters(5),
				new AperiodicParameters(),
				TestSafelet.storageParameters_Schedulable)
		{

			@Override
			public void handleAsyncEvent()
			{
				Console.println("Nested One-Shot: Release");

			}
		};

		Console.println("Launch Mission: Begin ");
	}

	/**
	 * Returns the size of the mission's memory
	 */
	@Override
	public long missionMemorySize()
	{
		return 10000;
	}

}
