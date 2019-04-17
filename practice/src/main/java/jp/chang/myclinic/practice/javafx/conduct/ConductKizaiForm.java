package jp.chang.myclinic.practice.javafx.conduct;

import jp.chang.myclinic.dto.ConductKizaiDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.EnterCancelBox;
import jp.chang.myclinic.practice.javafx.parts.SearchBoxOld;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ConductKizaiForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(ConductKizaiForm.class);
    private LocalDate at;
    private int conductId;

    public ConductKizaiForm(LocalDate at, int conductId) {
        super("器材入力");
        this.at = at;
        this.conductId = conductId;
        KizaiInput kizaiInput = new KizaiInput();
        SearchBoxOld<KizaiMasterDTO> searchBox = new SearchBoxOld<>(
                t -> Context.frontend.searchKizaiMaster(t, at),
                m -> m.name
        );
        searchBox.setOnSelectCallback(kizaiInput::setMaster);
        EnterCancelBox commands = new EnterCancelBox();
        commands.setEnterCallback(() -> doEnter(kizaiInput));
        commands.setCancelCallback(this::onCancel);
        getChildren().addAll(
                kizaiInput,
                commands,
                searchBox
        );
    }

    private void doEnter(KizaiInput input){
        ConductKizaiDTO kizai = new ConductKizaiDTO();
        kizai.conductId = conductId;
        input.stuffInto(kizai, this::onEnter, HandlerFX::alert);
    }

    protected void onEnter(ConductKizaiDTO kizai){

    }

    protected void onCancel(){

    }

}
