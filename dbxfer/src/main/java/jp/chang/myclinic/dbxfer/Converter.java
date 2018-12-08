package jp.chang.myclinic.dbxfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Converter {

    private static Logger logger = LoggerFactory.getLogger(Converter.class);

    public BigDecimal stringToBigDecimal(String src){
        return new BigDecimal(src);
    }

    public LocalDate stringToLocalDate(String src){
        return LocalDate.parse(src);
    }

    public LocalDate oldSqldateToLocalDate(String src){
        if( src == null || "0000-00-00".equals(src) ){
            return null;
        } else {
            return LocalDate.parse(src);
        }
    }

}
