package jp.chang.myclinic.practice.javafx.conduct;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EnterInjectionForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(EnterInjectionForm.class);

    private int conductDrugId = 0;
    private int conductId = 0;
    private DrugInput drugInput = new DrugInput();
    private RadioButtonGroup<ConductKind> kindGroup;

    public EnterInjectionForm() {
        super("処置注射入力");
        getChildren().addAll(
                drugInput,
                createKindInput(),
                createCommands()
        );
    }

    private Node createKindInput(){
        HBox hbox = new HBox(4);
        kindGroup = new RadioButtonGroup<>();
        kindGroup.createRadioButton("皮下・筋肉", ConductKind.HikaChuusha);
        kindGroup.createRadioButton("静脈 ", ConductKind.JoumyakuChuusha);
        kindGroup.createRadioButton("その他", ConductKind.OtherChuusha);
        hbox.getChildren().addAll(kindGroup.getButtons());
        return hbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnDragEntered(evt -> doEnter());
        cancelButton.setOnAction(evt -> onCancel(this));
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private void doEnter(){
        ConductDrugDTO drug = new ConductDrugDTO();
        drug.conductDrugId = conductDrugId;
        drug.conductId = conductId;
        List<String> errors = drugInput.stuffInto(drug);
        if( errors.size() > 0 ){
            GuiUtil.alertError(String.join("\n", errors));
        } else {
            onEnter(this, kindGroup.getValue(), drug);
        }
    }

    protected void onEnter(EnterInjectionForm form, ConductKind kind, ConductDrugDTO drug){

    }

    protected void onCancel(EnterInjectionForm form){

    }
}
