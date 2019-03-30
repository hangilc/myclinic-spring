package jp.chang.myclinic.backenddb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CannotDeleteVisitSafelyException extends RuntimeException {

    public CannotDeleteVisitSafelyException(String msg) {
        super(msg);
    }

}
