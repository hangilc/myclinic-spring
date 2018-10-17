package jp.chang.myclinic.util.logic;

import java.time.LocalDate;

public class Mappers {

    //private static Logger logger = LoggerFactory.getLogger(Mapper.class);

    private Mappers() {

    }

    public static String integerToString(int value) {
        return String.format("%d", value);
    }

    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public static String integerToStringWithZeroOrNullToEmpty(Integer value) {
        if (value == null || value == 0) {
            return "";
        } else {
            return String.format("%d", value);
        }
    }

    public static String localDateToSqldate(LocalDate value) {
        if (value == null) {
            return "0000-00-00";
        } else {
            return value.toString();
        }
    }

}
