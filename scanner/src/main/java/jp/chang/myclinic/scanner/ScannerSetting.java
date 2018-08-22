package jp.chang.myclinic.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
	private static String keyRegularDocSavingDirHint = "regular_doc_saving_dir_hint";
	public static ScannerSetting INSTANCE;

	static {
		try {
			INSTANCE = new ScannerSetting();
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		}
	}

	private Path settingFile;
	private Properties cache;

	private ScannerSetting() throws IOException {
		this.settingFile = resolveSettingFile();
		logger.info("resolved setting file to {}", settingFile);
		this.cache = loadProperties();
	}

	private Path resolveSettingFile(){
		String prop = System.getProperty("myclinic.scanner.settingFile");
		if( prop != null ){
			return Paths.get(prop);
		} else {
			return Paths.get(System.getProperty("user.home"), "myclinic-scanner.properties");
		}
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

	private void saveProperties(Properties props) throws IOException {
		try(BufferedWriter writer = Files.newBufferedWriter(settingFile, StandardCharsets.UTF_8,
				CREATE, TRUNCATE_EXISTING, WRITE)){
			props.store(writer, "");
		}

	}

	public Path getSavingDir(){
		String value = cache.getProperty(keySaveDir);
		if( value == null ){
			value = System.getProperty("jp.chang.myclinic.scanner.saving_dir");
		}
		if( value == null ){
			value = System.getProperty("user.dir");
		}
		return Paths.get(value);
	}

	public void setSavingDir(Path dir) throws IOException {
		cache.put(keySaveDir, dir.toAbsolutePath().toString());
		saveProperties(cache);
	}

	public int getDpi(){
		String value = cache.getProperty(keyDpi);
		if( value == null ){
			value = System.getProperty("jp.chang.myclinic.scanner.dpi");
		}
		if( value == null ){
			value = "200";
		}
		return Integer.parseInt(value);
	}

	public void setDpi(int dpi) throws IOException {
		cache.put(keyDpi, String.format("%d", dpi));
		saveProperties(cache);
	}

	public String getDefaultDevice(){
		return cache.getProperty(keyDefaultDevice);
	}

	public void setDefaultDevice(String device) throws IOException {
		cache.put(keyDefaultDevice, device);
		saveProperties(cache);
	}

	public Path getRegularDocSavingDirHint(){
		String value = cache.getProperty(keyRegularDocSavingDirHint);
		if( value == null ){
			value = System.getProperty("jp.chang.myclinic.scanner.regular_doc_saving_dir_hint");
		}
		if( value == null ){
			value = System.getProperty("user.dir");
		}
		return Paths.get(value);
	}

	public void setRegularDocSavingDirHint(Path dir) throws IOException {
		cache.put(keyRegularDocSavingDirHint, dir.toAbsolutePath().toString());
		saveProperties(cache);
	}
}