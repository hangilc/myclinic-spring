package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.consts.Shuushokugo;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class CheckByoumei extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckByoumei.class);

    CheckByoumei(Scope scope) {
        super(scope);
    }

    void check(){
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
                        ResolvedShinryouByoumei rsb = shinryouByoumeiMap.get(shinryoucode).get(0);
                        String msg = String.format("「%s」に対する病名がありません。", shinryou.master.name);
                        String fixMsg = String.format("病名「%s」を追加します。", fullDiseaseName(rsb));
                        error(msg, fixMsg, () -> {
                            DiseaseNewDTO dto = createNewDisease(visit, rsb);
                            enterDisease(dto);
                        });
                    }
                }
            });
        });
    }

    private String fullDiseaseName(ResolvedShinryouByoumei rsb){
        List<String> pre = new ArrayList<>();
        List<String> post = new ArrayList<>();
        rsb.shuushokugoList.forEach(s -> {
            if( s.code < Shuushokugo.PostFixStart ){
                pre.add(s.name);
            } else {
                post.add(s.name);
            }
        });
        return String.join("", pre) + rsb.byoumei.name + String.join("", post);
    }

}