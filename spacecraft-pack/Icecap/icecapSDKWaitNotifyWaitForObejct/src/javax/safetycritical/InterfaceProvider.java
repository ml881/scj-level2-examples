package javax.safetycritical;

import javax.realtime.ImmortalMemory;

class InterfaceProvider extends javax.realtime.InterfaceProvider{
	
	private static InterfaceProvider receiver = null;
	
	protected static InterfaceProvider instance(){
		if(receiver == null){
			receiver = new InterfaceProvider();
		}
		return receiver;
	}
	
	protected void executeInImmortal(ImmortalMemory immortal, Runnable run){
		this.exexuteInImmortal(immortal, run);
	}

}
