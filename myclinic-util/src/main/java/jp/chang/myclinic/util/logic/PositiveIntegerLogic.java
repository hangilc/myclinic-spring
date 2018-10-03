package jp.chang.myclinic.util.logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PositiveIntegerLogic implements Logic<Integer> {

    //private static Logger logger = LoggerFactory.getLogger(PositiveIntegerLogic.class);
    private String name;
    private StringProperty input = new SimpleStringProperty();

    public PositiveIntegerLogic(String name){
        this.name = name;
    }

    public boolean isEmpty(){
        String text = input.getValue();
        return text == null || text.isEmpty();
    }

    public void clear(){
        input.setValue("");
    }

    @Override
    public Integer getValue(ErrorMessages em) {
        String text = input.getValue();
        if( text == null || text.isEmpty() ){
            em.add(String.format("%sが設定されていません。", name));
        } else {
            try {
                int value = Integer.parseInt(text);
                if( value > 0 ){
                    return value;
                } else {
                    em.add(String.format("%sが正の値でありません。", name));
                }
            } catch(NumberFormatException ex){
                em.add(String.format("%sの入力が数値でありません。", name));
            }
        }
        return null;
    }

    @Override
    public void setValue(Integer value, ErrorMessages em) {
        if( value == null ){
            clear();
        } else {
            input.setValue("" + value);
        }
    }
}
