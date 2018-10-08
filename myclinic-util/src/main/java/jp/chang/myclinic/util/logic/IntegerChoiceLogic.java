package jp.chang.myclinic.util.logic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class IntegerChoiceLogic implements Logic<Integer> {

    private ObjectProperty<Integer> valueProperty = new SimpleObjectProperty<>();
    private String name;
    private Integer[] choices;

    public IntegerChoiceLogic(String name, Integer... choices) {
        this.name = name;
        this.choices = choices;
    }

    public ObjectProperty<Integer> valueProperty(){
        return valueProperty;
    }

    public String getName() {
        return name;
    }

    @Override
    public Integer getValue(ErrorMessages em) {
        Integer intValue = valueProperty.getValue();
        if( intValue == null ){
            em.add(String.format("%sが設定されていません。", name));
            return null;
        }
        if(isNotRightChoice(intValue)){
            em.add(String.format("%sの値が適切でありません。", getName()));
            return null;
        }
        return intValue;
    }

    @Override
    public void setValue(Integer value, ErrorMessages em) {
        if( value == null ){
            em.add(String.format("%sの値が設定されていません。。", getName()));
            return;
        }
        if(isNotRightChoice(value)){
            em.add(String.format("%sの値が適切でありません。", getName()));
            return;
        }
        valueProperty.setValue(value);
    }

    private boolean isNotRightChoice(int value){
        for (Integer choice : choices) {
            if (choice == value) {
                return false;
            }
        }
        return true;
    }
}
