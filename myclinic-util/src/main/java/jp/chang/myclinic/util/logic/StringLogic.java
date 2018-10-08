package jp.chang.myclinic.util.logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringLogic implements Logic<String>{

    private StringProperty string = new SimpleStringProperty();

    public StringProperty stringProperty(){
        return string;
    }

    @Override
    public String getValue(ErrorMessages em) {
        String value = string.getValue();
        if( value == null ){
            value = "";
        }
        return value;
    }

    @Override
    public void setValue(String value, ErrorMessages em) {
        string.setValue(value);
    }

}
