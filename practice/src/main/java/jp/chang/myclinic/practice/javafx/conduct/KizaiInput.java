package jp.chang.myclinic.practice.javafx.conduct;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductKizaiDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.util.validator.dto.ConductKizaiValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class KizaiInput extends DispGrid {

    private static Logger logger = LoggerFactory.getLogger(KizaiInput.class);

    private int kizaicode = 0;
    private Text nameText = new Text("");
    private TextField amountField = new TextField("1");
    private Text amountUnitText = new Text("");

    KizaiInput() {
        addRow("名称：", new TextFlow(nameText));
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            amountField.getStyleClass().add("kizai-amount-input");
            hbox.getChildren().addAll(amountField, amountUnitText);
            addRow("用量：", hbox);
        }
    }

    void setMaster(KizaiMasterDTO master) {
        setKizaicode(master.kizaicode);
        setName(master.name);
        setAmountUnit(master.unit);
    }

    private void setKizaicode(int kizaicode) {
        this.kizaicode = kizaicode;
    }

    private void setName(String name) {
        nameText.setText(name);
    }

    private void setAmountUnit(String unit) {
        amountUnitText.setText(unit);
    }

    private int getKizaicode() {
        return kizaicode;
    }

    private String getAmount() {
        return amountField.getText();
    }

    Validated<ConductKizaiDTO> getValidatedToEnter(int conductId){
        return new ConductKizaiValidator()
                .setValidatedConductKizaiId(0)
                .validateConductId(conductId)
                .validateKizaicode(getKizaicode())
                .validateAmount(getAmount())
                .validate();
    }

}
