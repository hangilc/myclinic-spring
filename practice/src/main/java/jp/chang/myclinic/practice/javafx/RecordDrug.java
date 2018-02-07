package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;

class RecordDrug extends TextFlow {

    private int drugId;

    RecordDrug(DrugFullDTO drug, int index){
        this.drugId = drug.drug.drugId;
        String text = String.format("%d)%s", index, DrugUtil.drugRep(drug));
        getChildren().add(new Text(text));
    }

    public int getDrugId() {
        return drugId;
    }

    public void modifyDays(int days){
        
    }
}
