package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;

public class ShinryouInput extends GridPane {

    private int shinryoucode = 0;
    private Text nameText = new Text();
    private Text tekiyouText = null;
    private Text shoujouShoukiText = null;

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

    private void updateAttrAndShouki() {
        getChildren().removeIf(node -> GridPane.getRowIndex(node) >= 1);
        int index = 1;
        if (tekiyouText != null) {
            addRow(index++, new Label("摘要："), tekiyouText);
        }
        if (shoujouShoukiText != null) {
            addRow(index, new Label("症状詳記："), shoujouShoukiText);
        }
    }

    public void setTekiyou(String tekiyou) {
        if( tekiyou == null || tekiyou.isEmpty() ){
            tekiyouText = null;
        } else {
            if (tekiyouText == null) {
                tekiyouText = new Text();
            }
            tekiyouText.setText(tekiyou);
        }
        updateAttrAndShouki();
    }

    public void setShoujouShouki(String shoujouShouki){
        if( shoujouShouki == null || shoujouShouki.isEmpty() ){
            shoujouShoukiText = null;
        } else {
            if (shoujouShoukiText == null) {
                shoujouShoukiText = new Text();
            }
            shoujouShoukiText.setText(shoujouShouki);
        }
        updateAttrAndShouki();
    }

    public void deleteTekiyou() {
        setTekiyou(null);
    }

    public void deleteShoujouShouki(){
        setShoujouShouki(null);
    }

    public int getShinryoucode() {
        return shinryoucode;
    }

}
