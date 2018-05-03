package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class CheckDiseaseExists extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckDiseaseExists.class);

    CheckDiseaseExists(List<VisitFull2DTO> visits, Masters masters, List<DiseaseFullDTO> diseases) {
        super(visits, masters, diseases);
    }

    void check(boolean fixit){
        if( getDiseases().size() == 0 ){
            error("病名なし");
        }
    }

}
