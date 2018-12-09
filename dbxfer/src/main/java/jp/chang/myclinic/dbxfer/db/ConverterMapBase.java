package jp.chang.myclinic.dbxfer.db;

import java.math.BigDecimal;
import java.util.function.Function;

public class ConverterMapBase implements ConverterMap {

    @Override
    public Function<Object, Object> getConverter(Class<?> srcType, Class<?> dstType) {
        if( srcType == BigDecimal.class && dstType == String.class ){
            return Object::toString;
        } else if( srcType == String.class && dstType == BigDecimal.class ){
            return s -> new BigDecimal((String) s);
        } else {
            return null;
        }
    }
}
