package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class CheckSaishinByoumei extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckSaishinByoumei.class);

    CheckSaishinByoumei(List<VisitFull2DTO> visits, Masters masters, List<DiseaseFullDTO> diseases) {
        super(visits, masters, diseases);
    }

    void check(boolean fixit){
        forEachVisit(visit -> {
            List<DiseaseFullDTO> ds = listDisease(visit);
            int nShoshin = countShoshinGroup(visit);
            int nSaishin = countSaishinGroup(visit);
            if( nShoshin == 0 && nSaishin > 0 ) {
                if (ds.size() == 0) {
                    error("再診時に継続病名なし");
                }
            }
        });
    }

}