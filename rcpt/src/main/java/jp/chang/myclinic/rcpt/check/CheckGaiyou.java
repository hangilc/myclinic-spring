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

    void check(){
        forEachVisit(visit -> {
            List<DrugFullDTO> ds = filterDrug(visit, this::isGaiyou);
            int nGaiyouChouzai = countShinryouMaster(visit, 外用調剤);
            if( ds.size() > 0 ){
                if( nGaiyouChouzai == 0 ){
                    String em = "調剤料（外用薬）を追加します。";
                    error("調剤料（外用薬）抜け", em, () -> enterShinryou(visit, 外用調剤));
                } else if( nGaiyouChouzai > 1 ){
                    String em = String.format("調剤料（外用薬）(%d件中%d件)を削除します。",
                            nGaiyouChouzai, nGaiyouChouzai - 1);
                    error("調剤料（外用薬）重複", em, () ->
                            removeExtraShinryouMaster(visit, 外用調剤, 1));
                }
            } else {
                if( nGaiyouChouzai > 0 ){
                    String em = String.format("調剤料（外用薬）(%d件)を削除します。", nGaiyouChouzai);
                    error("調剤料（外用薬）不可", em, () ->
                            removeExtraShinryouMaster(visit, 外用調剤, 0));
                }
            }
        });
    }

}
