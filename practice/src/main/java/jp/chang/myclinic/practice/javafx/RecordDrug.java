package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;

class RecordDrug extends StackPane {

    private DrugFullDTO drug;
    private int index;
    private TextFlow disp = new TextFlow();

    RecordDrug(DrugFullDTO drug, int index){
        this.drug = drug;
        this.index = index;
        updateDisp();
        getChildren().add(disp);
    }

    private void updateDisp(){
        String text = String.format("%d)%s", index, DrugUtil.drugRep(drug));
        disp.getChildren().clear();
        disp.getChildren().add(new TextFlow(new Text(text)));
    }

    public int getDrugId() {
        return drug.drug.drugId;
    }

    public void modifyDays(int days){
        DrugFullDTO newDrugFull = DrugFullDTO.copy(drug);
        DrugDTO newDrug = DrugDTO.copy(drug.drug);
        newDrug.days = days;
        newDrugFull.drug = newDrug;
        this.drug = newDrugFull;
        updateDisp();
    }
}
