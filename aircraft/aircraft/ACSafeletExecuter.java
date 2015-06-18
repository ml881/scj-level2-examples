/** Aircraft - Mode Change Example
* 
* 	Executes the application's <code>Safelet</code>
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;


import javax.safetycritical.Launcher;
import javax.safetycritical.Mission;
import javax.safetycritical.Safelet;


public class ACSafeletExecuter extends Launcher
{
    /**
    * Class Constructor
    *   @param arg0 The Safelet to execute
    */
	public ACSafeletExecuter(Safelet<Mission> safelet)
	{
		super(safelet, 2);	
	}
    
    /**
    *   Runs the Safelet, which starts the application
    */
	public static void main (String [] args)
	{
		ACSafelet GERTI = new ACSafelet();
		
		new ACSafeletExecuter(GERTI).run();
	}
}
