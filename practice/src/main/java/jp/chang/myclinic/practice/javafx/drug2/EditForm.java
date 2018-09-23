package jp.chang.myclinic.practice.javafx.drug2;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.events.DrugDeletedEvent;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class EditForm extends DrugFormBase {

    private static Logger logger = LoggerFactory.getLogger(EditForm.class);
    private int drugId;
    private CheckBox allFixedCheck = new CheckBox("用量・用法・日数をそのままに");

    public EditForm(DrugFullDTO drug, String drugTekiyou, VisitDTO visit) {
        super(visit, "処方の編集");
        this.drugId = drug.drug.drugId;
        DrugData data = DrugData.fromDrug(drug);
        getInput().setData(data);
        Hyperlink deleteLink = new Hyperlink("削除");
        deleteLink.setOnAction(evt -> doDelete());
        addToCommandBox(deleteLink);
        getInput().addRow(allFixedCheck);
    }

    @Override
    void doEnter() {
        DrugDTO drug = getInput().createDrug(drugId, getVisitId(), 0);
        Service.api.updateDrug(drug)
                .thenCompose(ok -> Service.api.getDrugFull(drugId))
                .thenAcceptAsync(this::onUpdated, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doDelete(){
        if (GuiUtil.confirm("この処方を削除していいですか？")) {
            class Local {
                private DrugDTO drug;
            }
            Local local = new Local();
            Service.api.getDrug(drugId)
                    .thenCompose(drugDTO -> {
                        local.drug = drugDTO;
                        return Service.api.deleteDrug(drugId);
                    })
                    .thenAccept(ok -> {
                        DrugDeletedEvent event = new DrugDeletedEvent(local.drug);
                        Platform.runLater(() -> EditForm.this.fireEvent(event));
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    @Override
    Set<Input.SetOption> getSetOptions() {
        Set<Input.SetOption> opts = new HashSet<>();
        if( allFixedCheck.isSelected() ){
            opts.add(Input.SetOption.MasterOnly);
        }
        return opts;
    }

    protected void onUpdated(DrugFullDTO updated) {
    }

    protected void onClose() {
    }

    protected void onTekiyouModified(String newTekiyou) {
    }
}
