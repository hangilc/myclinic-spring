package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.Drug;

public class RecordDrug extends TextFlow {
    private int drugId;
    private Text index = new Text();
    private Text drugRep = new Text();

    public RecordDrug(int index, Drug drug){
        this.drugId = drug.getDrugId();
        updateIndex(index);
        this.drugRep.textProperty().bind(drug.repProperty());
        getChildren().addAll(this.index, this.drugRep);
    }

    public int getDrugId() {
        return drugId;
    }

    final public void updateIndex(int index){
        this.index.setText(String.format("%d)", index));
    }
}
