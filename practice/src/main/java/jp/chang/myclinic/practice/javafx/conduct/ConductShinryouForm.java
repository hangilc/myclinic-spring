package jp.chang.myclinic.practice.javafx.conduct;

import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.EnterCancelBox;
import jp.chang.myclinic.practice.javafx.parts.ShinryouSearchBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

public class ConductShinryouForm extends WorkForm {

    private int conductId;

    public ConductShinryouForm(String at, int conductId){
        super("診療行為追加");
        this.conductId = conductId;
        ShinryouInput shinryouInput = new ShinryouInput();
        ShinryouSearchBox searchBox = new ShinryouSearchBox(at);
        searchBox.setOnSelectCallback(shinryouInput::setMaster);
        EnterCancelBox command = new EnterCancelBox(() -> doEnter(shinryouInput), this::onCancel);
        getChildren().addAll(
                shinryouInput,
                command,
                searchBox
        );
    }

    private void doEnter(ShinryouInput input){
        ConductShinryouDTO shinryou = new ConductShinryouDTO();
        shinryou.conductId = conductId;
        input.stuffInto(shinryou, this::onEnter, HandlerFX::alert);
    }

    protected void onEnter(ConductShinryouDTO shinryou){

    }

    protected void onCancel(){

    }
}
