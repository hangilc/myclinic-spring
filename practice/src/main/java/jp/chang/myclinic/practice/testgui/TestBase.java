package jp.chang.myclinic.practice.testgui;

import org.jetbrains.annotations.Contract;

public class TestBase {

    @Contract("false, _ -> fail")
    public void confirm(boolean bool, String errorMessage){
        confirm(bool, errorMessage, null);
    }

    @Contract("false, _, _ -> fail")
    public void confirm(boolean bool, String errorMessage, Runnable auxProc){
        if( !bool ){
            if( auxProc != null ){
                auxProc.run();
            }
            throw new RuntimeException(errorMessage + " in (" + getClass() + ")");
        }
    }

}
