package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.Shinryou;

public class RecordShinryou extends TextFlow {

    private int shinryouId;
    private int shinryoucode;

    public RecordShinryou(Shinryou shinryou){
        this.shinryouId = shinryou.getShinryouId();
        this.shinryoucode = shinryou.getShinryoucode();
        Text rep = new Text();
        rep.textProperty().bind(shinryou.repProperty());
        getChildren().add(rep);
    }

    public int getShinryouId() {
        return shinryouId;
    }

    public int getShinryoucode() {
        return shinryoucode;
    }
}
