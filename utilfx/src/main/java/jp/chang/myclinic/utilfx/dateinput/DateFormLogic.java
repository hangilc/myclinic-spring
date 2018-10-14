package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.logic.Converter;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicValue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;

import static jp.chang.myclinic.util.logic.Converters.stringToInteger;
import static jp.chang.myclinic.util.logic.Validators.*;

public class DateFormLogic {

    public static Converter<DateFormInputs, LocalDate> dateFormInputsToLocalDate() {
        return (inputs, name, em) -> {
            if (inputs == null || inputs.isEmpty()) {
                return null;
            }
            ErrorMessages emDate = new ErrorMessages();
            Gengou gengou = new LogicValue<>(inputs.gengou)
                    .validate(isNotNull())
                    .getValue("元号", emDate);
            int nen = new LogicValue<>(inputs.nen)
                    .validate(isNotNull())
                    .validate(isNotEmpty())
                    .convert(stringToInteger())
                    .validate(isInRange(1, Integer.MAX_VALUE))
                    .getValueOrElse(0, "年", emDate);
            int month = new LogicValue<>(inputs.month)
                    .validate(isNotNull())
                    .validate(isNotEmpty())
                    .convert(stringToInteger())
                    .validate(isInRange(1, 12))
                    .getValueOrElse(0, "月", emDate);
            int day = new LogicValue<>(inputs.day)
                    .validate(isNotNull())
                    .validate(isNotEmpty())
                    .convert(stringToInteger())
                    .validate(isInRange(1, 31))
                    .getValueOrElse(0, "日", emDate);
            if (emDate.hasError()) {
                em.add(String.format("%sの内容が不適切です。", name));
                em.indent();
                em.add(emDate);
                em.unindent();
                return null;
            } else {
                try {
                    return LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
                } catch (DateTimeException ex) {
                    em.add(String.format("%sの内容が不適切です。", name));
                    return null;
                }
            }
        };
    }

    public static Converter<LocalDate, DateFormInputs> localDateToDateFormInputs(){
        return (date, name, em) -> {
            if( date == null ){
                return null;
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
                em.add(String.format("%sが不適切は日付です。", name));
                return null;
            }
        };
    }

}
