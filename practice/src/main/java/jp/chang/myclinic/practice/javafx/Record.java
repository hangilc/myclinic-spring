package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;

public class Record extends VBox {

    private int visitId;
    private RecordTextsPane textPane;
    private RecordDrugsPane drugsPane;
    private RecordShinryouPane shinryouPane;
    private RecordConductsPane conductsPane;

    public Record(VisitFull2DTO visit){
        this.visitId = visit.visit.visitId;
        getChildren().addAll(createTitle(visit.visit), createBody(visit));
        setPrefWidth(400);
    }

    public int getVisitId() {
        return visitId;
    }

    private Node createTitle(VisitDTO visit){
        RecordTitle recordTitle = new RecordTitle(visit);
        return recordTitle;
    }

    private Node createBody(VisitFull2DTO visit){
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
        drugsPane = new RecordDrugsPane(visit.drugs, visit.visit);
        shinryouPane = new RecordShinryouPane(visit.shinryouList, visit.visit);
        conductsPane = new RecordConductsPane(visit.conducts, visit.visit.visitId, visit.visit.visitedAt);
        right.getChildren().addAll(
                new RecordHoken(visit.hoken, visit.visit),
                drugsPane,
                shinryouPane,
                conductsPane,
                new RecordCharge(visit.charge)
        );
        return hbox;
    }

    public void addDrug(DrugFullDTO drug){
        drugsPane.addDrug(drug);
    }

    public void modifyDrugDays(int drugId, int days) {
        drugsPane.modifyDrugDays(drugId, days);
    }

    public void deleteDrug(int drugId) {
        drugsPane.deleteDrug(drugId);
    }

    public void insertShinryou(ShinryouFullDTO shinryou){
        shinryouPane.insertShinryou(shinryou);
    }

    public void deleteShinryou(int shinryouId) {
        shinryouPane.deleteShinryou(shinryouId);
    }

    public void addConduct(ConductFullDTO conduct){
        conductsPane.addConduct(conduct);
    }

    public void deleteConduct(int conductId) {
        conductsPane.deleteConduct(conductId);
    }

    public void appendText(TextDTO enteredText) {
        textPane.appendText(enteredText);
    }
}
