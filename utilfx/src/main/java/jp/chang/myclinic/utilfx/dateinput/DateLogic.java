package jp.chang.myclinic.utilfx.dateinput;

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

    public ObjectProperty<Gengou> gengouProperty(){
        return gengouLogic.gengouProperty();
    }

    public StringProperty nenProperty(){
        return nenLogic.nenProperty();
    }

    public StringProperty monthProperty(){
        return monthLogic.monthProperty();
    }

    public StringProperty dayProperty(){
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
    public boolean setValue(LocalDate value, ErrorMessages em) {
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
        return em.hasErrorSince(ne);
    }

//    @Override
//    public LocalDate getValue(ErrorMessages em) {
//        LocalDate value = null;
//        if (!isEmpty()) {
//            int nen = 0, month = 0, day = 0;
//            try {
//                nen = Integer.parseInt(this.nen.getValue());
//                if( nen <= 0 ){
//                    em.add("年の値が正の数値でありません。");
//                }
//            } catch (NumberFormatException ex) {
//                em.add("年の入力が数値でありません。");
//            }
//            try {
//                month = Integer.parseInt(this.month.getValue());
//                if( !(month >= 1 && month <= 12 ) ){
//                    em.add("月の値が適切でありません。")
//                }
//            } catch (NumberFormatException ex) {
//                em.add("月の入力が数値でありません。");
//            }
//            try {
//                day = Integer.parseInt(this.day.getValue());
//                if( !(day >= 1 && day <= 31) ){
//                    em.add("日の値が適切でありません。");
//                }
//            } catch (NumberFormatException ex) {
//                em.add("日の入力が数値でありません。");
//            }
//            {
//                Gengou gengou = this.gengou.getValue();
//                if (gengou == null) {
//                    em.add("元号が設定されていません。");
//                } else {
//                    try {
//                        value = LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
//                    } catch (DateTimeException ex) {
//                        em.add("日付の入力が不適切です。");
//                    }
//                }
//            }
//        } else {
//            em.add("日付が入力されていません。");
//        }
//        if (err.size() > 0) {
//            if (errorHandler != null) {
//                errorHandler.accept(String.join(errorSeparator, err));
//            }
//            return null;
//        } else {
//            return value;
//        }
//    }
//
//    public void clear() {
//        nen.setValue("");
//        month.setValue("");
//        day.setValue("");
//    }
//
//    @Override
//    public String setValue(LocalDate value) {
//        if (value == null) {
//            setNen("");
//            setMonth("");
//            setDay("");
//            return null;
//        } else {
//            try {
//                JapaneseDate jd = JapaneseDate.from(value);
//                Gengou gengou = Gengou.fromEra(jd.getEra());
//                setGengou(gengou);
//                int nen = jd.get(ChronoField.YEAR_OF_ERA);
//                int month = value.getMonthValue();
//                int day = value.getDayOfMonth();
//                setNen("" + nen);
//                setMonth("" + month);
//                setDay("" + day);
//                return null;
//            } catch (DateTimeException ex) {
//                return "日付の形式が適切でありません。";
//            }
//        }
//    }
//
//    public static Converter<LocalDate, String> fromStorageConverter = (storage, handler) -> {
//        try {
//            return LocalDate.parse(storage);
//        } catch (NumberFormatException ex) {
//            if (handler != null) {
//                handler.accept("数値の形式が不適切です。");
//            }
//            return null;
//        }
//    };
//
//    public static Converter<String, LocalDate> toStorageConverter = (date, handler) -> date.toString();

}
