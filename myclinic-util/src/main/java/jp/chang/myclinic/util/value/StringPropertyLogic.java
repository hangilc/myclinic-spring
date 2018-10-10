package jp.chang.myclinic.util.value;

import javafx.beans.property.StringProperty;

public class StringPropertyLogic implements Logic<String> {

    private StringProperty prop;

    public StringPropertyLogic(StringProperty prop){
        this.prop = prop;
    }

    @Override
    public String getValue(String name, ErrorMessages em) {
        return prop.getValue();
    }

}
