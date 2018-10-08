package jp.chang.myclinic.util.value;

import javafx.beans.property.StringProperty;

public class Providers {

    private Providers() {

    }

    public static ValueProvider<String> stringFromProperty(StringProperty source){
        return em -> source.getValue();
    }

    public static ValueProvider<String> stringImmediate(String source){
        return em -> source;
    }

}
