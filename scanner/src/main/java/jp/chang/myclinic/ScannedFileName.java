package jp.chang.myclinic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;

class ScannedFileName {
	private int patientId;
	private String timestamp;
	private int serial;
	private String ext;
	private static Pattern filePattern = Pattern.compile("^(\\d+)-(.+)-(\\d+)\\.([^.]+)$");

	ScannedFileName(int patientId, String timestamp, int serial, String ext){
		this.patientId = patientId;
		this.timestamp = timestamp;
		this.serial = serial;
		this.ext = ext;
	}

	int getSerial(){
		return serial;
	}

	void setSerial(int serial){
		this.serial = serial;
	}

	void setExt(String ext){
		this.ext = ext;
	}

	@Override
	public String toString(){
		return String.format("%d-%s-%02d.%s", patientId, timestamp, serial, ext);
	}

	public Path toPath(Path directory){
		return directory.resolve(toString());
	}

	static ScannedFileName parse(String filename){
		Matcher matcher = filePattern.matcher(filename);
		if( matcher.matches() ){
			int patientId = Integer.parseInt(matcher.group(1));
			String timestamp = matcher.group(2);
			int serial = Integer.parseInt(matcher.group(3));
			String ext = matcher.group(4);
			return new ScannedFileName(patientId, timestamp, serial, ext);
		} else {
			return null;
		}
	}
}