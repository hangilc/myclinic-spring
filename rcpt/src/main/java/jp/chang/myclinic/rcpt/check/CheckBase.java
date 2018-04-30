package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckBase.class);
    private VisitFull2DTO visit;
    private List<DiseaseFullDTO> diseases;
    private boolean fixit;

    CheckBase(VisitFull2DTO visit, List<DiseaseFullDTO> diseases, boolean fixit) {
        this.visit = visit;
        this.diseases = diseases;
        this.fixit = fixit;
    }

    void error(String str){
        System.err.println(str);
    }

    void info(String str){
        System.err.println(str);
    }

    boolean isFixit(){
        return fixit;
    }

    List<ShinryouFullDTO> getShinryouList(){
        return visit.shinryouList;
    }

    VisitDTO getVisit(){
        return visit.visit;
    }

    int countShinryouByName(String name){
        int n = 0;
        for(ShinryouFullDTO shinryou: visit.shinryouList){
            if( shinryou.master.name.equals(name) ){
                n += 1;
            }
        }
        return n;
    }

    int countShinryou(){
        return visit.shinryouList.size();
    }

    boolean shinryouExists(Pattern namePattern){
        for(ShinryouFullDTO shinryou: visit.shinryouList){
            Matcher m = namePattern.matcher(shinryou.master.name);
            if( m.matches() ){
                return true;
            }
        }
        return false;
    }

    int countDrugs(){
        return visit.drugs.size();
    }

}
