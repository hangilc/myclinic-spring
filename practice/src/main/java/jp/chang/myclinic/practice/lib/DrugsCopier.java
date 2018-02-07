package jp.chang.myclinic.practice.lib;

import javafx.application.Platform;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;

import java.util.List;
import java.util.function.Consumer;

public class DrugsCopier {

    private int targetVisitId;
    private String targetVisitedAt;
    private Consumer<DrugFullDTO> drugEnteredCallback;
    private Runnable cb;

    public DrugsCopier(int targetVisitId, List<DrugFullDTO> drugs, Consumer<DrugFullDTO> drugEnteredCallback, Runnable cb){
        this.targetVisitId = targetVisitId;
        this.drugEnteredCallback = drugEnteredCallback;
        this.cb = cb;
        PracticeService.getVisit(targetVisitId)
                .thenAccept(visit -> {
                    targetVisitedAt = visit.visitedAt;
                    doCopy(drugs);
                });
    }

    private void doCopy(List<DrugFullDTO> drugs){
        if( drugs.size() == 0 ){
            Platform.runLater(cb);
        } else {
            DrugFullDTO srcDrug = drugs.get(0);
            PracticeService.resolveIyakuhinMaster(srcDrug, targetVisitedAt)
                    .thenCompose(master -> {
                        DrugDTO newDrug = DrugDTO.copy(srcDrug.drug);
                        newDrug.drugId = 0;
                        newDrug.visitId = targetVisitId;
                        newDrug.iyakuhincode = master.iyakuhincode;
                        newDrug.prescribed = 0;
                        return PracticeService.enterDrug(newDrug);
                    })
                    .thenCompose(PracticeService::getDrugFull)
                    .thenAccept(newDrugFull -> {
                        Platform.runLater(() -> drugEnteredCallback.accept(newDrugFull));
                        doCopy(drugs.subList(1, drugs.size()));
                    });
        }
    }
}
