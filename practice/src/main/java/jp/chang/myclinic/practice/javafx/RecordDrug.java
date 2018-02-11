package jp.chang.myclinic.practice.javafx;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.drug.DrugEditForm;
import jp.chang.myclinic.practice.javafx.drug.DrugForm;
import jp.chang.myclinic.practice.lib.GuiUtil;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.drug.DrugFormHelper;
import jp.chang.myclinic.util.DrugUtil;

class RecordDrug extends StackPane {

    private DrugFullDTO drug;
    private VisitDTO visit;
    private int index;
    private TextFlow disp = new TextFlow();

    RecordDrug(DrugFullDTO drug, VisitDTO visit, int index){
        this.drug = drug;
        this.visit = visit;
        this.index = index;
        disp.getStyleClass().add("drug-disp");
        updateDisp();
        disp.setOnMouseClicked(this::onDispClick);
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

    public void setIndex(int index){
        this.index = index;
        updateDisp();
    }

    private void onDispClick(MouseEvent event){
        DrugEditForm form = new DrugEditForm(visit, drug){
            @Override
            protected void onEnter(DrugForm self) {
                DrugFormHelper.convertToDrug(self.getDrugFormGetter(), drug.drug.drugId, visit.visitId, 0, (drug, errors) -> {
                    if( errors.size() > 0 ){
                        GuiUtil.alertError(String.join("\n", errors));
                    } else {
                        PracticeLib.updateDrug(drug, newDrugFull -> {
                            RecordDrug.this.drug = newDrugFull;
                            updateDisp();
                            showDisp();
                        });
                    }
                });
            }

            @Override
            protected void onClose(DrugForm self) {
                showDisp();
            }
        };
        getChildren().remove(disp);
        getChildren().add(form);
    }

    private void showDisp(){
        getChildren().clear();
        getChildren().add(disp);
    }
}
