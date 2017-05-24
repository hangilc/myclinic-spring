package jp.chang.myclinic.drawer.printer;

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

	public String getDeviceName(){
		return deviceName;
	}

	public void setDeviceName(String deviceName){
		this.deviceName = deviceName;
	}

	public int getOrientation(){
		return orientation;
	}

	public void setOrientation(int orientation){
		this.orientation = orientation;
		this.orientationLabel = PrinterConsts.findOrientationLabel(orientation);
	}

	public String getOrientationLabel(){
		return orientationLabel;
	}

	public int getPaperSize(){
		return paperSize;
	}

	public void setPaperSize(int paperSize){
		this.paperSize = paperSize;
		this.paperSizeLabel = PrinterConsts.findPaperSizeLabel(paperSize);
	}

	public String getPaperSizeLabel(){
		return paperSizeLabel;
	}

	public int getCopies(){
		return copies;
	}

	public void setCopies(int copies){
		this.copies = copies;
	}

	public int getPrintQuality(){
		return printQuality;
	}

	public void setPrintQuality(int printQuality){
		this.printQuality = printQuality;
		this.printQualityLabel = PrinterConsts.findPrintQualityLabel(printQuality);
	}

	public int getDefaultSource(){
		return defaultSource;
	}

	public void setDefaultSource(int defaultSource){
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