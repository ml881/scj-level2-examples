package scjlevel2examples.simplemodechange;

import scjlevel2examples.spacecraft.Mode;

//This marker interface shows that a particualr class is a ModeChanger and says it must implement the changeTo method
public interface ModeChanger 
{
	public void changeTo(Mode newMode);
}
