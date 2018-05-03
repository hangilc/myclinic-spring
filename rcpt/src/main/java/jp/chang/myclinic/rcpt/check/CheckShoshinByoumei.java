package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

class CheckShoshinByoumei extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckShoshinByoumei.class);

    CheckShoshinByoumei(List<VisitFull2DTO> visits, Masters masters, List<DiseaseFullDTO> diseases) {
        super(visits, masters, diseases);
    }

    void check(boolean fixit){
        forEachVisit(visit -> {
            int nShoshin = countShoshinGroup(visit);
            int nSaishin = countSaishinGroup(visit);
            if( nShoshin > 0 && nSaishin == 0 ){
                List<DiseaseFullDTO> ds = listDiseases(visit);
                List<DiseaseFullDTO> starters = ds.stream()
                        .filter(d -> diseaseStartsAt(d, visit)).collect(Collectors.toList());
                if( starters.size() == 0 ){
                    error("初診時病名なし");
                }
                if( ds.size() > starters.size() ){
                    error("初診時に継続病名あり");
                }
            }
        });
    }

}
