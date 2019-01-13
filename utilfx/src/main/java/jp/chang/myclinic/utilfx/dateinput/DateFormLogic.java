package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.GengouNenPair;
import jp.chang.myclinic.util.kanjidate.KanjiDate;
import jp.chang.myclinic.util.logic.*;

import java.time.LocalDate;

public class DateFormLogic extends LogicUtil {

    public static LocalDate dateFormInputsToLocalDate(DateFormInputs inputs, String name, ErrorMessages em) {
        if( inputs.isEmpty() ){
            em.add(nameWith(name, "が") + "入力されていません。");
            return null;
        }
        Logic<Gengou> gengouLogic = new LogicValue<>(inputs.gengou)
                .validate(Validators::isNotNull);
        Logic<Integer> nenLogic = new LogicValue<>(inputs.nen)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::stringToInteger);
        Logic<Integer> monthLogic = new LogicValue<>(inputs.month)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::stringToInteger);
        Logic<Integer> dayLogic = new LogicValue<>(inputs.day)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::stringToInteger);
        return DateLogic.toLocalDate(name, gengouLogic, nenLogic, monthLogic, dayLogic, em);
    }

    public static LocalDate dateFormInputsToNullableLocalDate(DateFormInputs inputs, String name, ErrorMessages em){
        if( inputs.isEmpty() ){
            return null;
        } else {
            return dateFormInputsToLocalDate(inputs, name, em);
        }
    }

    public static DateFormInputs localDateToDateFormInputs(LocalDate date) {
        GengouNenPair gn = KanjiDate.yearToGengou(date);
        Gengou gengou = gn.gengou;
        DateFormInputs inputs = new DateFormInputs(gengou);
        int nen = gn.nen;
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        inputs.nen = "" + nen;
        inputs.month = "" + month;
        inputs.day = "" + day;
        return inputs;
    }

    public static Converter<LocalDate, DateFormInputs> nullableLocalDateToDateFormInputs(Gengou defaultGengou) {
        return (date, name, em) -> {
            if (date == null) {
                return new DateFormInputs(defaultGengou, "", "", "");
            } else {
                return localDateToDateFormInputs(date);
            }
        };
    }

    public static void isNotEmptyDateFormInputs(DateFormInputs inputs, String name, ErrorMessages em) {
        if (inputs != null && inputs.isEmpty()) {
            em.add(nameWith(name, "が") + "空白です。");
        }
    }

    public static BiLogic<String> dateFormValidIntervalToSqldate(BiLogic<DateFormInputs> src) {
        return src
                .validate(Validators::isNotNull, Validators::valid)
                .validate(DateFormLogic::isNotEmptyDateFormInputs, Validators::valid)
                .convert(DateFormLogic::dateFormInputsToNullableLocalDate)
                .validate(BiValidators::isValidInterval)
                .map(Mappers::localDateToSqldate);
    }

    public static Logic<DateFormInputs> validFromSqldateToDateFormInputs(Logic<String> sqldateLogic) {
        return sqldateLogic
                .validate(Validators::isNotNull)
                .convert(Converters::sqldateToLocalDate)
                .validate(Validators::isNotNull)
                .map(DateFormLogic::localDateToDateFormInputs);
    }

    public static Logic<DateFormInputs> validUptoSqldateToDateFormInputs(Logic<String> sqldateLogic) {
        return sqldateLogic
                .convert(Converters::sqldateToLocalDate)
                .convert(nullableLocalDateToDateFormInputs(Gengou.Current));
    }

}
