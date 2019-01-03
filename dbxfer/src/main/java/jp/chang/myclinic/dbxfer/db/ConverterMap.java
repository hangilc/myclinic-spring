package jp.chang.myclinic.dbxfer.db;

import java.util.function.Function;

public interface ConverterMap {

    Function<Object, Object> getConverter(Class<?> srcType, Class<?> dstType);

}
