/** Simple Nested Sequencer
 * 
 * 	Executes the application's <code>Safelet</code>
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.simpleNestedSequencer;

import javax.safetycritical.LaunchLevel2;

import devices.Console;

public class TestSafeletExecuter
{

	/**
	 * Runs the Safelet, which starts the application
	 */
	public static void main(String[] args)
	{
		// Const.BACKING_STORE_SIZE=16000000 ;
		// Const.IMMORTAL_MEM_SIZE = Const.BACKING_STORE_SIZE /2 ;

		Console.println("Launcher");

		// new TestSafeletExecuter().run();
		new LaunchLevel2(new TestSafelet());
	}
}
