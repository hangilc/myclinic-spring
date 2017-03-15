package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class AddIyakuhin extends AddMaster {

	private Path file;

	public AddIyakuhin(Path file){
		this.file = file;
	}

	public void run(){
		try(BufferedReader reader = Files.newBufferedReader(file, Charset.forName("MS932"))){
			Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(reader);
			int count = 0;
			System.out.println("reading master file...\n");
			for(CSVRecord record: records){
				count += 1;
				if( count <= 1 ){
					System.out.println(record);
				}
			}
			System.out.printf("read %d entries\n", count);
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		}
	}

}