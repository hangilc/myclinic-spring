package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;

public class AddIyakuhin extends AddMaster {

	private Path file;

	public AddIyakuhin(Path file){
		this.file = file;
	}

	public void run(){
		try(BufferedReader reader = Files.newBufferedReader(file, Charset.forName("MS932"))){
			String line = reader.readLine();
			System.out.println(line);
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		}
	}

}