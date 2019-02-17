package jp.chang.myclinic.practice.testgui;

import org.jetbrains.annotations.Contract;

class TestBase {

    @Contract("false, _ -> fail")
    void confirm(boolean bool, String errorMessage){
        if( !bool ){
            throw new RuntimeException(errorMessage + " in (" + getClass() + ")");
        }
    }

}
