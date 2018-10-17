package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.logic.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;

public class DateFormLogic extends LogicUtil {

    public static LocalDate dateFormInputsToLocalDate(DateFormInputs inputs, String name, ErrorMessages em) {
        if (inputs == null || inputs.isEmpty()) {
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

    private static DateFormInputs implLocalDateToDateFormInputs(LocalDate date, Gengou defaultGengou,
                                                                String name, ErrorMessages em) {
        if (date == null) {
            return new DateFormInputs(defaultGengou, "", "", "");
        }
        try {
            JapaneseDate jd = JapaneseDate.from(date);
            Gengou gengou = Gengou.fromEra(jd.getEra());
            DateFormInputs inputs = new DateFormInputs(gengou);
            int nen = jd.get(ChronoField.YEAR_OF_ERA);
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();
            inputs.nen = "" + nen;
            inputs.month = "" + month;
            inputs.day = "" + day;
            return inputs;
        } catch (DateTimeException ex) {
            em.add(nameWith(name, "が") + "不適切は日付です。");
            return null;
        }
    }

    public static Converter<LocalDate, DateFormInputs> nullableLocalDateToDateFormInputs(Gengou defaultGengou) {
        return (date, name, em) -> implLocalDateToDateFormInputs(date, defaultGengou, name, em);
    }

    public static DateFormInputs localDateToDateFormInputs(LocalDate date, String name, ErrorMessages em) {
        return implLocalDateToDateFormInputs(date, null, name, em);
    }

    public static void isNotEmptyDateFormInputs(DateFormInputs inputs, String name, ErrorMessages em) {
        if (inputs != null && inputs.isEmpty()) {
            em.add(nameWith(name, "が") + "空白です。");
        }
    }

    public static BiLogic<String> dateFormValidIntervalToSqldate(BiLogic<DateFormInputs> src){
        return src
                .validate(Validators::isNotNull, Validators::valid)
                .validate(DateFormLogic::isNotEmptyDateFormInputs, Validators::valid)
                .convert(DateFormLogic::dateFormInputsToLocalDate)
                .validate(BiValidators::isValidInterval)
                .map(Mappers::localDateToSqldate);
    }

    public static Logic<DateFormInputs> validFromSqldateToDateFormInputs(Logic<String> sqldateLogic){
        return sqldateLogic
                .validate(Validators::isNotNull)
                .convert(Converters::sqldateToLocalDate)
                .validate(Validators::isNotNull)
                .convert(DateFormLogic::localDateToDateFormInputs);
    }

    public static Logic<DateFormInputs> validUptoSqldateToDateFormInputs(Logic<String> sqldateLogic){
        return sqldateLogic
                .convert(Converters::sqldateToLocalDate)
                .convert(nullableLocalDateToDateFormInputs(Gengou.Current));
    }

}
