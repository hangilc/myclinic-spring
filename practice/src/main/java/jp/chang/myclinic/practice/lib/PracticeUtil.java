package jp.chang.myclinic.practice.lib;

import javafx.scene.Node;
import javafx.scene.control.Label;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.time.LocalDate;
import java.util.List;

public class PracticeUtil {

    public static void storeKouhiList(VisitDTO visit, List<KouhiDTO> kouhiList){
        visit.kouhi1Id = 0;
        visit.kouhi2Id = 0;
        visit.kouhi3Id = 0;
        if( kouhiList.size() > 0 ){
            visit.kouhi1Id = kouhiList.get(0).kouhiId;
            if( kouhiList.size() > 1 ){
                visit.kouhi2Id = kouhiList.get(1).kouhiId;
                if( kouhiList.size() > 2 ){
                    visit.kouhi3Id = kouhiList.get(2).kouhiId;
                }
            }
        }
    }

    public static DrugCategory zaikeiToCategory(char zaikei){
        if( zaikei == MyclinicConsts.ZaikeiGaiyou ){
            return DrugCategory.Gaiyou;
        } else {
            return DrugCategory.Naifuku;
        }
    }

    public static int findCopyTarget(int srcVisitId){
        int targetVisitId = Context.currentPatientService.getCurrentOrTempVisitId();
        if( targetVisitId == 0 ){
            GuiUtil.alertError("コピー先を見つけられませんでした。");
        } else if( targetVisitId == srcVisitId ){
            targetVisitId = 0;
            GuiUtil.alertError("同じ診察にはコピーできません。");
        }
        return targetVisitId;
    }

    public static void addFormClass(Node node){
        node.getStyleClass().add("form");
    }

    public static Node createFormTitle(String label){
        Label title = new Label(label);
        title.getStyleClass().add("title");
        title.setMaxWidth(Double.MAX_VALUE);
        return title;
    }

    public static boolean confirmCurrentVisitAction(int visitId, String message) {
        if( visitId == Context.currentPatientService.getCurrentOrTempVisitId() ){
            return true;
        } else {
            return GuiUtil.confirm("現在診察中あるいは暫定診察でありませんが、" + message);
        }
    }

    public static String[] gazouLabelExamples = new String[]{ "胸部単純Ｘ線", "腹部単純Ｘ線" };

    public static DiseaseModifyEndReasonDTO createDiseaseModifyEndReason(DiseaseFullDTO disease, DiseaseEndReason reason, LocalDate endDate){
        DiseaseModifyEndReasonDTO modify = new DiseaseModifyEndReasonDTO();
        modify.diseaseId = disease.disease.diseaseId;
        if( diseaseHasSusp(disease.adjList) && reason == DiseaseEndReason.Cured ){
            reason = DiseaseEndReason.Stopped;
        }
        modify.endReason = reason.getCode();
        modify.endDate = endDate.toString();
        return modify;
    }

    public static boolean diseaseHasSusp(List<DiseaseAdjFullDTO> adjList){
        for(DiseaseAdjFullDTO adj: adjList){
            if( "の疑い".equals(adj.master.name) ){
                return true;
            }
        }
        return false;
    }
}
