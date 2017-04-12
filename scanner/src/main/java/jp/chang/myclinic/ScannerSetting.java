package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ScannerSetting {

	private static Logger logger = LoggerFactory.getLogger(ScannerSetting.class);
	public static ScannerSetting INSTANCE = new ScannerSetting();

	public Path settingFile;

	private ScannerSetting(){
		settingFile = resolveSettingFile();
	}

	private Path resolveSettingFile(){
		Path path = Paths.get(System.getProperty("user.home"), "myclinic-scanner.properties");
		String prop = System.getProperty("myclinic.scanner.settingFile");
		if( prop != null ){
			path = Paths.get(prop);
		}
		logger.info("resolved setting file to {}", path);
		return path;
	}

}