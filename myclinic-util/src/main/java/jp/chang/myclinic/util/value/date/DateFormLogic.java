package jp.chang.myclinic.util.value.date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.value.ErrorMessages;
import jp.chang.myclinic.util.value.Logic;
import jp.chang.myclinic.util.value.ObjectPropertyLogic;
import jp.chang.myclinic.util.value.StringPropertyLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;

import static jp.chang.myclinic.util.value.Converters.*;
import static jp.chang.myclinic.util.value.Validators.*;

public class DateFormLogic implements Logic<LocalDate> {

    private static Logger logger = LoggerFactory.getLogger(DateFormLogic.class);

    private ObjectProperty<Gengou> gengouSource = new SimpleObjectProperty<>();
    private StringProperty nenSource = new SimpleStringProperty();
    private StringProperty monthSource = new SimpleStringProperty();
    private StringProperty daySource = new SimpleStringProperty();

    private Logic<Gengou> gengouLogic;
    private Logic<Integer> nenLogic;
    private Logic<Integer> monthLogic;
    private Logic<Integer> dayLogic;

    public DateFormLogic() {
        this.gengouLogic = new ObjectPropertyLogic<Gengou>(gengouSource).validate(isNotNull());
        nenLogic = new StringPropertyLogic(nenSource)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger())
                .validate(inRange(1, Integer.MAX_VALUE));
        monthLogic = new StringPropertyLogic(monthSource)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger())
                .validate(inRange(1, 12));
        dayLogic = new StringPropertyLogic(daySource)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger())
                .validate(inRange(1, 31));
    }

    public void setGengouSource(ObjectProperty<Gengou> source) {
        source.bindBidirectional(gengouSource);
    }

    public void setNenSource(StringProperty src) {
        src.bindBidirectional(nenSource);
    }

    public void setMonthSource(StringProperty src) {
        src.bindBidirectional(monthSource);
    }

    public void setDaySource(StringProperty src) {
        src.bindBidirectional(daySource);
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

    private boolean isEmpty(StringProperty sp){
        String t = sp.getValue();
        return t == null || t.isEmpty();
    }

    public boolean isEmpty(){
        return isEmpty(nenSource) && isEmpty(monthSource) && isEmpty(daySource);
    }

    public void clear(){
        nenSource.setValue("");
        monthSource.setValue("");
        daySource.setValue("");
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
