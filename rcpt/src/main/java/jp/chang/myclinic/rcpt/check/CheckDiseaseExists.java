package jp.chang.myclinic.rcpt.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CheckDiseaseExists extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckDiseaseExists.class);

    CheckDiseaseExists(Scope scope) {
        super(scope);
    }

    void check(boolean fixit){
        if( getDiseases().size() == 0 ){
            error("病名なし");
        }
    }

}
