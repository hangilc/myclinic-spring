package jp.chang.myclinic.rcpt.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CheckByoumei extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckByoumei.class);

    CheckByoumei(Scope scope) {
        super(scope);
    }

    void check(boolean fixit){
        checkHbA1c(fixit);
        checkPSA(fixit);
    }

    void checkHbA1c(boolean fixit){
        forEachVisit(visit -> {
            int n = countShinryouMaster(visit, getShinryouMaster().ＨｂＡ１ｃ);
            if( n > 0 ){

            }
        });
    }

    void checkPSA(boolean fixit){
        forEachVisit(visit -> {
            int n = countShinryouMaster(visit, getShinryouMaster().ＰＳＡ);
            if( n > 0 ){

            }
        });
    }

}
