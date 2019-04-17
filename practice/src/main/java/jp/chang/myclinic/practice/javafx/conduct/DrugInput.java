package jp.chang.myclinic.practice.javafx.conduct;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.util.validator.dto.ConductDrugValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrugInput extends GridPane {

    private int iyakuhincode;
    private Text nameText;
    private TextField amountField;
    private Text amountUnitText;

    public DrugInput() {
        int row = 0;
        {
            nameText = new Text("");
            TextFlow textFlow = new TextFlow();
            textFlow.getChildren().add(nameText);
            addRow(row, new Label("名称："), textFlow);
            row += 1;
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            amountField = new TextField("1");
            amountField.getStyleClass().add("drug-amount-input");
            amountUnitText = new Text("");
            hbox.getChildren().addAll(amountField, amountUnitText);
            addRow(row, new Label("用量："), hbox);
        }
    }

    void setMaster(IyakuhinMasterDTO master){
        setIyakuhincode(master.iyakuhincode);
        setName(master.name);
        setAmountUnit(master.unit);
    }

    private void setIyakuhincode(int iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    private void setName(String name) {
        nameText.setText(name);
    }

    private void setAmount(String amount) {
        amountField.setText(amount);
    }

    private void setAmountUnit(String unit) {
        amountUnitText.setText(unit);
    }

    private int getIyakuhincode() {
        return iyakuhincode;
    }

    private String getAmount() {
        return amountField.getText();
    }

    Validated<ConductDrugDTO> getValidateConductDrugToEnter(){
        return new ConductDrugValidator()
                .setValidatedConductDrugId(0)
                .setValidatedConductId(0)
                .validateIyakuhincode(getIyakuhincode())
                .validateAmount(getAmount())
                .validate();
    }

}
