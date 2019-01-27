package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.Shuushokugo;
import jp.chang.myclinic.dto.DiseaseAdjDTO;
import jp.chang.myclinic.dto.DiseaseDTO;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;
import jp.chang.myclinic.rcpt.resolvedmap2.ResolvedDiseaseAdjMap;
import jp.chang.myclinic.rcpt.resolvedmap2.ResolvedDiseaseMap;
import jp.chang.myclinic.rcpt.resolvedmap2.ResolvedMap;
import jp.chang.myclinic.rcpt.resolvedmap2.ResolvedShinryouMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class CheckByoumei {

    //private static Logger logger = LoggerFactory.getLogger(CheckByoumei.class);
    private Scope scope;

    CheckByoumei(Scope scope) {
        this.scope = scope;
    }

    void check(){
        ResolvedShinryouMap shinryouMap = scope.resolvedMasterMap.shinryouMap;
        ResolvedDiseaseMap byoumeiMap = scope.resolvedMasterMap.diseaseMap;
        ResolvedDiseaseAdjMap adjMap = scope.resolvedMasterMap.diseaseAdjMap;
        scope.visits.forEach(visit -> {
            LocalDate visitDate = scope.getVisitDate(visit);
            visit.shinryouList.forEach(shinryou -> {
                int shinryoucode = shinryou.shinryou.shinryoucode;
                if( shinryoucode == shinryouMap.ＨｂＡ１ｃ ){
                    if( !scope.hasByoumeiAt(byoumeiMap.糖尿病, visitDate) ){
                        scope.error(
                                "「ＨｂＡ１ｃ」に対する病名がありません。",
                                "病名「糖尿病の疑い」を追加します。",
                                () -> scope.enterDisease(visit, byoumeiMap.糖尿病, adjMap.疑い)
                        );
                    }
                } else if( shinryoucode == shinryouMap.ＰＳＡ ){
                    if( !scope.hasByoumeiAt(byoumeiMap.前立腺癌, visitDate) ){
                        scope.error(
                                "「ＰＳＡ」に対する病名がありません。",
                                "病名「前立腺癌の疑い」を追加します。",
                                () -> scope.enterDisease(visit, byoumeiMap.前立腺癌, adjMap.疑い)
                        );
                    }
                }
            });
        });
    }

}
