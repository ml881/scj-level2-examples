package javax.realtime;

public class InterfaceProvider {
	
	protected void exexuteInImmortal(ImmortalMemory immortal, Runnable run){
		immortal.executeInArea(run);
	}

}
