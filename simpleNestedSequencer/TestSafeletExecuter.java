/** Simple Nested Sequencer
 * 
 * 	Executes the application's <code>Safelet</code>
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.simpleNestedSequencer;

import javax.safetycritical.Launcher;
import javax.safetycritical.Mission;
import javax.safetycritical.Safelet;
import javax.scj.util.Const;

import devices.Console;


public class TestSafeletExecuter extends Launcher
{
	/**
	 * Class Constructor
	 * 
	 * @param arg0
	 *            The Safelet to execute
	 */
	public TestSafeletExecuter(Safelet<Mission> safelet)
	{
		super(safelet, 2);
	}

	/**
	 * Runs the Safelet, which starts the application
	 */
	public static void main(String[] args)
	{
//		Const.BACKING_STORE_SIZE=16000000 ;
//		Const.IMMORTAL_MEM_SIZE = Const.BACKING_STORE_SIZE /2 ;
				
				
		Console.println("Launcher");
		
		new TestSafeletExecuter(new TestSafelet()).run();
	}
}
