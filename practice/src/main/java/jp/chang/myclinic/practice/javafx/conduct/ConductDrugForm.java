package jp.chang.myclinic.practice.javafx.conduct;

import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.EnterCancelBox;
import jp.chang.myclinic.practice.javafx.parts.SearchBoxOld;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ConductDrugForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(ConductDrugForm.class);
    private LocalDate at;
    private int conductId;

    public ConductDrugForm(LocalDate at, int conductId) {
        super("薬剤追加");
        this.at = at;
        this.conductId = conductId;
        DrugInput drugInput = new DrugInput();
        EnterCancelBox commands = new EnterCancelBox();
        commands.setEnterCallback(() -> doEnter(drugInput));
        commands.setCancelCallback(this::onCancel);
        SearchBoxOld<IyakuhinMasterDTO> searchBox = new SearchBoxOld<>(
                t -> Context.frontend.searchIyakuhinMaster(t, at),
                m -> m.name
        );
        searchBox.setOnSelectCallback(drugInput::setMaster);
        getChildren().addAll(
                drugInput,
                commands,
                searchBox
        );
    }

    private void doEnter(DrugInput input){
        ConductDrugDTO drug = new ConductDrugDTO();
        drug.conductId = conductId;
        //input.stuffInto(drug, this::onEnter, HandlerFX::alert);
    }

    protected void onEnter(ConductDrugDTO drug){

    }

    protected void onCancel(){

    }

}
