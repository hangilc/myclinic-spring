package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.util.validator.Validators;
import jp.chang.myclinic.util.validator.dto.ShinryouValidator;

public class ShinryouInput extends GridPane {

    private int shinryoucode = 0;
    private Text nameText = new Text();
    private Text tekiyouText = null;

    public ShinryouInput() {
        setHgap(4);
        Label label = new Label("名称：");
        label.setMinWidth(USE_PREF_SIZE);
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().add(nameText);
        addRow(0, label, textFlow);
    }

    public void setMaster(ShinryouMasterDTO master) {
        this.shinryoucode = master.shinryoucode;
        this.nameText.setText(master.name);
    }

    private void updateAttr() {
        getChildren().removeIf(node -> GridPane.getRowIndex(node) >= 1);
        if (tekiyouText != null) {
            addRow(1, new Label("摘要："), tekiyouText);
        }
    }

    public void setTekiyou(String tekiyou) {
        if (tekiyouText == null) {
            tekiyouText = new Text();
        }
        tekiyouText.setText(tekiyou);
        updateAttr();
    }

    public void deleteTekiyou() {
        this.tekiyouText = null;
        updateAttr();
    }

    public int getShinryoucode() {
        return shinryoucode;
    }

    public Validated<ShinryouDTO> getShinryouToEnter(int visitId) {
        return new ShinryouValidator()
                .setValidatedShinryouId(0)
                .validateShinryoucode(shinryoucode)
                .validate();
    }
}
