package jp.chang.myclinic.util.value.date;

import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.value.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;

import static jp.chang.myclinic.util.value.Converters.stringToInteger;
import static jp.chang.myclinic.util.value.Validators.inRange;

public class DateFormLogic implements Logic<LocalDate> {

    private static Logger logger = LoggerFactory.getLogger(DateFormLogic.class);

    private ObjectPropertyLogic<Gengou> gengouSource = new ObjectPropertyLogic<Gengou>();
    private StringPropertyLogic nenSource = new StringPropertyLogic();
    private StringPropertyLogic monthSource = new StringPropertyLogic();
    private StringPropertyLogic daySource = new StringPropertyLogic();
    private Logic<Gengou> gengouLogic;
    private Logic<Integer> nenLogic;
    private Logic<Integer> monthLogic;
    private Logic<Integer> dayLogic;

    public DateFormLogic() {
        this.gengouLogic = gengouSource;
        nenLogic = nenSource
                .convert(stringToInteger())
                .validate(inRange(1, Integer.MAX_VALUE));
        monthLogic = monthSource
                .convert(stringToInteger())
                .validate(inRange(1, 12));
        dayLogic = daySource
                .convert(stringToInteger())
                .validate(inRange(1, 31));
    }

    public void setGengouSource(Logic<Gengou> src) {
        this.gengouLogic = src;
    }

    public void setNenSource(StringProperty src) {
        nenSource.setProperty(src);
    }

    public void setMonthSource(StringProperty src) {
        monthSource.setProperty(src);
    }

    public void setDaySource(StringProperty src) {
        daySource.setProperty(src);
    }

    @Override
    public LocalDate getValue(String name, ErrorMessages em) {
        ErrorMessages dateErrors = new ErrorMessages();
        Gengou gengou = gengouLogic.getValue("元号", dateErrors);
        Integer nen = nenLogic.getValue("年", dateErrors);
        Integer month = monthLogic.getValue("月", dateErrors);
        Integer day = dayLogic.getValue("日", dateErrors);
        if (dateErrors.hasError()) {
            em.add(String.format("%sの設定が不適切です。", name));
            em.indent();
            em.add(dateErrors);
            em.unindent();
            return null;
        } else {
            try {
                return LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
            } catch (DateTimeException ex) {
                em.add(String.format("%sの入力が不適切です。", name));
                return null;
            }
        }
    }

    public boolean isEmpty(){
        return nenSource.isEmpty() && monthSource.isEmpty() && daySource.isEmpty();
    }

    public void clear(){
        nenSource.clear();
        monthSource.clear();
        daySource.clear();
    }

    public void setValue(LocalDate value){
        if (value == null) {
            clear();
        } else {
            try {
                JapaneseDate jd = JapaneseDate.from(value);
                Gengou gengou = Gengou.fromEra(jd.getEra());
                gengouSource.setValue(gengou);
                int nen = jd.get(ChronoField.YEAR_OF_ERA);
                int month = value.getMonthValue();
                int day = value.getDayOfMonth();
                nenSource.setValue("" + nen);
                monthSource.setValue("" + month);
                daySource.setValue("" + day);
            } catch (DateTimeException ex) {
                logger.error("Invalid date.", ex);
            }
        }
    }

    public void setValueFromStorage(String store){
        if( store == null ){
            setValue(null);
        } else {
            try {
                LocalDate date = LocalDate.parse(store);
                setValue(date);
            } catch(DateTimeException ex){
                logger.error("Invalid ate.", ex);
            }
        }
    }

}
