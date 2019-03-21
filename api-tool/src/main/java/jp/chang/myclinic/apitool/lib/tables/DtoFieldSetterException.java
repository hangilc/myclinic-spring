package jp.chang.myclinic.apitool.lib.tables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DtoFieldSetterException extends RuntimeException {

    private String rawMessage;

    DtoFieldSetterException(String msg) {
        super(msg);
        this.rawMessage = msg;
    }

    public String getRawMessage(){
        return rawMessage;
    }

}
