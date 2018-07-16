package jp.chang.myclinic.masterlib;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ZipFileParser {

    private ZipFileParser() {

    }

    static void parse(File zipFile, Pattern fileNamePattern, Consumer<CSVRecord> consumer) throws IOException {
        try (ZipFile zip = new ZipFile(zipFile, Charset.forName("MS932"))) {
            List<ZipEntry> entries = listEntries(zip);
            if (entries.size() != 1) {
                throw new RuntimeException("Master zip file should contain only single CSV file.");
            }
            ZipEntry entry = entries.get(0);
            Matcher matcher = fileNamePattern.matcher(entry.getName());
            if (!matcher.matches()) {
                throw new RuntimeException("CSV file expected");
            }
            try (InputStream is = zip.getInputStream(entry)) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(is)) {
                    Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(inputStreamReader);
                    for (CSVRecord record : records) {
                        consumer.accept(record);
                    }
                }
            }
        }
    }

    private static List<ZipEntry> listEntries(ZipFile zipFile) {
        return zipFile.stream().collect(Collectors.toList());
    }
}
