package jp.chang.myclinic.practice.javafx.drug2;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditForm extends DrugFormBase {

    private static Logger logger = LoggerFactory.getLogger(EditForm.class);
    private int drugId;

    public EditForm(DrugFullDTO drug, String drugTekiyou, VisitDTO visit) {
        super(visit, "処方の編集");
        this.drugId = drug.drug.drugId;
        DrugData data = DrugData.fromDrug(drug);
        getInput().setData(data);
    }

    @Override
    void doEnter() {
        DrugDTO drug = getInput().createDrug(drugId, getVisitId(), 0);
        Service.api.updateDrug(drug)
                .thenCompose(ok -> Service.api.getDrugFull(drugId))
                .thenAcceptAsync(this::onUpdated, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    protected void onUpdated(DrugFullDTO updated) {
    }

    protected void onClose() {
    }

    protected void onTekiyouModified(String newTekiyou) {
    }
}
