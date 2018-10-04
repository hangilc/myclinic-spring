package jp.chang.myclinic.util.logic.date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;

public class DateLogic implements Logic<LocalDate> {

    //private static Logger logger = LoggerFactory.getLogger(DateLogic.class);
    private GengouLogic gengouLogic = new GengouLogic();
    private NenLogic nenLogic = new NenLogic();
    private MonthLogic monthLogic = new MonthLogic();
    private DayLogic dayLogic = new DayLogic();

    public boolean isEmpty() {
        return nenLogic.isEmpty() && monthLogic.isEmpty() && dayLogic.isEmpty();
    }

    public void clear() {
        nenLogic.clear();
        monthLogic.clear();
        dayLogic.clear();
    }

    public ObjectProperty<Gengou> gengouProperty() {
        return gengouLogic.gengouProperty();
    }

    public StringProperty nenProperty() {
        return nenLogic.nenProperty();
    }

    public StringProperty monthProperty() {
        return monthLogic.monthProperty();
    }

    public StringProperty dayProperty() {
        return dayLogic.dayProperty();
    }

    @Override
    public LocalDate getValue(ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        Gengou gengou = gengouLogic.getValue(em);
        Integer nen = nenLogic.getValue(em);
        Integer month = monthLogic.getValue(em);
        Integer day = dayLogic.getValue(em);
        if (em.hasErrorSince(ne)) {
            return null;
        } else {
            try {
                return LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
            } catch (DateTimeException ex) {
                em.add("日付の入力が不適切です。");
                return null;
            }
        }
    }

    @Override
    public void setValue(LocalDate value, ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        if (value == null) {
            clear();
        } else {
            try {
                JapaneseDate jd = JapaneseDate.from(value);
                Gengou gengou = Gengou.fromEra(jd.getEra());
                gengouLogic.setValue(gengou, em);
                int nen = jd.get(ChronoField.YEAR_OF_ERA);
                int month = value.getMonthValue();
                int day = value.getDayOfMonth();
                nenLogic.setValue(nen, em);
                monthLogic.setValue(month, em);
                dayLogic.setValue(day, em);
            } catch (DateTimeException ex) {
                em.add("日付の形式が適切でありません。");
            }
        }
    }

    @Override
    public LocalDate fromStorageValue(String storageValue, ErrorMessages em) {
        try {
            return LocalDate.parse(storageValue);
        } catch (NumberFormatException ex) {
            em.add("数値の形式が不適切です。");
            return null;
        }
    }

    @Override
    public String toStorageValue(LocalDate value, ErrorMessages em) {
        if( value == null ){
            em.add("Null LocalDate value.");
            return null;
        } else {
            return value.toString();
        }
    }

}
