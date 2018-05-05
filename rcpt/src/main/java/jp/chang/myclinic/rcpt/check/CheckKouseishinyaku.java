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

    void check(){
        forEachVisit(visit -> {
            List<DrugFullDTO> ds = filterDrug(visit, this::isMadoku);
            int nMadokuShinryou = countShinryouMaster(visit, sm.向精神薬);
            if( ds.size() > 0 ){
                if( nMadokuShinryou == 0){
                    String em = "調剤・処方料（麻・向・覚・毒）を追加します。";
                    error("調剤・処方料（麻・向・覚・毒）抜け", em, () ->
                            enterShinryou(visit, sm.向精神薬));
                } else if( nMadokuShinryou > 1 ){
                    String em = messageForRemoveExtra("調剤・処方料（麻・向・覚・毒）",
                            nMadokuShinryou, 1);
                    error("調剤・処方料（麻・向・覚・毒）重複", em, () ->
                            removeExtraShinryouMaster(visit, sm.向精神薬, 1)
                    );
                }
            } else {
                if( nMadokuShinryou > 0 ){
                    String em = "調剤・処方料（麻・向・覚・毒）を削除します。";
                    error("調剤・処方料（麻・向・覚・毒）請求不可", em, () ->
                            removeExtraShinryouMaster(visit, sm.向精神薬, 0)
                    );
                }
            }
        });
    }

}
