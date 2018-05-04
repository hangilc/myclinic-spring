package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;

import java.util.List;

class CheckKouseishinyaku extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckKouseishinyaku.class);
    private ResolvedShinryouMap sm;

    CheckKouseishinyaku(Scope scope) {
        super(scope);
        this.sm = getShinryouMaster();
    }

    void check(boolean fixit){
        forEachVisit(visit -> {
            List<DrugFullDTO> ds = filterDrug(visit, this::isMadoku);
            int nMadokuShinryou = countShinryouMaster(visit, sm.向精神薬);
            if( ds.size() > 0 ){
                if( nMadokuShinryou == 0){
                    error("調剤・処方料（麻・向・覚・毒）抜け", fixit, () ->
                            enterShinryou(visit, sm.向精神薬));
                } else if( nMadokuShinryou > 1 ){
                    error("調剤・処方料（麻・向・覚・毒）重複", fixit, () ->
                            removeExtraShinryouMaster(visit, sm.向精神薬, 1)
                    );
                }
            } else {
                if( nMadokuShinryou > 0 ){
                    error("調剤・処方料（麻・向・覚・毒）請求不可", fixit, () ->
                            removeExtraShinryouMaster(visit, sm.向精神薬, 0)
                    );
                }
            }
        });
    }

}
