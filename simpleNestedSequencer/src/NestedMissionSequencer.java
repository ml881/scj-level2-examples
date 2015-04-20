/** Simple Nested Sequencer
 * 
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.simpleNestedSequencer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class NestedMissionSequencer extends MissionSequencer<Mission>
{

	private boolean returnedMission = false;

	public NestedMissionSequencer(PriorityParameters priority,
			StorageParameters storage)
	{
		super(priority, storage);
		Console.println("Nested Mission Sequencer: Construct ");

	}

	@Override
	protected Mission getNextMission()
	{
		Console.println("Mode Changer: getNextMission ");

		if (returnedMission)
		{
			return null;
		} else
		{
			returnedMission = true;
			return new NestedMission();
		}

	}

}
