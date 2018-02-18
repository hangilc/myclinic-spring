package jp.chang.myclinic.practice.javafx.conduct;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.practice.lib.conduct.ConductKizaiInputInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KizaiInput extends DispGrid implements ConductKizaiInputInterface {

    private static Logger logger = LoggerFactory.getLogger(KizaiInput.class);

    private int kizaicode = 0;
    private Text nameText = new Text("");
    private TextField amountField = new TextField("1");
    private Text amountUnitText = new Text("");

    public KizaiInput() {
        addRow("名称：", new TextFlow(nameText));
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            amountField.getStyleClass().add("kizai-amount-input");
            hbox.getChildren().addAll(amountField, amountUnitText);
            addRow("用量：", hbox);
        }
    }

    @Override
    public void setKizaicode(int kizaicode) {
        this.kizaicode = kizaicode;
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
    public int getKizaicode() {
        return kizaicode;
    }

    @Override
    public String getAmount() {
        return amountField.getText();
    }
}
