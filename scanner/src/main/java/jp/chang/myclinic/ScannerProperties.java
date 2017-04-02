package jp.chang.myclinic;

import java.util.Properties;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;

public class ScannerProperties extends Properties {

	public static ScannerProperties INSTANCE;

	private static String keySaveDir = "myclinic.scanner.save.dir";

	static {
		try {
			INSTANCE = new ScannerProperties();
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		}
	}

	public Path getSaveDir(){
		String value = getProperty(keySaveDir);
		if( value != null ){
			return Paths.get(value);
		} else {
			return null;
		}
	}

	private ScannerProperties() throws IOException {
		super();
		loadFromSystemProperties();
		loadFromFile();
	}

	private void loadFromSystemProperties(){
		String[] keys = new String[]{ keySaveDir };
		for(String key: keys){
			String value = System.getProperty(key);
			if( value != null ){
				setProperty(key, value);
			}
		}
	}

	private void loadFromFile() throws IOException {
		Path path = Paths.get(System.getProperty("user.home"), "myclinic-scanner.properties");
		if( Files.exists(path) ){
			try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
				load(reader);
			}
		}
	}

}