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

    @Override
    public T getValue(String name, ErrorMessages em){
        if( prop == null ){
            em.add(String.format("Null property. (%s)", name));
            return null;
        }
        return prop.getValue();
    }

}
