package jp.chang.myclinic.backenddb.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLIntegrityConstraintViolationException;

public class IntegrityException extends RuntimeException {

    public IntegrityException(SQLIntegrityConstraintViolationException e) {
        super(e);
    }

}
