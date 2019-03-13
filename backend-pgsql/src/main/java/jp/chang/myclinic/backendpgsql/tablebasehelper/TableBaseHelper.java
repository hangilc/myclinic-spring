package jp.chang.myclinic.backendpgsql.tablebasehelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TableBaseHelper {

    private static final DateTimeFormatter sqlDateTimeFormatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    private TableBaseHelper() { }


    public static String getValidUpto(LocalDate dbValue){
        return dbValue == null ? "0000-00-00" : dbValue.toString();
    }

    public static String localDateTimeToString(LocalDateTime at){
        return at.format(sqlDateTimeFormatter);
    }

}
