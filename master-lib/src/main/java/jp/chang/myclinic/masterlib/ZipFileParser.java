package jp.chang.myclinic.masterlib;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.NoSuchFileException;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ZipFileParser {

    private ZipFileParser(){

    }

    static void parse(File zipFile, String file, Consumer<CSVRecord> consumer) throws IOException {
        try(ZipFile zip = new ZipFile(zipFile, Charset.forName("MS932"))){
            ZipEntry entry = zip.getEntry(file);
            if( entry == null ){
                throw new NoSuchFileException(file);
            } else {
                try(InputStream is = zip.getInputStream(entry)){
                    try (InputStreamReader inputStreamReader = new InputStreamReader(is)) {
                        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(inputStreamReader);
                        for(CSVRecord record: records){
                            consumer.accept(record);
                        }
                    }
                }
            }
        }
    }

}
