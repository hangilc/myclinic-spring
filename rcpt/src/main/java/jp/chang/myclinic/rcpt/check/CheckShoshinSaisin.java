package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;

import java.util.List;

class CheckShoshinSaisin extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckShoshinSaisin.class);

    CheckShoshinSaisin(List<VisitFull2DTO> visits, Masters masters) {
        super(visits, masters);
    }

    void check(boolean fixit){
        forEachVisit(visit -> {
            int nShoshin = countShinryouMaster(visit, getMasters().初診);
            int nSaishin = countShinryouMaster(visit, getMasters().再診) +
                    countShinryouMaster(visit, getMasters().同日再診);
            int n = nShoshin + nSaishin;
            if( n == 0 ){
                error("初診再診もれ");
            } else if( n > 1 ){
                error("初診再診重複");
            }
        });
    }

}
