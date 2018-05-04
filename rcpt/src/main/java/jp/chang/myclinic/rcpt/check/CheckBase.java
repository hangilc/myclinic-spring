package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.Madoku;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.mastermap.ResolvedMap;
import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseAdjMap;
import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseMap;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckBase.class);
    private List<VisitFull2DTO> visits;
    private ResolvedMap resolvedMasterMap;
    private  List<DiseaseFullDTO> diseases;

    CheckBase(Scope scope){
        this.visits = scope.visits;
        this.resolvedMasterMap = scope.resolvedMasterMap;
        this.diseases =scope. diseases;
    }

    List<DiseaseFullDTO> getDiseases(){
        return diseases;
    }

    void error(String msg){
        System.out.println(msg);
    }

    void error(String msg, boolean fixit, Runnable fixer){
        System.out.println(msg);
        if( fixit ){
            fixer.run();
            System.out.println("FIXED");
        }
    }

    void info(String msg){
        System.out.println(msg);
    }

    void forEachVisit(Consumer<VisitFull2DTO> cb){
        visits.forEach(cb);
    }

    ResolvedShinryouMap getShinryouMaster(){
        return resolvedMasterMap.shinryouMap;
    }

    ResolvedDiseaseMap getDiseaseMaster(){
        return resolvedMasterMap.diseaseMap;
    }

    ResolvedDiseaseAdjMap getDiseaseAdjMap(){
        return resolvedMasterMap.diseaseAdjMap;
    }

    VisitFull2DTO findVisit(Predicate<VisitFull2DTO> pred){
        return visits.stream().filter(pred).findFirst().orElseThrow(() -> {
            throw new RuntimeException("Cannot find visit");
        });
    }

    int countShinryou(VisitFull2DTO visit, Predicate<ShinryouFullDTO> pred){
        return (int)visit.shinryouList.stream().filter(pred).count();
    }

    int countShinryouInVisits(Predicate<ShinryouFullDTO> pred){
        return visits.stream().mapToInt(visit -> countShinryou(visit, pred)).sum();
    }

    int countShinryouMasterInVisits(int shinryoucode){
        Predicate<ShinryouFullDTO> pred = s -> s.master.shinryoucode == shinryoucode;
        return countShinryouInVisits(pred);
    }

    int countShohousenGroupInVisits(){
        return countShinryouInVisits(s -> {
            int shinryoucode = s.master.shinryoucode;
            return shinryoucode == getShinryouMaster().処方せん料 ||
                    shinryoucode == getShinryouMaster().処方せん料７;
        });
    }

    void enterShinryou(VisitFull2DTO visit, int shinryoucode){
        ShinryouDTO shinryou = new ShinryouDTO();
        shinryou.visitId = visit.visit.visitId;
        shinryou.shinryoucode = shinryoucode;
        try {
            int shinryouId = Service.api.enterShinryouCall(shinryou).execute().body();
            assert shinryouId > 0;
        } catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    int countShinryouMaster(VisitFull2DTO visit, int shinryoucode){
        return (int)visit.shinryouList.stream().filter(s -> s.master.shinryoucode == shinryoucode).count();
    }

    int countShoshinGroup(VisitFull2DTO visit){
        return countShinryouMaster(visit, getShinryouMaster().初診);
    }

    int countSaishinGroup(VisitFull2DTO visit){
        return countShinryouMaster(visit, getShinryouMaster().再診) +
                countShinryouMaster(visit, getShinryouMaster().同日再診);
    }

    List<ShinryouFullDTO> filterShinryou(VisitFull2DTO visit, Predicate<ShinryouFullDTO> pred){
        return visit.shinryouList.stream().filter(pred).collect(Collectors.toList());
    }

    void removeExtraShinryouMasterInVisits(int shinryoucode, int toBeRemained){
        List<Integer> shinryouIds = visits.stream().flatMap(visit -> visit.shinryouList.stream())
                .filter(s -> s.master.shinryoucode == shinryoucode)
                .skip(toBeRemained)
                .map(s -> s.shinryou.shinryouId)
                .collect(Collectors.toList());
        try {
            boolean success = Service.api.batchDeleteShinryouCall(shinryouIds).execute().body();
            assert success;
        } catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    void removeExtraShinryou(VisitFull2DTO visit, Predicate<ShinryouFullDTO> pred, int toBeRemained){
        List<Integer> shinryouIds = visit.shinryouList.stream().filter(pred)
                .map(s -> s.shinryou.shinryouId)
                .skip(toBeRemained)
                .collect(Collectors.toList());
        try {
            boolean success = Service.api.batchDeleteShinryouCall(shinryouIds).execute().body();
            assert success;
        } catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    void removeExtraShinryouMaster(VisitFull2DTO visit, int shinryoucode, int toBeRemained){
        removeExtraShinryou(visit, s -> s.master.shinryoucode == shinryoucode, toBeRemained);
    }

    void enterShohouryou(VisitFull2DTO visit){
        enterShinryou(visit, getShinryouMaster().処方料);
    }

    void enterShohouryou7(VisitFull2DTO visit){
        enterShinryou(visit, getShinryouMaster().処方料７);
    }

    void removeExtraShohouryou(VisitFull2DTO visit, int remain){
        removeExtraShinryouMaster(visit, getShinryouMaster().処方料, remain);
    }

    void removeExtraShohouryou7(VisitFull2DTO visit, int remain){
        removeExtraShinryouMaster(visit, getShinryouMaster().処方料７, remain);
    }

    void forEachShinryouInVisits(Consumer<ShinryouFullDTO> cb){
        visits.stream().flatMap(visit -> visit.shinryouList.stream()).forEach(cb);
    }

    int countDrugInVisits(Predicate<DrugFullDTO> pred){
        return visits.stream().mapToInt(visit -> countDrug(visit, pred)).sum();
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

    List<DrugFullDTO> filterDrug(VisitFull2DTO visit, Predicate<DrugFullDTO> pred){
        return visit.drugs.stream().filter(pred).collect(Collectors.toList());
    }

    boolean isMadoku(DrugFullDTO drug){
        return Madoku.fromCode(drug.master.madoku) != Madoku.NoMadoku;
    }

    DrugCategory drugCategoryOf(DrugFullDTO drug){
        return DrugCategory.fromCode(drug.drug.category);
    }

    boolean isNaifuku(DrugFullDTO drug){
        return drugCategoryOf(drug) == DrugCategory.Naifuku;
    }

    boolean isGaiyou(DrugFullDTO drug){
        return drugCategoryOf(drug) == DrugCategory.Gaiyou;
    }

    int countDrug(VisitFull2DTO visit, Predicate<DrugFullDTO> pred){
        return (int)visit.drugs.stream().filter(pred).count();
    }

    int countShohouryou(VisitFull2DTO visit){
        return countShinryouMaster(visit, getShinryouMaster().処方料);
    }

    int countShohouryou7(VisitFull2DTO visit){
        return countShinryouMaster(visit, getShinryouMaster().処方料７);
    }

    boolean isChoukiNaifukuDrug(DrugFullDTO drug){
        return isNaifuku(drug) && drug.drug.days > 14;
    }

    int countChoukiNaifukuDrug(VisitFull2DTO visit){
        return countDrug(visit, this::isChoukiNaifukuDrug);
    }

}
