package jp.chang.myclinic.mastermanip;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.mastermanip.lib.CSVRow;
import jp.chang.myclinic.mastermanip.lib.CommonsCSVRow;
import jp.chang.myclinic.mastermanip.lib.IyakuhinMasterCSV;
import jp.chang.myclinic.mastermanip.lib.ShinryouMasterCSV;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class Convert {

    private static Logger logger = LoggerFactory.getLogger(Convert.class);

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            String kind = args[0];
            ObjectMapper mapper = new ObjectMapper();
            switch(kind){
                case "iyakuhin": {
                    forEachEntry(record -> convertIyakuhin(record, mapper));
                    break;
                }
                case "shinryou": {
                    forEachEntry(record -> convertShinryou(record, mapper));
                    break;
                }
                default: {
                    System.out.println("Unknown kind: " + kind);
                    System.out.println("Valid kind is one of iyakuhin, shinryou, or kizai.");
                    System.exit(1);
                }
            }
        } else {
            System.err.println("Usage: master-convert kind");
            System.exit(1);
        }
    }

    private static void convertIyakuhin(CSVRecord record, ObjectMapper mapper) {
        CSVRow row = new CommonsCSVRow(record);
        IyakuhinMasterCSV master = new IyakuhinMasterCSV(row);
        try {
            String json = mapper.writeValueAsString(master);
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write as JSON");
        }
    }

    private static void convertShinryou(CSVRecord record, ObjectMapper mapper) {
        CSVRow row = new CommonsCSVRow(record);
        ShinryouMasterCSV master = new ShinryouMasterCSV(row);
        try {
            String json = mapper.writeValueAsString(master);
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write as JSON");
        }
    }

    private static void forEachEntry(Consumer<CSVRecord> consumer) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(reader);
                for(CSVRecord record: records){
                    consumer.accept(record);
                }
            }
        }
    }

}
