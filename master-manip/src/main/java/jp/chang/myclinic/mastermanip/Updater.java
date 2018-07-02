package jp.chang.myclinic.mastermanip;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.mastermanip.lib.IyakuhinMasterCSV;
import jp.chang.myclinic.mastermanip.lib.KizaiMasterCSV;
import jp.chang.myclinic.mastermanip.lib.ShinryouMasterCSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class Updater {

    private static Logger logger = LoggerFactory.getLogger(Updater.class);

    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            String kind = args[0];
            String validFrom = args[1];
            ObjectMapper mapper = new ObjectMapper();
            switch (kind) {
                case "iyakuhin": {
                    forEachLine(line -> updateIyakuhin(line, mapper, validFrom));
                    break;
                }
                case "shinryou": {
                    forEachLine(line -> updateShinryou(line, mapper, validFrom));
                    break;
                }
                case "kizai": {
                    forEachLine(line -> updateKizai(line, mapper, validFrom));
                    break;
                }
                default: {
                    System.err.println("Unknown kind (updater): " + kind);
                    System.err.println("Valid kind is one of iyakuhin, shinryou, and kizai");
                    System.exit(1);
                }
            }
        } else {
            System.err.println("Usage: master-updater kind valid-from");
            System.exit(1);
        }
    }

    private static void updateIyakuhin(String line, ObjectMapper mapper, String validFrom) {
        try {
            IyakuhinMasterCSV master = mapper.readValue(line, new TypeReference<IyakuhinMasterCSV>() {
            });
            int kubun = master.kubun;
            if (kubun == 0 || kubun == 3 || kubun == 5) {
                System.out.println(iyakuhinSql(master, validFrom));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to parse iyakuhin.");
            System.exit(1);
        }
    }

    private static void updateShinryou(String line, ObjectMapper mapper, String validFrom) {
        try {
            ShinryouMasterCSV master = mapper.readValue(line, new TypeReference<ShinryouMasterCSV>() {
            });
            int kubun = master.kubun;
            if (kubun == 0 || kubun == 3 || kubun == 5) {
                System.out.println(shinryouSql(master, validFrom));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to parse iyakuhin.");
            System.exit(1);
        }
    }

    private static void updateKizai(String line, ObjectMapper mapper, String validFrom) {
        try {
            KizaiMasterCSV master = mapper.readValue(line, new TypeReference<KizaiMasterCSV>() {
            });
            int kubun = master.kubun;
            if (kubun == 0 || kubun == 3 || kubun == 5) {
                System.out.println(kizaiSql(master, validFrom));
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

    private static String iyakuhinSql(IyakuhinMasterCSV master, String validFrom) {
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

    private static String shinryouTemplate;

    static {
        shinryouTemplate = String.join(" ",
                "insert into shinryoukoui_master_arch set",
                "shinryoucode=%s,",
                "name='%s',",
                "tensuu='%s',",
                "tensuu_shikibetsu='%s',",
                "shuukeisaki='%s',",
                "houkatsukensa='%s',",
                "oushinkubun='%s',",
                "kensagroup='%s',",
                // "roujintekiyou='%s',",
//                "code_shou='%s',",
//                "code_bu='%s',",
//                "code_alpha='%s',",
//                "code_kubun='%s',",
                "valid_from='%s',",
                "valid_upto='%s';"
        );
    }

    private static String shinryouSql(ShinryouMasterCSV master, String validFrom) {
        return String.format(shinryouTemplate,
                master.shinryoucode,
                master.name,
                master.tensuu,
                master.tensuuShikibetsu,
                master.shuukeisaki,
                master.houkatsukensa,
                master.oushinKubun,
                master.kensaGroup,
                //master.roujinTekiyou,
//                master.codeShou,
//                master.codeBu,
//                master.codeAlpha,
//                master.codeKubun,
                validFrom,
                "0000-00-00"
        );
    }

    private static String kizaiTemplate;

    static {
        kizaiTemplate = String.join(" ",
                "insert into tokuteikizai_master_arch set",
                "kizaicode=%d,",
                "name='%s',",
                "yomi='%s',",
                "unit='%s',",
                "kingaku='%s',",
                "valid_from='%s',",
                "valid_upto='%s';"
        );
    }

    private static String kizaiSql(KizaiMasterCSV master, String validFrom) {
        return String.format(kizaiTemplate,
                master.kizaicode,
                master.name,
                master.yomi,
                master.unit,
                master.kingaku,
                validFrom,
                "0000-00-00"
        );
    }

    private static void forEachLine(Consumer<String> consumer) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        consumer.accept(line);
                    }
                }
            }
        }
    }

}
