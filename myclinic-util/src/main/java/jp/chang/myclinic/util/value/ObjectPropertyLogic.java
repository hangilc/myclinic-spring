package jp.chang.myclinic.util.value;

import javafx.beans.property.ObjectProperty;

public class ObjectPropertyLogic<T> implements Logic<T> {

    private ObjectProperty<T> prop;

    public ObjectPropertyLogic(ObjectProperty<T> prop){
        this.prop = prop;
    }

    @Override
    public T getValue(String name, ErrorMessages em){
        return prop.getValue();
    }

}
