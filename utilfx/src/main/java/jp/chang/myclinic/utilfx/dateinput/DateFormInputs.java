package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.logic.BiLogic;
import jp.chang.myclinic.util.logic.BiValidators;
import jp.chang.myclinic.util.logic.Mappers;
import jp.chang.myclinic.util.logic.Validators;

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
