package jp.chang.myclinic.masterlib;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MainInitMaster {

    public static void main(String[] args) throws IOException {
        Path saveDir = Paths.get("./master-files");
        Path shinryouZip = saveDir.resolve(MasterDownloader.DEFAULT_SHINRYOU_FILENAME);
        int[] counts = new int[]{0};
        parseZipFile(shinryouZip.toFile(), "s.csv", record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            ShinryouMasterCSV shinryouCSV = new ShinryouMasterCSV(csvRow);
            int kubun = shinryouCSV.kubun;
            if (kubun == 0 || kubun == 3 || kubun == 5) {
                
                counts[0] += 1;
            }
        });
        System.out.println(counts[0]);
    }

    private static void parseZipFile(File zipFile, String file, Consumer<CSVRecord> consumer) throws IOException {
        try(ZipFile zip = new ZipFile(zipFile, Charset.forName("MS932"))){
            ZipEntry entry = zip.getEntry(file);
            if( entry == null ){
                System.err.println("Cannot find file: " + file);
                System.exit(1);
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
