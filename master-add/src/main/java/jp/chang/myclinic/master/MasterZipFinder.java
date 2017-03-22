package jp.chang.myclinic.master;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class MasterZipFinder {
	public static final String SHINRYOU_PREFIX = "s";
	public static final String IYAKUHIN_PREFIX = "y";
	public static final String KIZAI_PREFIX = "t";
	public static final String SHOUBYOUMEI_PREFIX = "b";
	public static final String SHUUSHOKUGO_PREFIX = "z";

	public static Path findMasterZip(Path dir, String prefix){
		List<Path> zipFiles = new ArrayList<>();
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, prefix + "*.zip")){
			for(Path path: stream){
				zipFiles.add(path);
			}
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		}
		zipFiles.sort((a, b) -> -a.getFileName().compareTo(b.getFileName()));
		if( zipFiles.size() == 0 ){
			return null;
		}
		return zipFiles.get(0);
	}

}