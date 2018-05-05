package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class CheckByoumei extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckByoumei.class);

    CheckByoumei(Scope scope) {
        super(scope);
    }

    void check(boolean fixit){
        Map<Integer, List<ResolvedShinryouByoumei>> shinryouByoumeiMap = getShinryouByoumeiMap();
        Set<Integer> targetShinryoucodes = shinryouByoumeiMap.keySet();
        forEachVisit(visit -> {
            forEachShinryou(visit, shinryou -> {
                int shinryoucode = shinryou.master.shinryoucode;
                if( targetShinryoucodes.contains(shinryoucode) ){
                    Set<Integer> dcodes = shinryouByoumeiMap.get(shinryoucode).stream()
                            .map(sb -> sb.byoumei.code).collect(Collectors.toSet());
                    int n = countDisease(d -> dcodes.contains(d.disease.shoubyoumeicode));
                    if( n == 0 ){
                        String msg = String.format("「%s」に対する病名がありません。", shinryou.master.name);
                        error(msg, fixit, () -> {
                            DiseaseNewDTO dto = createNewDisease(visit, shinryouByoumeiMap.get(shinryoucode).get(0));
                            enterDisease(dto);
                        });
                    }
                }
            });
        });
    }

}
