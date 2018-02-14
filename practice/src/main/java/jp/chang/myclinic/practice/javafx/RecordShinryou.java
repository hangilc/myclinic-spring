package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShinryouFullDTO;

class RecordShinryou extends TextFlow {

    private int shinryouId;
    private int visitId;
    private int shinryoucode;

    RecordShinryou(ShinryouFullDTO shinryou){
        this.shinryouId = shinryou.shinryou.shinryouId;
        this.visitId = shinryou.shinryou.visitId;
        this.shinryoucode = shinryou.shinryou.shinryoucode;
        String text = shinryou.master.name;
        getChildren().add(new Text(text));
    }

    public int getShinryouId() {
        return shinryouId;
    }

    public int getVisitId() {
        return visitId;
    }

    public int getShinryoucode() {
        return shinryoucode;
    }
}
