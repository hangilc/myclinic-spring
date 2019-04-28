package jp.chang.myclinic.frontend.rest;

import jp.chang.myclinic.util.DateTimeUtil;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SuppressWarnings("unchecked")
class RestParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> aClass, Type type, Annotation[] annotations) {
        System.out.println("getConverter: "+ aClass);
        if( aClass == LocalDate.class ) {
            return (ParamConverter<T>) (new LocalDateConverter());
        } else if( aClass == LocalDateTime.class ){
            return (ParamConverter<T>) (new LocalDateTimeConverter());
        } else {
            return null;
        }
    }

    private static class LocalDateConverter implements ParamConverter<LocalDate> {

        @Override
        public LocalDate fromString(String s) {
            if (s != null && s.equals("0000-00-00")) {
                return null;
            }
            return LocalDate.parse(s);
        }

        @Override
        public String toString(LocalDate localDate) {
            if (localDate == null) {
                return "0000-00-00";
            }
            return localDate.toString();
        }
    }


    private static class LocalDateTimeConverter implements ParamConverter<LocalDateTime> {

        @Override
        public LocalDateTime fromString(String s) {
            return DateTimeUtil.parseSqlDateTime(s);
        }

        @Override
        public String toString(LocalDateTime localDateTime) {
            System.out.println("LocalDateTime toString");
            return DateTimeUtil.toSqlDateTime(localDateTime);
        }

    }
}
