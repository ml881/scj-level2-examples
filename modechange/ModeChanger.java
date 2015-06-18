package modechange;


//This marker interface shows that a particular class is a ModeChanger and says it must implement the changeTo method
public interface ModeChanger 
{
	public void changeTo(Mode newMode);
}
