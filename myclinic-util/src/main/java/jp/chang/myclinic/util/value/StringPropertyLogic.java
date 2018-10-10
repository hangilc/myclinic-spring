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

    private String getCheckedValue(){
        if( prop == null ){
            return null;
        } else {
            String value = prop.getValue();
            if( value == null ){
                return null;
            } else if( value.isEmpty() ){
                return null;
            } else {
                return value;
            }
        }
    }

    public boolean isEmpty(){
        return getCheckedValue() != null;
    }

    public void clear(){
        if( prop != null ){
            prop.setValue("");
        }
    }

    @Override
    public String getValue(String name, ErrorMessages em) {
        String value = getCheckedValue();
        if( value == null ){
            em.add(String.format("%sが設定されていません。", name));
            return null;
        } else {
            return value;
        }
    }

}
