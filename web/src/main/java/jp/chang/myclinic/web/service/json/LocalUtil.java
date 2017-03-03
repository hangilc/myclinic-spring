package jp.chang.myclinic.web.service.json;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Created by hangil on 2017/03/03.
 */
class LocalUtil {
    LocalDateTime stringToLocalDateTime(String src){
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.parseStrict();
        DateTimeFormatter formatter = builder.toFormatter();
        return LocalDateTime.parse(src, formatter);
    }

    Timestamp stringToTimestamp(String src){
        return Timestamp.valueOf(stringToLocalDateTime(src));
    }
}


