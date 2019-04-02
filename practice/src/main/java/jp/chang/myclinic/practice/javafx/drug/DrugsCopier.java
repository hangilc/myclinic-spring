package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.lib.PracticeService;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class DrugsCopier {

    private int targetVisitId;
    private String targetVisitedAt;
    private BiConsumer<DrugFullDTO, DrugAttrDTO> drugEnteredCallback;
    private Runnable cb;

    public DrugsCopier(int targetVisitId, List<DrugFullDTO> drugs,
                       BiConsumer<DrugFullDTO, DrugAttrDTO> drugEnteredCallback, Runnable cb){
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
            class Local {
                private int enteredDrugId;
                private DrugAttrDTO dstAttr;
            }
            Local local = new Local();
            PracticeService.resolveIyakuhinMaster(srcDrug, targetVisitedAt)
                    .thenCompose(master -> {
                        DrugDTO newDrug = DrugDTO.copy(srcDrug.drug);
                        newDrug.drugId = 0;
                        newDrug.visitId = targetVisitId;
                        newDrug.iyakuhincode = master.iyakuhincode;
                        newDrug.prescribed = 0;
                        return PracticeService.enterDrug(newDrug);
                    })
                    .thenCompose(enteredDrugId -> {
                        local.enteredDrugId = enteredDrugId;
                        return Context.getInstance().getFrontend().findDrugAttr(srcDrug.drug.drugId);
                    })
                    .thenCompose(srcAttr -> {
                        if( srcAttr != null ) {
                            DrugAttrDTO dstAttr = DrugAttrDTO.copy(srcAttr);
                            dstAttr.drugId = local.enteredDrugId;
                            local.dstAttr = dstAttr;
                            return Context.getInstance().getFrontend().enterDrugAttr(dstAttr);
                        } else {
                            return CompletableFuture.completedFuture(true);
                        }
                    })
                    .thenCompose(ok -> PracticeService.getDrugFull(local.enteredDrugId))
                    .thenAccept(newDrugFull -> {
                        Platform.runLater(() -> drugEnteredCallback.accept(newDrugFull, local.dstAttr));
                        doCopy(drugs.subList(1, drugs.size()));
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }
}
