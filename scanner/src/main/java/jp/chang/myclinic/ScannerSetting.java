package jp.chang.myclinic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;
import java.nio.charset.StandardCharsets;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ScannerSetting {

	private static Logger logger = LoggerFactory.getLogger(ScannerSetting.class);
	private static String keySaveDir = "myclinic.scanner.save.dir";
	private static String keyDip     = "myclinic.scanner.dip";
	public static ScannerSetting INSTANCE;

	static {
		try {
			INSTANCE = new ScannerSetting();
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		}
	}

	public Path settingFile = Paths.get(System.getProperty("user.home"), "myclinic-scanner.properties");
	public Path savingDir = Paths.get(System.getProperty("user.dir"));
	public int dip = 200;

	private ScannerSetting() throws IOException {
		resolveSettingFile();
		Properties properties = loadProperties();
		resolveSavingDir(properties);
		resolveDip(properties);
	}

	public void saveToFile() throws IOException {
		Properties props = new Properties();
		props.setProperty(keySaveDir, savingDir.toString());
		props.setProperty(keyDip, String.valueOf(dip));
		try(BufferedWriter writer = Files.newBufferedWriter(settingFile, StandardCharsets.UTF_8, 
			CREATE, TRUNCATE_EXISTING, WRITE)){
			props.store(writer, "");
		}
	}

	private void resolveSettingFile(){
		String prop = System.getProperty("myclinic.scanner.settingFile");
		if( prop != null ){
			settingFile = Paths.get(prop);
		}
		logger.info("resolved setting file to {}", settingFile);
	}

	private Properties loadProperties() throws IOException {
		Path path = settingFile;
		Properties properties = new Properties();
		if( Files.exists(path) ){
			try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
				properties.load(reader);
			}
			logger.info("loaded properties from {}", path);
		} else {
			logger.warn("setting file does not exist (ignored): {}", settingFile);
		}
		return properties;
	}

	private void resolveSavingDir(Properties properties){
		String value = properties.getProperty(keySaveDir);
		{
			String other = System.getProperty(keySaveDir);
			if( other != null ){
				value = other;
			}
		}
		if( value != null ){
			savingDir = Paths.get(value);
		}
	}

	private void resolveDip(Properties properties){
		String value = properties.getProperty(keyDip);
		{
			String arg = System.getProperty(keyDip);
			if( arg != null ){
				value = arg;
			}
		}
		if( value != null ){
			try{ 
				dip = Integer.parseInt(value);
			} catch(NumberFormatException ex){
				throw new RuntimeException(ex);
			}
		}
	}

}