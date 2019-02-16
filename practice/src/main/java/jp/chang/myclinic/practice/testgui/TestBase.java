package jp.chang.myclinic.practice.testgui;

class TestBase {

    void confirm(boolean bool, String errorMessage){
        if( !bool ){
            throw new RuntimeException(errorMessage + " in (" + getClass() + ")");
        }
    }
}
