package jp.chang.myclinic.drawer.printer;

public class DevNamesInfo {
	private String driver = "";
	private String device = "";
	private String output = "";

	public String getDriver(){
		return driver;
	}

	public void setDriver(String driver){
		this.driver = driver;
	}

	public String getDevice(){
		return device;
	}

	public void setDevice(String device){
		this.device = device;
	}

	public String getOutput(){
		return output;
	}

	public void setOutput(String output){
		this.output = output;
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