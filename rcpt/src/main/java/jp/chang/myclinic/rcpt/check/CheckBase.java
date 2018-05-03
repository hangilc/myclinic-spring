package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;

import java.util.List;
import java.util.function.Consumer;

class CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckBase.class);
    private List<VisitFull2DTO> visits;
    private Masters masters;
    private  List<DiseaseFullDTO> diseases;

    CheckBase(){
        this(null, null, null);
    }

    CheckBase(List<VisitFull2DTO> visits, Masters masters){
        this(visits, masters, null);
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

    void forEachVisit(Consumer<VisitFull2DTO> cb){
        visits.forEach(cb);
    }

    int countShinryouMaster(VisitFull2DTO visit, ShinryouMasterDTO master){
        int shinryoucode = master.shinryoucode;
        return (int)visit.shinryouList.stream().filter(s -> s.master.shinryoucode == shinryoucode).count();
    }
}
