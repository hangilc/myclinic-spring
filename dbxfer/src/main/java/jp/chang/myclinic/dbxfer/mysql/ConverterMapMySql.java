package jp.chang.myclinic.dbxfer.mysql;

import jp.chang.myclinic.dbxfer.db.ConverterMapBase;
import jp.chang.myclinic.dbxfer.db.OldSqldate;
import jp.chang.myclinic.dbxfer.db.Sqldate;

import java.time.LocalDate;
import java.util.function.Function;

public class ConverterMapMySql extends ConverterMapBase {

    private LocalDate sqldateToLocalDate(Sqldate sqldate){
        return LocalDate.parse(sqldate.getRep());
    }

    private LocalDate oldSqldateToLocalDate(OldSqldate oldSqldate){
        if( oldSqldate == null || "0000-00-00".equals(oldSqldate.getRep()) ){
            return null;
        } else {
            return LocalDate.parse(oldSqldate.getRep());
        }
    }

    @Override
    public Function<Object, Object> getConverter(Class<?> srcType, Class<?> dstType) {
        if( srcType == Sqldate.class && dstType == LocalDate.class ){
            return s -> sqldateToLocalDate((Sqldate)s);
        } else if( srcType == LocalDate.class && dstType == Sqldate.class ){
            return d -> new Sqldate((LocalDate)d);
        } else if( srcType == OldSqldate.class && dstType == LocalDate.class ){
            return s -> oldSqldateToLocalDate((OldSqldate)s);
        } else if( srcType == LocalDate.class && dstType == OldSqldate.class ){
            return d -> new OldSqldate((LocalDate)d);
        } else {
            return super.getConverter(srcType, dstType);
        }
    }
}
