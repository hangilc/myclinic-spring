package jp.chang.myclinic.utilfx.dateinput;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Gengou;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DateInputLogic {

    //private static Logger logger = LoggerFactory.getLogger(DateInputLogic.class);
    private ObjectProperty<Gengou> gengou = new SimpleObjectProperty<Gengou>(Gengou.Current);
    private StringProperty nen = new SimpleStringProperty();
    private StringProperty month = new SimpleStringProperty();
    private StringProperty day = new SimpleStringProperty();
    private boolean nullAllowed = false;

    public boolean isEmpty() {
        return nen.isEmpty().getValue() &&
                month.isEmpty().getValue() &&
                day.isEmpty().getValue();
    }

    private boolean isNullAllowed() {
        return nullAllowed;
    }

    public Gengou getGengou() {
        return gengou.get();
    }

    public ObjectProperty<Gengou> gengouProperty() {
        return gengou;
    }

    public void setGengou(Gengou gengou) {
        this.gengou.set(gengou);
    }

    public String getNen() {
        return nen.get();
    }

    public StringProperty nenProperty() {
        return nen;
    }

    public void setNen(String nen) {
        this.nen.set(nen);
    }

    public String getMonth() {
        return month.get();
    }

    public StringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public String getDay() {
        return day.get();
    }

    public StringProperty dayProperty() {
        return day;
    }

    public void setDay(String day) {
        this.day.set(day);
    }

    public void setNullAllowed(boolean nullAllowed) {
        this.nullAllowed = nullAllowed;
    }

    public LocalDate getValue(Consumer<List<String>> errorHandler) {
        List<String> err = new ArrayList<>();
        LocalDate value = null;
        if (!isEmpty()) {
            int nen = 0, month = 0, day = 0;
            try {
                nen = Integer.parseInt(this.nen.getValue());
            } catch (NumberFormatException ex) {
                err.add("年の入力が不適切です。");
            }
            try {
                month = Integer.parseInt(this.month.getValue());
            } catch (NumberFormatException ex) {
                err.add("月の入力が不適切です。");
            }
            try {
                day = Integer.parseInt(this.day.getValue());
            } catch (NumberFormatException ex) {
                err.add("日の入力が不適切です。");
            }
            {
                Gengou gengou = this.gengou.getValue();
                if (gengou == null) {
                    err.add("元号が設定されていません。");
                } else {
                    try {
                        value = LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
                    } catch (DateTimeException ex) {
                        err.add("日付の入力が不適切です。");
                    }
                }
            }
        } else {
            if (isNullAllowed()) {
                value = LocalDate.MAX;
            } else {
                err.add("日付が入力されていません。");
            }
        }
        if (err.size() > 0) {
            if (errorHandler != null) {
                errorHandler.accept(err);
            }
            return null;
        } else {
            return value;
        }
    }

    public void clear(){
        nen.setValue("");
        month.setValue("");
        day.setValue("");
    }

    public void setValue(LocalDate value){
        if (value == null) {
            setNen("");
            setMonth("");
            setDay("");
        } else {
            JapaneseDate jd = JapaneseDate.from(value);
            Gengou gengou = Gengou.fromEra(jd.getEra());
            setGengou(gengou);
            int nen = jd.get(ChronoField.YEAR_OF_ERA);
            int month = value.getMonthValue();
            int day = value.getDayOfMonth();
            setNen("" + nen);
            setMonth("" + month);
            setDay("" + day);
        }
    }
}
