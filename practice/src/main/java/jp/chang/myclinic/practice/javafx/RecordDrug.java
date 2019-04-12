package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
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
import jp.chang.myclinic.practice.javafx.drug.DrugEditForm;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.utilfx.GuiUtil;

public class RecordDrug extends StackPane {

    private DrugFullDTO drug;
    private DrugAttrDTO attr;
    private VisitDTO visit;
    private int index;
    private TextFlow disp = new TextFlow();
    private Runnable onDeletedHandler = () -> {};

    RecordDrug(DrugFullDTO drug, VisitDTO visit, int index, DrugAttrDTO attr) {
        this.drug = drug;
        this.attr = attr;
        this.visit = visit;
        this.index = index;
        disp.getStyleClass().add("drug-disp");
        updateDisp();
        disp.setOnMouseClicked(this::onDispClick);
        getChildren().add(disp);
    }

    public void setOnDeletedHandler(Runnable handler){
        this.onDeletedHandler = handler;
    }

    public int getDrugId() {
        return drug.drug.drugId;
    }

    public boolean isDisplaying() {
        for (Node node : getChildren()) {
            if (node == disp) {
                return true;
            }
        }
        return false;
    }

    public String getDisplayingText() {
        StringBuilder sb = new StringBuilder();
        for (Node node : disp.getChildren()) {
            if (node instanceof Text) {
                Text t = (Text) node;
                sb.append(t.getText());
            }
        }
        return sb.toString();
    }

    private void updateDisp() {
        String text = String.format("%d)%s", index, DrugUtil.drugRep(drug));
        String tekiyou = (attr != null) ? attr.tekiyou : null;
        if (tekiyou != null) {
            text += " [摘要：" + tekiyou + "]";
        }
        disp.getChildren().clear();
        disp.getChildren().add(new Text(text));
    }

    void modifyDays(int days) {
        DrugFullDTO newDrugFull = DrugFullDTO.copy(drug);
        DrugDTO newDrug = DrugDTO.copy(drug.drug);
        newDrug.days = days;
        newDrugFull.drug = newDrug;
        this.drug = newDrugFull;
        updateDisp();
    }

    void setIndex(int index) {
        this.index = index;
        updateDisp();
    }

    private void onDispClick(MouseEvent event) {
        if (event.isPopupTrigger()) {
            doContextMenu(event);
        } else {
            if (!PracticeEnv.INSTANCE.isCurrentOrTempVisitId(visit.visitId)) {
                if (!GuiUtil.confirm("現在診察中でありませんが、この薬剤を編集しますか？")) {
                    return;
                }
            }
            DrugEditForm form = new DrugEditForm(drug, attr, visit);
            form.setOnEnteredHandler((drug, attr) -> {
                RecordDrug.this.drug = drug;
                RecordDrug.this.attr = attr;
                updateDisp();
                showDisp();
            });
            form.setOnCancelHandler(() -> showDisp());
            form.setOnDeletedHandler(onDeletedHandler);
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

    private void showDisp() {
        getChildren().clear();
        getChildren().add(disp);
    }
}
