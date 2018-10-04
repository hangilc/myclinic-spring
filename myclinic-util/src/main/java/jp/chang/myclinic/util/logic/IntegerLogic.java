package jp.chang.myclinic.util.logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IntegerLogic implements Logic<Integer> {

    //private static Logger logger = LoggerFactory.getLogger(IntegerLogic.class);
    private StringProperty input = new SimpleStringProperty();
    private String name;

    public IntegerLogic(String name){
        this.name = name;
    }

    public boolean isEmpty(){
        String text = input.getValue();
        return text == null || text.isEmpty();
    }

    public void clear(){
        input.setValue("");
    }

    public String getName(){
        return name;
    }

    @Override
    public Integer getValue(ErrorMessages em) {
        return fromStorageValue(input.getValue(), em);
    }

    @Override
    public void setValue(Integer value, ErrorMessages em) {
        if( value == null ){
            clear();
        } else {
            input.setValue("" + value);
        }
    }

    @Override
    public Integer fromStorageValue(String text, ErrorMessages em) {
        if( !validate(() -> text != null && !text.isEmpty(), em, "%sが設定されていません。", name) ) {
            return null;
        }
        try {
            int value = Integer.parseInt(text);
            if( !validate(() -> value > 0, em, "%sが正の値でありません。", name) ) {
                return null;
            }
            return value;
        } catch (NumberFormatException ex) {
            em.add(String.format("%sの入力が数値でありません。", name));
            return null;
        }
    }

    @Override
    public String toStorageValue(Integer value, ErrorMessages em) {
        if( value == null ){
            em.add(String.format("%sの値が null です。", name));
            return null;
        } else {
            return String.format("%d", value);
        }
    }

}
