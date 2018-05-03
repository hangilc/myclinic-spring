package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;

import java.util.List;

class CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckBase.class);
    private List<VisitFull2DTO> visits;
    private Masters masters;
    private  List<DiseaseFullDTO> diseases;

    CheckBase(){

    }

    CheckBase(List<VisitFull2DTO> visits, Masters masters, List<DiseaseFullDTO> diseases){
        this.visits = visits;
        this.masters = masters;
        this.diseases = diseases;
    }

    Masters getMasters(){
        return masters;
    }

    List<DiseaseFullDTO> getDiseases(){
        return diseases;
    }

    void error(String msg){
        System.err.println(msg);
    }

    void info(String msg){
        System.err.println(msg);
    }

}
