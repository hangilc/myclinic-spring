package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class RecordShinryou extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordShinryou.class);
    private int shinryouId;
    private int shinryoucode;

    RecordShinryou(int shinryouId, int shinryoucode, String rep) {
        this.shinryouId = shinryouId;
        this.shinryoucode = shinryoucode;
        this.getChildren().add(new Text(rep));
    }

    public int getShinryouId() {
        return shinryouId;
    }

    public int getShinryoucode() {
        return shinryoucode;
    }
}
