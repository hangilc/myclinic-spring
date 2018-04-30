package jp.chang.myclinic.rcpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler {

    private static Logger logger = LoggerFactory.getLogger(Handler.class);

    private Handler() { }

    public static <T> T exceptionally(Throwable t){
        logger.error("Error:", t);
        return null;
    }

}
