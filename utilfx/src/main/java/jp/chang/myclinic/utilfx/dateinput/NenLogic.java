package jp.chang.myclinic.utilfx.dateinput;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;

class NenLogic implements Logic<Integer> {

    //private static Logger logger = LoggerFactory.getLogger(NenLogic.class);
    private StringProperty nen = new SimpleStringProperty();

    public boolean isEmpty(){
        String text = nen.getValue();
        return text == null || text.isEmpty();
    }

    public void clear(){
        nen.setValue("");
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

    @Override
    public Integer getValue(ErrorMessages em) {
        if( isEmpty() ){
            em.add("年の値が設定されていません。");
            return null;
        }
        try {
            int value = Integer.parseInt(nen.getValue());
            if( !(value > 0) ){
                em.add("年の値が正でありません。");
                return null;
            }
            return value;
        } catch(NumberFormatException ex){
            em.add("年の値が数値でありません。");
            return null;
        }
    }

    @Override
    public void setValue(Integer value, ErrorMessages em) {
        if( value == null ){
            clear();
        } else {
            nen.setValue("" + value);
        }
    }

}
