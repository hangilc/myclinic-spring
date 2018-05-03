package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class CheckKouseishinyaku extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckKouseishinyaku.class);

    CheckKouseishinyaku(List<VisitFull2DTO> visits, Masters masters, List<DiseaseFullDTO> diseases) {
        super(visits, masters, diseases);
    }

    void check(boolean fixit){
        forEachVisit(visit -> {
            List<DrugFullDTO> ds = filterDrug(visit, this::isMadoku);
            int nMadokuShinryou = countShinryouMaster(visit, getMasters().向精神薬);
            if( ds.size() > 0 ){
                if( nMadokuShinryou == 0){
                    error("調剤・処方料（麻・向・覚・毒）抜け");
                    if( fixit ) {
                        enterShinryou(visit, getMasters().向精神薬);
                        info("FIXED");
                    }
                } else if( nMadokuShinryou > 1 ){
                    error("調剤・処方料（麻・向・覚・毒）重複");
                    if( fixit ){
                        removeExtraShinryou(visit, getMasters().向精神薬, 1);
                        info("FIXED");
                    }
                }
            } else {
                if( nMadokuShinryou > 0 ){
                    error("調剤・処方料（麻・向・覚・毒）請求不可");
                    if( fixit ){
                        removeExtraShinryou(visit, getMasters().向精神薬, 0);
                        info("FIXED");
                    }
                }
            }
        });
    }

}
