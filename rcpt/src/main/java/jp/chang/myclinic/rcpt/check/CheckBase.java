package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        System.out.println(msg);
    }

    void info(String msg){
        System.out.println(msg);
    }

    void forEachVisit(Consumer<VisitFull2DTO> cb){
        visits.forEach(cb);
    }

    int countShinryouMaster(VisitFull2DTO visit, ShinryouMasterDTO master){
        int shinryoucode = master.shinryoucode;
        return (int)visit.shinryouList.stream().filter(s -> s.master.shinryoucode == shinryoucode).count();
    }

    int countShoshinGroup(VisitFull2DTO visit){
        return countShinryouMaster(visit, getMasters().初診);
    }

    int countSaishinGroup(VisitFull2DTO visit){
        return countShinryouMaster(visit, getMasters().再診) +
                countShinryouMaster(visit, getMasters().同日再診);
    }

    List<DiseaseFullDTO> listDisease(VisitFull2DTO visit){
        String at = visit.visit.visitedAt.substring(0, 10);
        return diseases.stream().filter(d -> isValidAt(d, at)).collect(Collectors.toList());
    }

    private boolean isValidAt(DiseaseFullDTO disease, String at){
        String startDate = disease.disease.startDate;
        String endDate = disease.disease.endDate;
        return inTheInterval(startDate, endDate, at);
    }

    private boolean inTheInterval(String startDate, String endDate, String at){
        return startDate.compareTo(at) <= 0 &&
                ("0000-00-00".equals(endDate) || at.compareTo(endDate) <= 0);
    }

    boolean diseaseStartsAt(DiseaseFullDTO disease, VisitFull2DTO visit){
        String at = visit.visit.visitedAt.substring(0, 10);
        return disease.disease.startDate.equals(at);
    }

}
