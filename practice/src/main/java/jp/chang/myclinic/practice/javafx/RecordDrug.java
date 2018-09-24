package jp.chang.myclinic.practice.javafx;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.drug.EditForm;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.utilfx.GuiUtil;

class RecordDrug extends StackPane {

    private DrugFullDTO drug;
    private String tekiyou;
    private VisitDTO visit;
    private int index;
    private TextFlow disp = new TextFlow();

    RecordDrug(DrugFullDTO drug, VisitDTO visit, int index, DrugAttrDTO attr){
        this.drug = drug;
        if( attr != null ){
            this.tekiyou = attr.tekiyou;
        }
        this.visit = visit;
        this.index = index;
        disp.getStyleClass().add("drug-disp");
        updateDisp();
        disp.setOnMouseClicked(this::onDispClick);
        getChildren().add(disp);
    }

    private void updateDisp(){
        String text = String.format("%d)%s", index, DrugUtil.drugRep(drug));
        if( tekiyou != null ){
            text += " [摘要：" + tekiyou + "]";
        }
        disp.getChildren().clear();
        disp.getChildren().add(new Text(text));
    }

    public int getDrugId() {
        return drug.drug.drugId;
    }

    void modifyDays(int days){
        DrugFullDTO newDrugFull = DrugFullDTO.copy(drug);
        DrugDTO newDrug = DrugDTO.copy(drug.drug);
        newDrug.days = days;
        newDrugFull.drug = newDrug;
        this.drug = newDrugFull;
        updateDisp();
    }

    void setIndex(int index){
        this.index = index;
        updateDisp();
    }

    private void onDispClick(MouseEvent event){
        if( event.isPopupTrigger() ){
            doContextMenu(event);
        } else  {
            if( !PracticeEnv.INSTANCE.isCurrentOrTempVisitId(visit.visitId) ){
                if( !GuiUtil.confirm("現在診察中でありませんが、この薬剤を編集しますか？") ){
                    return;
                }
            }
            EditForm form = new EditForm(drug, tekiyou, visit){
                @Override
                protected void onUpdated(DrugFullDTO updated) {
                    RecordDrug.this.drug = updated;
                    updateDisp();
                    showDisp();
                }

                @Override
                protected void onClose() {
                    showDisp();
                }

                @Override
                protected void onTekiyouModified(String newTekiyou) {
                    RecordDrug.this.tekiyou = newTekiyou;
                    updateDisp();
                }
            };
            getChildren().remove(disp);
            getChildren().add(form);
        }
    }

    private void doContextMenu(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        {
            MenuItem item = new MenuItem("文字列コピー");
            item.setOnAction(evt -> {
                String text = DrugUtil.drugRep(drug);
                GuiUtil.copyToClipboard(text);
            });
            contextMenu.getItems().add(item);
        }
        contextMenu.show(disp, event.getScreenX(), event.getScreenY());
    }

    private void showDisp(){
        getChildren().clear();
        getChildren().add(disp);
    }
}
