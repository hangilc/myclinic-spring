package jp.chang.myclinic.practice.javafx.charge;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ChargeDTO;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.practice.javafx.parts.EnterCancelBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChargeForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(ChargeForm.class);
    private TextField chargeInputField;

    public ChargeForm(MeisaiDTO meisai, ChargeDTO charge) {
        super("請求額の変更");
        EnterCancelBox commands = new EnterCancelBox();
        commands.setEnterCallback(this::doEnter);
        commands.setCancelCallback(this::onCancel);
        getChildren().addAll(
                createInput(meisai, charge),
                commands
        );
    }

    private void doEnter(){
        try {
            int chargeValue = Integer.parseInt(chargeInputField.getText());
            onEnter(chargeValue);
        } catch(NumberFormatException ex){
            GuiUtil.alertError("請求額の入力が不適切です。");
        }
    }

    private Node createInput(MeisaiDTO meisai, ChargeDTO chargeDTO) {
        VBox vbox = new VBox(4);
        vbox.getChildren().addAll(
                new Label(String.format("診療報酬総点：%,d 点", meisai.totalTen)),
                new Label(String.format("負担割：%d 割", meisai.futanWari)),
                new Label(String.format("現在の請求額：%,d 円", chargeDTO.charge)),
                createInputField(meisai.charge)
        );
        return vbox;
    }

    private Node createInputField(int chargeValue) {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        TextField textField = new TextField("" + chargeValue);
        textField.getStyleClass().add("charge-input-field");
        hbox.getChildren().addAll(
                new Label("変更後の請求額："),
                textField,
                new Label("円")
        );
        chargeInputField = textField;
        return hbox;
    }

    protected void onEnter(int chargeValue){

    }

    protected void onCancel() {

    }

}
