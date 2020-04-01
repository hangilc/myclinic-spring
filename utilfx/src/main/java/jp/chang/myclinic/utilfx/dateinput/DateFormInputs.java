package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.GengouNenPair;
import jp.chang.myclinic.util.kanjidate.KanjiDate;

import java.time.LocalDate;
import java.util.Optional;

public class DateFormInputs {

    public Gengou gengou;
    public String nen;
    public String month;
    public String day;

    public DateFormInputs(){

    }

    public DateFormInputs(Gengou gengou){
        this.gengou = gengou;
    }

    public DateFormInputs(Gengou gengou, String nen, String month, String day){
        this.gengou = gengou;
        this.nen = nen;
        this.month = month;
        this.day = day;
    }

    public DateFormInputs(Gengou gengou, int nen, int month, int day){
        this(gengou, "" + nen, "" + month, "" + day);
    }

    public boolean isEmpty(){
        return (nen == null || nen.isEmpty()) && (month == null || month.isEmpty()) &&
                (day == null || day.isEmpty());
    }

    public void clear(){
        nen = "";
        month = "";
        day = "";
    }

    public void setDate(LocalDate date){
        try {
            GengouNenPair gp = KanjiDate.yearToGengou(date);
            this.gengou = gp.gengou;
            this.nen = String.format("%d", gp.nen);
            this.month = String.format("%d", date.getMonthValue());
            this.day = String.format("%d", date.getDayOfMonth());
        } catch(IllegalArgumentException ex){
            ex.printStackTrace();
        }
    }

    public Optional<LocalDate> getDate() {
        try {
            int nenValue = Integer.parseInt(this.nen);
            KanjiDate.gengouToYear(gengou, nenValue);
            int monthValue = Integer.parseInt(this.month);
            int dayValue= Integer.parseInt(this.day);
            int yearValue = KanjiDate.gengouToYear(this.gengou, nenValue);
            return Optional.of(LocalDate.of(yearValue, monthValue, dayValue));
        } catch(Exception ex){
            return Optional.empty();
        }
    }

}
