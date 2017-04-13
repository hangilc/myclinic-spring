package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;

public class AppScannerBackup
{
	private static Pattern filePattern;

	static class ScannedFile {
		int patientId;
		Path path;

		ScannedFile(int patientId, Path path){
			this.patientId = patientId;
			this.path = path;
		}

		@Override
		public String toString(){
			return "ScannedFile[" + patientId + "," + path + "]";
		}
	}

    public static void main( String[] args ) throws IOException {
    	if( args.length != 2 ){
    		System.out.println("Usage: scanner-backup src-dir dst-dir");
    		System.exit(1);
    	}
    	filePattern = Pattern.compile("^(\\d+)-.+");
    	Path srcDir = Paths.get(args[0]);
    	Path dstDir = Paths.get(args[1]);
    	List<ScannedFile> srcFiles = listSrcFiles(srcDir);
    	for(ScannedFile scannedFile: srcFiles){
    		copyFile(scannedFile, dstDir);
    	}
    }

    static List<ScannedFile> listSrcFiles(Path srcDir) throws IOException {
    	List<ScannedFile> list = new ArrayList<>();
    	try(DirectoryStream<Path> stream = Files.newDirectoryStream(srcDir)){
    		for(Path path: stream){
    			String filename = path.getFileName().toString();
    			Matcher matcher = filePattern.matcher(filename);
    			if( matcher.matches() ){
    				int patientId = Integer.parseInt(matcher.group(1));
    				list.add(new ScannedFile(patientId, path));
    			}
    		}
    	}
    	return list;
    }

    static void ensureDir(Path dir) throws IOException {
    	if( !Files.exists(dir) ){
    		System.out.printf("creating directory %s\n", dir);
    		Files.createDirectory(dir);
    	}
    }

    static void copyFile(ScannedFile srcFile, Path dstDir) throws IOException {
    	Path patientDir = dstDir.resolve("" + srcFile.patientId);
    	ensureDir(patientDir);
    	Path dstFile = patientDir.resolve(srcFile.path.getFileName());
    	System.out.printf("copying %s -> %s\n", srcFile.path, dstFile);
    	Files.copy(srcFile.path, dstFile);
    }
}
