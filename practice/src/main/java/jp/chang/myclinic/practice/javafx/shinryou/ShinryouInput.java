package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShinryouMasterDTO;

public class ShinryouInput extends GridPane {

    private int shinryoucode = 0;
    private Text nameText = new Text();

    public ShinryouInput(){
        setHgap(4);
        Label label = new Label("名称：");
        label.setMinWidth(USE_PREF_SIZE);
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().add(nameText);
        addRow(0, label, textFlow);
    }

    public void setMaster(ShinryouMasterDTO master){
        this.shinryoucode = master.shinryoucode;
        this.nameText.setText(master.name);
    }

    public int getShinryoucode(){
        return shinryoucode;
    }

}
