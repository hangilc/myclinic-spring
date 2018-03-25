package jp.chang.myclinic.mastermanip;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.mastermanip.lib.IyakuhinMasterCSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class Updater {

    private static Logger logger = LoggerFactory.getLogger(Updater.class);

    public static void main(String[] args) throws IOException {
        if( args.length == 2 ){
            String kind = args[0];
            String validFrom = args[1];
            ObjectMapper mapper = new ObjectMapper();
            switch(kind){
                case "iyakuhin": {
                    forEachLine(line -> updateIyakuhin(line, mapper, validFrom));
                    break;
                }
                default: {
                    System.err.println("Unknown kinda: " + kind);
                    System.err.println("Valid kind is one of iyakuhin, shinryou, and kizai");
                    System.exit(1);
                }
            }
        } else {
            System.err.println("Usage: master-updater kind valid-from");
            System.exit(1);
        }
    }

    private static void updateIyakuhin(String line, ObjectMapper mapper, String validFrom){
        try {
            IyakuhinMasterCSV master = mapper.readValue(line, new TypeReference<IyakuhinMasterCSV>(){});
            int kubun = master.kubun;
            if( kubun == 0 || kubun == 3 || kubun == 5 ){
                System.out.println(iyakuhinSql(master, validFrom));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to parse iyakuhin.");
            System.exit(1);
        }
    }

    private static String iyakuhinTemplate;
    static {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into iyakuhin_master_arch set ");
        builder.append("iyakuhincode=%d,");
        builder.append("yakkacode='%s',");
        builder.append("name='%s',");
        builder.append("yomi='%s',");
        builder.append("unit='%s',");
        builder.append("yakka='%s',");
        builder.append("madoku='%s',");
        builder.append("kouhatsu='%s',");
        builder.append("zaikei='%s',");
        builder.append("valid_from='%s',");
        builder.append("valid_upto='%s';");
        iyakuhinTemplate = builder.toString();
    }

    private static String iyakuhinSql(IyakuhinMasterCSV master, String validFrom){
        return String.format(iyakuhinTemplate,
                master.iyakuhincode,
                master.yakkacode,
                master.name,
                master.yomi,
                master.unit,
                master.yakka,
                master.madoku,
                master.kouhatsu,
                master.zaikei,
                validFrom,
                "0000-00-00");
    }

    private static void forEachLine(Consumer<String> consumer) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                while(true){
                    String line = reader.readLine();
                    if( line == null ){
                        break;
                    } else {
                        consumer.accept(line);
                    }
                }
            }
        }
    }

}
