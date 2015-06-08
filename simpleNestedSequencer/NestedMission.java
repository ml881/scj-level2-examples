/** Simple Nested Sequencer
 * 
 *   
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.simpleNestedSequencer;

import javax.realtime.AperiodicParameters;
import javax.realtime.HighResolutionTime;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;

import javax.scj.util.Const;

import devices.Console;

public class NestedMission extends Mission
{

	@Override
	protected void initialize()
	{
		Console.println("Launch Mission: Init ");

		// Initially false because the conditions haven't been checked yet
		NestedOneShotEventHandler nestedOneShot = new NestedOneShotEventHandler(
				new PriorityParameters(5), new RelativeTime(5, 0),
				new AperiodicParameters(),
				TestSafelet.storageParameters_Schedulable);

		nestedOneShot.register();

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
