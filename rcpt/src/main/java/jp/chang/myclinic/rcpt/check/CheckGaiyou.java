package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class CheckGaiyou extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckGaiyou.class);

    CheckGaiyou(List<VisitFull2DTO> visits, Masters masters) {
        super(visits, masters);
    }

    void check(boolean fixit){
        forEachVisit(visit -> {
            List<DrugFullDTO> ds = filterDrug(visit, this::isGaiyou);
            int nGaiyouChouzai = countShinryouMaster(visit, getMasters().外用調剤);
            if( ds.size() > 0 ){
                if( nGaiyouChouzai == 0 ){
                    error("調剤料（外用薬）抜け");
                    enterShinryou(visit, getMasters().外用調剤);
                    info("FIXED");
                } else if( nGaiyouChouzai > 1 ){
                    error("調剤料（外用薬）重複");
                    removeExtraShinryou(visit, getMasters().外用調剤, 1);
                    info("FIXED");
                }
            } else {
                if( nGaiyouChouzai > 0 ){
                    error("調剤料（外用薬）不可");
                    removeExtraShinryou(visit, getMasters().外用調剤, 0);
                    info("FIXED");
                }
            }
        });
    }

}
