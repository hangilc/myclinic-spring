package jp.chang.myclinic.practice.javafx.conduct;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.practice.lib.conduct.ConductDrugForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrugInput extends GridPane implements ConductDrugForm {

    private static Logger logger = LoggerFactory.getLogger(DrugInput.class);

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
            amountField = new TextField();
            amountField.getStyleClass().add("drug-amount-input");
            amountUnitText = new Text("");
            hbox.getChildren().addAll(amountField, amountUnitText);
            addRow(row, new Label("用量："), hbox);
        }
    }

    @Override
    public void setIyakuhincode(int iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    @Override
    public void setName(String name) {
        nameText.setText(name);
    }

    @Override
    public void setAmount(String amount) {
        amountField.setText(amount);
    }

    @Override
    public void setAmountUnit(String unit) {
        amountUnitText.setText(unit);
    }

    @Override
    public int getIyakuhincode() {
        return iyakuhincode;
    }

    @Override
    public String getAmount() {
        return amountField.getText();
    }

}
