package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.*;
import jp.chang.myclinic.utilfx.TwoColumn;

import java.util.ArrayList;
import java.util.List;

public class Record extends VBox {

    private int visitId;
    private TwoColumn body = new TwoColumn(4);
    private VBox drugBox = new VBox();
    private VBox shinryouBox = new VBox();
    private VBox conductBox = new VBox();
    private List<RecordDrug> drugs = new ArrayList<>();
    private List<RecordShinryou> shinryouList = new ArrayList<>();
    private List<RecordConduct> conducts = new ArrayList<>();

    public Record(Visit visit){
        this.visitId = visit.getVisitId();
        getChildren().addAll(
                new RecordTitle(visit),
                body
        );
        addHoken(visit);
        body.getRightBox().getChildren().addAll(drugBox, shinryouBox, conductBox, createCharge(visit));
    }

    public int getVisitId() {
        return visitId;
    }

    public void addText(Text text){
        RecordText recordText = new RecordText(text);
        body.getLeftBox().getChildren().add(recordText);
    }

    private void addHoken(Visit visit){
        TextFlow textFlow = new TextFlow();
        javafx.scene.text.Text text = new javafx.scene.text.Text();
        text.textProperty().bind(visit.hokenRepProperty());
        textFlow.getChildren().add(text);
        body.getRightBox().getChildren().add(textFlow);
    }

    public void addDrug(Drug drug){
        int index = drugs.size() + 1;
        RecordDrug recordDrug = new RecordDrug(drug.getDrugId(), index, drug.repProperty());
        drugBox.getChildren().add(recordDrug);
        drugs.add(recordDrug);
    }

    public void addShinryou(Shinryou shinryou){
        RecordShinryou recordShinryou = new RecordShinryou(shinryou);
        shinryouBox.getChildren().add(recordShinryou);
        shinryouList.add(recordShinryou);
    }

    public void addConduct(Conduct conduct){
        RecordConduct recordConduct = new RecordConduct(conduct);
        conductBox.getChildren().add(recordConduct);
        conducts.add(recordConduct);
    }

    private Node createCharge(Visit visit){
        Label label = new Label();
        label.getStyleClass().add("record-charge");
        label.textProperty().bind(visit.getCharge().repProperty());
        return label;
    }
}
