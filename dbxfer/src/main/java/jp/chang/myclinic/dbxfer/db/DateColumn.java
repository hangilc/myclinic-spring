package jp.chang.myclinic.dbxfer.db;

import java.sql.Date;
import java.time.LocalDate;

public class DateColumn extends Column {

    //private static Logger logger = LoggerFactory.getLogger(DateColumn.class);

    public DateColumn(String name) {
        super(name, Date.class, LocalDate.class);
    }

    @Override
    Object convertJdbcObjectToJavaObject(Object jdbcObject) {
        if( jdbcObject == null ){
            return null;
        } else {
            Date date = (Date) jdbcObject;
            return date.toLocalDate();
        }
    }

    @Override
    Object convertJavaObjectToJdbcObject(Object javaObject) {
        if( javaObject == null ){
            return null;
        } else {
            LocalDate localDate = (LocalDate)javaObject;
            return Date.valueOf(localDate);
        }
    }
}
