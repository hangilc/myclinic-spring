package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DrugFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class CheckGaiyou extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckGaiyou.class);
    private int 外用調剤;

    CheckGaiyou(Scope scope) {
        super(scope);
        this.外用調剤 = getShinryouMaster().外用調剤;
    }

    void check(boolean fixit){
        forEachVisit(visit -> {
            List<DrugFullDTO> ds = filterDrug(visit, this::isGaiyou);
            int nGaiyouChouzai = countShinryouMaster(visit, 外用調剤);
            if( ds.size() > 0 ){
                if( nGaiyouChouzai == 0 ){
                    error("調剤料（外用薬）抜け", fixit, () -> enterShinryou(visit, 外用調剤));
                } else if( nGaiyouChouzai > 1 ){
                    error("調剤料（外用薬）重複", fixit, () ->
                            removeExtraShinryouMaster(visit, 外用調剤, 1));
                }
            } else {
                if( nGaiyouChouzai > 0 ){
                    error("調剤料（外用薬）不可", fixit, () ->
                            removeExtraShinryouMaster(visit, 外用調剤, 0));
                }
            }
        });
    }

}
