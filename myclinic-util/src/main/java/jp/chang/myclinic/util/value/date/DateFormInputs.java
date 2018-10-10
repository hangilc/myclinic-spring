package jp.chang.myclinic.util.value.date;

import jp.chang.myclinic.consts.Gengou;

public class DateFormInputs {

    public Gengou gengou;
    public String nen;
    public String month;
    public String day;

    public DateFormInputs(Gengou gengou){
        this.gengou = gengou;
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
}
