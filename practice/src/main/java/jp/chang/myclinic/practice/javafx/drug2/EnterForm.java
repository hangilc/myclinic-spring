package jp.chang.myclinic.practice.javafx.drug2;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.events.DrugEnteredEvent;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.HashSet;
import java.util.Set;

public class EnterForm extends DrugFormBase {

    //private static Logger logger = LoggerFactory.getLogger(EnterForm.class);

    private CheckBox daysFixedCheck = new CheckBox("固定");

    public EnterForm(VisitDTO visit) {
        super(visit, "新規処方の入力");
        daysFixedCheck.setSelected(true);
        getInput().addToDaysRow(daysFixedCheck);
    }

    @Override
    Set<Input.SetOption> getSetOptions() {
        Set<Input.SetOption> opts = new HashSet<Input.SetOption>();
        if (daysFixedCheck.isSelected()) {
            opts.add(Input.SetOption.FixedDays);
        }
        return opts;
    }

    @Override
    void doEnter() {
        DrugDTO drug = getInput().createDrug(0, getVisitId(), 0);
        Service.api.enterDrug(drug)
                .thenCompose(Service.api::getDrugFull)
                .thenAccept(enteredDrug -> Platform.runLater(() -> {
                    EnterForm.this.fireEvent(new DrugEnteredEvent(enteredDrug, null));
                    doClearInput();
                    getSearchInput().clear();
                    getSearchResult().clear();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

}
