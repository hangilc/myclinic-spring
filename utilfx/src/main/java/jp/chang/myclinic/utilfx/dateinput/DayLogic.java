package jp.chang.myclinic.utilfx.dateinput;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;

class DayLogic implements Logic<Integer> {

    //private static Logger logger = LoggerFactory.getLogger(NenLogic.class);
    private StringProperty day = new SimpleStringProperty();

    public boolean isEmpty(){
        String text = day.getValue();
        return text == null || text.isEmpty();
    }

    public void clear(){
        day.setValue("");
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

    @Override
    public Integer getValue(ErrorMessages em) {
        if( isEmpty() ){
            em.add("日の値が設定されていません。");
            return null;
        }
        try {
            int value = Integer.parseInt(day.getValue());
            if( !(value >= 1 && value <= 31) ){
                em.add("日の値が適切な範囲内でありません。");
                return null;
            }
            return value;
        } catch(NumberFormatException ex){
            em.add("日の値が数値でありません。");
            return null;
        }
    }

    @Override
    public boolean setValue(Integer value, ErrorMessages em) {
        if( value == null ){
            clear();
        } else {
            day.setValue("" + value);
        }
        return true;
    }

}
