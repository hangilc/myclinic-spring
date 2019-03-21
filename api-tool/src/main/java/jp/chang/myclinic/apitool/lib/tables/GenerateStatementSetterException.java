package jp.chang.myclinic.apitool.lib.tables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateStatementSetterException extends RuntimeException {

    private String rawMessage;

    public GenerateStatementSetterException(String msg) {
        super(msg);
        this.rawMessage = msg;
    }

    public String getRawMessage(){
        return rawMessage;
    }

}
