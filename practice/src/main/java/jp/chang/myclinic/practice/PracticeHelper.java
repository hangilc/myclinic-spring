package jp.chang.myclinic.practice;

import javafx.application.Platform;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PracticeHelper {

    private static PracticeHelper INSTANCE = new PracticeHelper();

    public static PracticeHelper getInstance() {
        return INSTANCE;
    }

    public LocalDate getVisitedAt(String visitedAt) {
        if (visitedAt.length() == 10) {
            return LocalDate.parse(visitedAt);
        } else {
            return LocalDateTime.parse(visitedAt).toLocalDate();
        }
    }

    public CompletableFuture<Void> endPatient() {
        CurrentPatientService curr = Context.currentPatientService;
        Frontend frontend = Context.frontend;
        int currentVisitId = curr.getCurrentVisitId();
        return (currentVisitId == 0 ?
                CompletableFuture.completedFuture(null) :
                frontend.suspendExam(currentVisitId))
                .thenAcceptAsync(v -> {
                    curr.setCurrentPatient(null, 0);
                }, Platform::runLater);
    }

    public CompletableFuture<Void> startPatient(PatientDTO patient) {
        return endPatient()
                .thenAcceptAsync(ignore -> {
                    Context.currentPatientService.setCurrentPatient(patient, 0);
                }, Platform::runLater);
    }

    public static class BatchEnteredShinryou {
        public List<ShinryouFullDTO> shinryouList;
        public List<ConductFullDTO> conducts;
        public Map<Integer, ShinryouAttrDTO> attrMap;
    }

    public void batchEnterShinryouByNames(int visitId, List<String> names,
                                                 Consumer<BatchEnteredShinryou> cb) {
        class Local {
            private BatchEnterResultDTO enterResult;
            private List<ShinryouFullDTO> shinryouList;
            private List<ConductFullDTO> conducts;
        }
        Local local = new Local();
        BatchEnterByNamesRequestDTO req = new BatchEnterByNamesRequestDTO();
        req.shinryouNames = new ArrayList<>();
        req.conducts = new ArrayList<>();
        names.forEach(name -> {
            if( "骨塩定量".equals(name) ){
                addKotsuenTeiryou(req);
            } else {
                req.shinryouNames.add(name);
            }
        });
        Context.frontend.batchEnterByNames(visitId, req)
                .thenCompose(result -> {
                    local.enterResult = result;
                    return Context.frontend.listShinryouFullByIds(local.enterResult.shinryouIds);
                })
                .thenCompose(result -> {
                    local.shinryouList = result;
                    return Context.frontend.listConductFullByIds(local.enterResult.conductIds);
                })
                .thenCompose(result -> {
                    local.conducts = result;
                    List<Integer> shinryouIds = local.enterResult.shinryouIds;
                    return Context.frontend.batchGetShinryouAttr(shinryouIds);
                })
                .thenAccept(attrList -> {
                    Map<Integer, ShinryouAttrDTO> attrMap = new HashMap<>();
                    attrList.forEach(attr -> attrMap.put(attr.shinryouId, attr));
                    BatchEnteredShinryou arg = new BatchEnteredShinryou();
                    arg.shinryouList = local.shinryouList;
                    arg.conducts = local.conducts;
                    arg.attrMap = attrMap;
                    Platform.runLater(() -> cb.accept(arg));
                })
                .exceptionally(HandlerFX.exceptionally());
    }

    private static void addKotsuenTeiryou(BatchEnterByNamesRequestDTO req){
        EnterConductByNamesRequestDTO conductReq = new EnterConductByNamesRequestDTO();
        conductReq.kind = ConductKind.Gazou.getCode();
        conductReq.gazouLabel = "骨塩定量に使用";
        conductReq.shinryouNames = List.of("骨塩定量ＭＤ法");
        EnterConductKizaiByNamesRequestDTO kizai = new EnterConductKizaiByNamesRequestDTO();
        kizai.name = "四ツ切";
        kizai.amount = 1.0;
        conductReq.kizaiList = List.of(kizai);
        req.conducts.add(conductReq);
    }

}
