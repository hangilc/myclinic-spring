package jp.chang.myclinic.master;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public class CSVSearcher {
	public static List<CSVRecord> searchCSV(Pattern pattern, Reader source, int oneBasedNameIndex) throws IOException {
		List<CSVRecord> list = new ArrayList<>();
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(source);
		for(CSVRecord record: records){
			String name = record.get(oneBasedNameIndex-1);
			Matcher matcher = pattern.matcher(name);
			if( matcher.find() ){
				list.add(record);
			}
		}
		return list;
	}

	public static List<CSVRecord> searchCSV(String[] texts, Path zipFile, String csvFileName, int oneBasedNameIndex) throws IOException {
		try(FileSystem fs = FileSystems.newFileSystem(zipFile, null)){
			Path masterFile = fs.getPath(csvFileName);
			try(BufferedReader reader = Files.newBufferedReader(masterFile, Charset.forName("MS932"))){
				String expr = String.join(".*", texts);
				System.out.println(expr);
				Pattern pattern = Pattern.compile(expr);
				return searchCSV(pattern, reader, oneBasedNameIndex);
			}
		}
	}

}