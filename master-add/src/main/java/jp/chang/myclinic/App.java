package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.UncheckedIOException;

public class App {

	public static void main(String[] args){
		new App().run(args);
	}

	private void run(String[] args){
		if( args.length != 1 ){
			usage();
			System.exit(1);
		}
		String masterName = args[0];
		switch(masterName){
			case "iyakuhin": addIyakuhin(); break;
			default: invalidMasterName(masterName); System.exit(1);
		}
	}

	private void usage(){
		System.out.println("usage: maser-add master-name");
		System.out.println("master-name is one of 'iyakuhin', 'shinryou', or 'kizai'");
	}

	private void invalidMasterName(String name){
		System.out.println("invalid master-name: " + name);
		System.out.println("master-name should be one of 'iyakuhin', 'shinryou', or 'kizai'");
	}

	private void addIyakuhin(){
		Path path = pickZipFile(Paths.get("."), "y");
		try(FileSystem fs = FileSystems.newFileSystem(path, null)){
			Path masterFile = fs.getPath("y.csv");
			new AddIyakuhin(masterFile).run();
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		}
	}

	private Path pickZipFile(Path dir, String prefix){
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
			System.out.println("cannot find zip file for iyakuhin master");
			System.exit(1);
		}
		return zipFiles.get(0);
	}
}