package jp.chang.myclinic.util.value;

import javafx.beans.property.ObjectProperty;

public class ObjectPropertyLogic<T> implements Logic<T> {

    private ObjectProperty<T> prop;

    public void setProperty(ObjectProperty<T> prop){
        this.prop = prop;
    }

    public void setValue(T value){
        if( prop != null ) {
            prop.setValue(value);
        }
    }

    private T getCheckedValue(){
        if( prop == null ){
            return null;
        } else {
            return prop.getValue();
        }
    }

    public boolean isEmpty(){
        return getCheckedValue() != null;
    }

    @Override
    public T getValue(String name, ErrorMessages em){
        T value = getCheckedValue();
        if( value == null ){
            em.add(String.format("%sが設定されていません。", name));
            return null;
        } else {
            return value;
        }
    }

}
