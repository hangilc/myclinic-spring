package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.Madoku;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.rcpt.Masters;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
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

    void forEachDrug(VisitFull2DTO visit, Consumer<DrugFullDTO> cb){
        visit.drugs.forEach(cb);
    }

    List<DrugFullDTO> filterDrug(VisitFull2DTO visit, Predicate<DrugFullDTO> pred){
        return visit.drugs.stream().filter(pred).collect(Collectors.toList());
    }

    boolean isMadoku(DrugFullDTO drug){
        return Madoku.fromCode(drug.master.madoku) != Madoku.NoMadoku;
    }

    List<ShinryouFullDTO> filterShinryou(VisitFull2DTO visit, Predicate<ShinryouFullDTO> pred){
        return visit.shinryouList.stream().filter(pred).collect(Collectors.toList());
    }

    void removeExtraShinryou(VisitFull2DTO visit, ShinryouMasterDTO master, int toBeRemained){
        List<ShinryouFullDTO> targets = filterShinryou(visit,
                s -> s.master.shinryoucode == master.shinryoucode);
        List<Integer> shinryouIds = targets.subList(toBeRemained, targets.size())
                .stream().map(s -> s.shinryou.shinryouId).collect(Collectors.toList());
        try {
            boolean success = Service.api.batchDeleteShinryouCall(shinryouIds).execute().body();
            assert success;
        } catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    void enterShinryou(VisitFull2DTO visit, ShinryouMasterDTO master){
        ShinryouDTO shinryou = new ShinryouDTO();
        shinryou.visitId = visit.visit.visitId;
        shinryou.shinryoucode = master.shinryoucode;
        try {
            int shinryouId = Service.api.enterShinryouCall(shinryou).execute().body();
            assert shinryouId > 0;
        } catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    DrugCategory drugCategoryOf(DrugFullDTO drug){
        return DrugCategory.fromCode(drug.drug.category);
    }

    boolean isGaiyou(DrugFullDTO drug){
        return drugCategoryOf(drug) == DrugCategory.Gaiyou;
    }

}
