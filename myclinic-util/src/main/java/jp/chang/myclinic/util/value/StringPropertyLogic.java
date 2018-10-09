package jp.chang.myclinic.util.value;

import javafx.beans.property.StringProperty;

public class StringPropertyLogic implements Logic<String> {

    private StringProperty prop;

    public void setProperty(StringProperty prop){
        this.prop = prop;
    }

    public void setValue(String value) {
        if( prop != null ){
            prop.setValue(value);
        }
    }

    @Override
    public String getValue(String name, ErrorMessages em) {
        if( prop == null ){
            em.add(String.format("Null property. (%s)", name));
            return null;
        }
        return prop.getValue();
    }

}
