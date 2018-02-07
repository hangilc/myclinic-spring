package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;

public class Record extends VBox {

    private int visitId;
    private RecordDrugsPane drugsPane;

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
        left.getChildren().add(new RecordTextsPane(visit.texts, visit.visit.visitId));
        drugsPane = new RecordDrugsPane(visit.drugs, visit.visit.patientId, visit.visit.visitId, visit.visit.visitedAt);
        right.getChildren().addAll(
                new RecordHoken(visit.hoken, visit.visit),
                drugsPane,
                new RecordShinryouPane(visit.shinryouList),
                new RecordConductsPane(visit.conducts),
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
}
