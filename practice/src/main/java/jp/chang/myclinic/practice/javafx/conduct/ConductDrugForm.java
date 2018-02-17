package jp.chang.myclinic.practice.javafx.conduct;

import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.parts.EnterCancelBox;
import jp.chang.myclinic.practice.javafx.parts.SearchBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConductDrugForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(ConductDrugForm.class);
    private String at;
    private int conductId;

    public ConductDrugForm(String at, int conductId) {
        super("薬剤追加");
        this.at = at;
        this.conductId = conductId;
        DrugInput drugInput = new DrugInput();
        EnterCancelBox commands = new EnterCancelBox();
        commands.setEnterCallback(() -> doEnter(drugInput));
        commands.setCancelCallback(this::onCancel);
        SearchBox<IyakuhinMasterDTO> searchBox = new SearchBox<>(
                t -> Service.api.searchIyakuhinMaster(t, at),
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
        List<String> errs = input.stuffInto(drug);
        if( errs.size() > 0 ){
            GuiUtil.alertError(String.join("\n", errs));
        } else {
            onEnter(drug);
        }
    }

    protected void onEnter(ConductDrugDTO drug){

    }

    protected void onCancel(){

    }

}
