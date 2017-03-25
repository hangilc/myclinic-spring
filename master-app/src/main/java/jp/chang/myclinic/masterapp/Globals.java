package jp.chang.myclinic.masterapp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.UncheckedIOException;

public class Globals {

	private static Path masterFilesDirectory;

	static {
		masterFilesDirectory = Paths.get("master-files");
		if( !Files.exists(masterFilesDirectory) ){
			try{
				Files.createDirectory(masterFilesDirectory);
			} catch(IOException ex){
				throw new UncheckedIOException(ex);
			}
		}
	}

	public static Path getMasterFilesDirectory(){
		return masterFilesDirectory;
	}

	public static void setMasterFilesDirectory(Path dir){
		masterFilesDirectory = dir;
	}

}