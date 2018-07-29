package jp.chang.myclinic.practice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class MainScope {

    //private static Logger logger = LoggerFactory.getLogger(MainContext.class);

    @Value("${jp.chang.myclinic.debug.http:false}")
    public boolean debugHttp;
}
