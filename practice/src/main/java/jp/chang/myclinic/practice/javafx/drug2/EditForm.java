package jp.chang.myclinic.practice.javafx.drug2;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
    private Label tekiyouLabel = new Label();
    private HBox tekiyouRow;

    public EditForm(DrugFullDTO drug, String drugTekiyou, VisitDTO visit) {
        super(visit, "処方の編集");
        this.drugId = drug.drug.drugId;
        DrugData data = DrugData.fromDrug(drug);
        getInput().setData(data);
        tekiyouLabel.setText(drugTekiyou);
        this.tekiyouRow = getInput().addRowBeforeCategory(new Label("摘要："), tekiyouLabel);
        updateTekiyouVisibility();
        Hyperlink deleteLink = new Hyperlink("削除");
        deleteLink.setOnAction(evt -> doDelete());
        if (drugTekiyou == null || drugTekiyou.isEmpty()) {
            Hyperlink tekiyouLink = new Hyperlink("摘要入力");
            tekiyouLink.setOnAction(evt -> doEnterTekiyou());
            addToCommandBox(tekiyouLink);
        } else {
            Hyperlink editTekiyouLink = new Hyperlink("摘要編集");
            Hyperlink deleteTekiyouLink = new Hyperlink("摘要削除");
            editTekiyouLink.setOnAction(evt -> doEditTekiyou());
            deleteTekiyouLink.setOnAction(evt -> doDeleteTekiyou());
            addToCommandBox(editTekiyouLink);
            addToCommandBox(deleteTekiyouLink);
        }
        addToCommandBox(deleteLink);
        getInput().addRow(allFixedCheck);
    }

    private boolean hasTekiyou() {
        String tekiyou = getTekiyou();
        return tekiyou != null && !tekiyou.isEmpty();
    }

    private void updateTekiyouVisibility() {
        boolean visible = hasTekiyou();
        tekiyouRow.setManaged(visible);
        tekiyouRow.setVisible(visible);
    }

    @Override
    void doEnter() {
        DrugDTO drug = getInput().createDrug(drugId, getVisitId(), 0);
        Service.api.updateDrug(drug)
                .thenCompose(ok -> Service.api.getDrugFull(drugId))
                .thenAcceptAsync(this::onUpdated, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doDelete() {
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

    private String getTekiyou() {
        return tekiyouLabel.getText();
    }

    private void setTekiyou(String text) {
        tekiyouLabel.setText(text);
    }

    private void doEnterTekiyou() {
        String curr = getTekiyou();
        if (curr == null) {
            curr = "";
        }
        GuiUtil.askForString("摘要の内容", curr).ifPresent(str -> {
            Service.api.setDrugTekiyou(drugId, str)
                    .thenAccept(ok -> {
                        Platform.runLater(() -> {
                            setTekiyou(str);
                            updateTekiyouVisibility();
                            onTekiyouModified(str);
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        });
    }

    private void doEditTekiyou() {

    }

    private void doDeleteTekiyou() {

    }

    @Override
    Set<Input.SetOption> getSetOptions() {
        Set<Input.SetOption> opts = new HashSet<>();
        if (allFixedCheck.isSelected()) {
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
