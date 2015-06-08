package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class FlatBufferMissionSequencer extends MissionSequencer<Mission>
{
	private boolean returnedMission;

	public FlatBufferMissionSequencer(PriorityParameters priorityParameters,
			StorageParameters storageParameters)
	{
		super(priorityParameters, storageParameters);
		returnedMission = false;
	}

	protected Mission getNextMission()
	{
		Console.println("FlatBufferMissionSequencer");

		// As this sequencer only delivers one mission,
		// if it has not been returned yet then return it,
		// else return null which will terminate the sequencer

		if (!returnedMission)
		{
			Console.println("FlatBufferMissionSequencer returns mission");
			returnedMission = true;
			return new FlatBufferMission();
		}
		else
		{
			return null;
		}
	}

}
