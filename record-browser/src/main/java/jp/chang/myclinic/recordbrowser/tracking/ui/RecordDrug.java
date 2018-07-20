package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.DrugModel;

class RecordDrug extends TextFlow{

    //private static Logger logger = LoggerFactory.getLogger(RecordDrug.class);
    private int drugId;
    private Text indexText = new Text("");

    RecordDrug(int index, DrugModel drugModel) {
        this.drugId = drugModel.getDrugId();
        Text repText = new Text();
        repText.textProperty().bind(drugModel.repProperty());
        getChildren().addAll(indexText, repText);
        doSetIndex(index);
    }

    private void doSetIndex(int i){
        indexText.setText(i + ")");
    }

    int getDrugId() {
        return drugId;
    }

    void setIndex(int i){
        doSetIndex(i);
    }

}
