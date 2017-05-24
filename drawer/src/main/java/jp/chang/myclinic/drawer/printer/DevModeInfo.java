package jp.chang.myclinic.drawer.printer;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class DevModeInfo {
	private String deviceName = "";
	private int orientation;
	private String orientationLabel = "";
	private int paperSize;
	private String paperSizeLabel = "";
	private int copies;
	private int printQuality;
	private String printQualityLabel = "";
	private int defaultSource;
	private String defaultSourceLabel = "";

	private DevModeInfo(){

	}

	public DevModeInfo(Pointer pointer){
		init(pointer);
	}

	public DevModeInfo(byte[] data){
		Pointer pointer = new Memory(data.length);
		pointer.write(0, data, 0, data.length);
		init(pointer);
	}

	private void init(Pointer pDevMode){
		DEVMODE devmode = new DEVMODE(pDevMode);
		setDeviceName(new String(devmode.dmDeviceName));
		setOrientation(devmode.dmOrientation);
		setPaperSize(devmode.dmPaperSize);
		setCopies(devmode.dmCopies);
		setPrintQuality(devmode.dmPrintQuality);
		setDefaultSource(devmode.dmDefaultSource);
	}

	public String getDeviceName(){
		return deviceName;
	}

	private void setDeviceName(String deviceName){
		this.deviceName = deviceName;
	}

	public int getOrientation(){
		return orientation;
	}

	private void setOrientation(int orientation){
		this.orientation = orientation;
		this.orientationLabel = PrinterConsts.findOrientationLabel(orientation);
	}

	public String getOrientationLabel(){
		return orientationLabel;
	}

	public int getPaperSize(){
		return paperSize;
	}

	private void setPaperSize(int paperSize){
		this.paperSize = paperSize;
		this.paperSizeLabel = PrinterConsts.findPaperSizeLabel(paperSize);
	}

	public String getPaperSizeLabel(){
		return paperSizeLabel;
	}

	public int getCopies(){
		return copies;
	}

	private void setCopies(int copies){
		this.copies = copies;
	}

	public int getPrintQuality(){
		return printQuality;
	}

	private void setPrintQuality(int printQuality){
		this.printQuality = printQuality;
		this.printQualityLabel = PrinterConsts.findPrintQualityLabel(printQuality);
	}

	public int getDefaultSource(){
		return defaultSource;
	}

	private void setDefaultSource(int defaultSource){
		this.defaultSource = defaultSource;
		this.defaultSourceLabel = PrinterConsts.findDefaultSourceLabel(defaultSource);
	}

	@Override
	public String toString(){
		return "DevModeInfo[" +
			"deviceName=" + deviceName + ", " +
			"orientation=" + orientationLabel + "(" + orientation + "), " +
			"paperSize=" + paperSizeLabel + "(" + paperSize + "), " +
			"copies=" + copies + ", " +
			"printQuality=" + printQualityLabel + "(" + printQuality + "), " +
			"defaultSource=" + defaultSourceLabel + "(" + defaultSource + ")" +
		"]";
	}
}