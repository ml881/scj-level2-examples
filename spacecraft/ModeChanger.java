/** Spacecraft - Mode Change Example
* 
* 	Interface that identifies a ModeChanger 
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package scjlevel2examples.spacecraft;


public interface ModeChanger
{
	/**
	 * This method should be synchronized when overridden
	 * and should ensure the parameter is the next mode
	 * returned   
	 * 
	 * @param newMode The next Mode (Mission)
	 */
	public void changeTo(Mode newMode);
	
	/**
	 * This method should be synchronized when overridden
	 * and should ensure that the next mode returned 
	 * is one along in the pre-planned sequence of modes
	 */
	public void advanceMode();
}
