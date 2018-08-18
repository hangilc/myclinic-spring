package jp.chang.myclinic.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static java.nio.file.StandardOpenOption.*;

class ScannerSetting {

	private static Logger logger = LoggerFactory.getLogger(ScannerSetting.class);
	private static String keySaveDir = "myclinic.scanner.save.dir";
	private static String keyDpi     = "myclinic.scanner.dpi";
	private static String keyDefaultDevice = "myclinic.scanner.defaultDevice";
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
	public int dpi = 200;
	public String defaultDevice = "";

	private ScannerSetting() throws IOException {
		resolveSettingFile();
		Properties properties = loadProperties();
		resolveSavingDir(properties);
		resolveDpi(properties);
		resolveDefaultDevice(properties);
	}

	public void saveToFile() throws IOException {
		Properties props = new Properties();
		props.setProperty(keySaveDir, savingDir.toString());
		props.setProperty(keyDpi, String.valueOf(dpi));
		props.setProperty(keyDefaultDevice, defaultDevice);
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

	private String getPropValue(Properties properties, String key){
		String value = properties.getProperty(key);
		{
			String other = System.getProperty(key);
			if( other != null ){
				value = other;
			}
		}
		return value;
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

	private void resolveDpi(Properties properties){
		String value = properties.getProperty(keyDpi);
		{
			String arg = System.getProperty(keyDpi);
			if( arg != null ){
				value = arg;
			}
		}
		if( value != null ){
			try{ 
				dpi = Integer.parseInt(value);
			} catch(NumberFormatException ex){
				throw new RuntimeException(ex);
			}
		}
	}

	private void resolveDefaultDevice(Properties properties){
		String value = getPropValue(properties, keyDefaultDevice);
		if( value != null ){
			defaultDevice = value;
		}
	}

}