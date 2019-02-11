package jp.chang.myclinic.practice.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;

import java.util.Map;

public class Record extends VBox {

    private int visitId;
    private RecordTextsPane textPane;
    private RecordDrugsPane drugsPane;
    private RecordShinryouPane shinryouPane;
    private RecordConductsPane conductsPane;
    private ObjectProperty<ShoukiDTO> shouki;

    public Record(VisitFull2DTO visit, Map<Integer, ShinryouAttrDTO> shinryouAttrMap,
                  Map<Integer, DrugAttrDTO> drugAttrMap, ShoukiDTO shouki) {
        this.visitId = visit.visit.visitId;
        this.shouki = new SimpleObjectProperty<>(shouki);
        getChildren().addAll(
                new RecordTitle(visit.visit, this.shouki),
                createBody(visit, shinryouAttrMap, drugAttrMap)
        );
        setPrefWidth(400);
    }

    public int getVisitId() {
        return visitId;
    }

    public void simulateNewTextButtonClick(){
        textPane.simulateNewTextButtonClick();
    }

    private Node createBody(VisitFull2DTO visit, Map<Integer, ShinryouAttrDTO> shinryouAttrMap,
                            Map<Integer, DrugAttrDTO> drugAttrMap) {
        HBox hbox = new HBox();
        VBox left = new VBox();
        VBox right = new VBox();
        left.prefWidthProperty().bind(widthProperty().divide(2));
        left.setStyle("-fx-padding: 5");
        right.prefWidthProperty().bind(widthProperty().divide(2));
        right.setStyle("-fx-padding: 5");
        hbox.getChildren().addAll(left, right);
        textPane = new RecordTextsPane(visit.texts, visit.visit.visitId);
        left.getChildren().add(textPane);
        drugsPane = new RecordDrugsPane(visit.drugs, visit.visit, drugAttrMap);
        shinryouPane = new RecordShinryouPane(visit.shinryouList, visit.visit, shinryouAttrMap);
        conductsPane = new RecordConductsPane(visit.conducts, visit.visit.visitId, visit.visit.visitedAt);
        right.getChildren().addAll(
                new RecordHoken(visit.hoken, visit.visit),
                drugsPane,
                shinryouPane,
                conductsPane,
                new RecordCharge(visit.charge),
                new RecordShouki(shouki)
        );
        return hbox;
    }

    public void addDrug(DrugFullDTO drug, DrugAttrDTO attr) {
        drugsPane.addDrug(drug, attr);
    }

    public void modifyDrugDays(int drugId, int days) {
        drugsPane.modifyDrugDays(drugId, days);
    }

    public void deleteDrug(int drugId) {
        drugsPane.deleteDrug(drugId);
    }

    public void insertShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr) {
        shinryouPane.insertShinryou(shinryou, attr);
    }

    public void deleteShinryou(int shinryouId) {
        shinryouPane.deleteShinryou(shinryouId);
    }

    public void addConduct(ConductFullDTO conduct) {
        conductsPane.addConduct(conduct);
    }

    public void deleteConduct(int conductId) {
        conductsPane.deleteConduct(conductId);
    }

    public void appendText(TextDTO enteredText) {
        textPane.appendText(enteredText);
    }

    public void setShouki(ShoukiDTO shoukiDTO){
        this.shouki.setValue(shoukiDTO);
    }
}
