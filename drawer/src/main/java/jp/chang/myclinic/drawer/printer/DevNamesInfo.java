package jp.chang.myclinic.drawer.printer;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class DevNamesInfo {
	private String driver = "";
	private String device = "";
	private String output = "";

	private DevNamesInfo(){

	}

	public DevNamesInfo(Pointer pointer){
		init(pointer);
	}

	public DevNamesInfo(byte[] data){
		Pointer pointer = new Memory(data.length);
		pointer.write(0, data, 0, data.length);
		init(pointer);
	}

	private void init(Pointer pointer){
		DEVNAMES devnames = new DEVNAMES(pointer);
		driver = pointer.getWideString(devnames.wDriverOffset.intValue()*2);
		device = pointer.getWideString(devnames.wDeviceOffset.intValue()*2);
		output = pointer.getWideString(devnames.wOutputOffset.intValue()*2);
	}

	public String getDriver(){
		return driver;
	}

	public String getDevice(){
		return device;
	}

	public String getOutput(){
		return output;
	}

	@Override
	public String toString(){
		return "DevNamesInfo[" + 
			"driver=" + driver + ", " +
			"device=" + device + ", " +
			"ouotput=" + output +
		"]";
	}
}