package jp.chang.myclinic.utilfx.dateinput;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;

class MonthLogic implements Logic<Integer> {

    //private static Logger logger = LoggerFactory.getLogger(NenLogic.class);
    private StringProperty month = new SimpleStringProperty();

    public boolean isEmpty(){
        String text = month.getValue();
        return text == null || text.isEmpty();
    }

    public void clear(){
        month.setValue("");
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

    @Override
    public Integer getValue(ErrorMessages em) {
        if( isEmpty() ){
            em.add("月の値が設定されていません。");
            return null;
        }
        try {
            int value = Integer.parseInt(month.getValue());
            if( !(value >= 1 && value <= 12) ){
                em.add("月の値が適切な範囲内でありません。");
                return null;
            }
            return value;
        } catch(NumberFormatException ex){
            em.add("月の値が数値でありません。");
            return null;
        }
    }

    @Override
    public boolean setValue(Integer value, ErrorMessages em) {
        if( value == null ){
            clear();
        } else {
            month.setValue("" + value);
        }
        return true;
    }

}
