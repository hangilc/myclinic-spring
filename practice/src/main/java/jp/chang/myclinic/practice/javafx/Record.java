package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static jp.chang.myclinic.utilfx.GuiUtil.alertExceptionGui;

public class Record extends VBox {

    private int visitId;
    private RecordTextsPane textPane;
    private RecordDrugsPane drugsPane;
    private RecordShinryouPane shinryouPane;
    private RecordConductsPane conductsPane;
    private StackPane hokenArea = new StackPane();
    private ObjectProperty<ShoukiDTO> shouki;

    Record(VisitFull2DTO visit, Map<Integer, ShinryouAttrDTO> shinryouAttrMap,
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

    public void simulateNewTextButtonClick() {
        textPane.simulateNewTextButtonClick();
    }

    public Optional<TextEnterForm> findTextEnterForm() {
        return textPane.findTextEnterForm();
    }

    public Optional<RecordText> findRecordText(int textId) {
        return textPane.findRecordText(textId);
    }

    public int getLastTextId() {
        return textPane.listTextId().stream().max(Comparator.naturalOrder()).orElse(0);
    }

    public List<Integer> listTextId() {
        return textPane.listTextId();
    }

    public Optional<RecordHoken> findRecordHoken(){
        for(Node node: hokenArea.getChildren()){
            if( node instanceof RecordHoken ){
                return Optional.of((RecordHoken)node);
            }
        }
        return Optional.empty();
    }

    public void simulateNewDrugButtonClick(){
        drugsPane.simulateNewDrugButtonClick();
    }

    public Optional<DrugEnterForm> findDrugEnterForm(){
        return drugsPane.findDrugEnterForm();
    }

    public List<RecordDrug> listDrug(){
        return drugsPane.listDrug();
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
        hokenArea.getChildren().add(createRecordHoken(visit.hoken, visit.visit));
        right.getChildren().addAll(
                hokenArea,
                drugsPane,
                shinryouPane,
                conductsPane,
                new RecordCharge(visit.charge),
                new RecordShouki(shouki)
        );
        return hbox;
    }

    void addDrug(DrugFullDTO drug, DrugAttrDTO attr) {
        drugsPane.addDrug(drug, attr);
    }

    void modifyDrugDays(int drugId, int days) {
        drugsPane.modifyDrugDays(drugId, days);
    }

    void deleteDrug(int drugId) {
        drugsPane.deleteDrug(drugId);
    }

    void insertShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr) {
        shinryouPane.insertShinryou(shinryou, attr);
    }

    void deleteShinryou(int shinryouId) {
        shinryouPane.deleteShinryou(shinryouId);
    }

    void addConduct(ConductFullDTO conduct) {
        conductsPane.addConduct(conduct);
    }

    void deleteConduct(int conductId) {
        conductsPane.deleteConduct(conductId);
    }

    void appendText(TextDTO enteredText) {
        textPane.appendText(enteredText);
    }

    void setShouki(ShoukiDTO shoukiDTO) {
        this.shouki.setValue(shoukiDTO);
    }

    private RecordHoken createRecordHoken(HokenDTO hoken, VisitDTO visit) {
        RecordHoken recordHoken = new RecordHoken(hoken);
        recordHoken.setOnMouseClicked(event -> {
            Service.api.listAvailableHoken(visit.patientId, visit.visitedAt.substring(0, 10))
                    .thenAcceptAsync(availHoken -> {
                        HokenSelectForm form = new HokenSelectForm(availHoken, hoken);
                        form.setCallback(new HokenSelectForm.Callback() {
                            private void replaceWith(Node node) {
                                hokenArea.getChildren().setAll(node);
                            }

                            @Override
                            public void onEnter(VisitDTO newVisit) {
                                newVisit.visitId = visitId;
                                Service.api.updateHoken(newVisit)
                                        .thenCompose(ok -> Service.api.getHoken(visitId))
                                        .thenAcceptAsync(newHoken -> {
                                            replaceWith(createRecordHoken(newHoken, visit));
                                        }, Platform::runLater)
                                        .exceptionally(alertExceptionGui("保険の選択変更に失敗しました。"));
                            }

                            @Override
                            public void onCancel() {
                                replaceWith(recordHoken);
                            }
                        });
                        hokenArea.getChildren().setAll(form);
                    }, Platform::runLater)
                    .exceptionally(alertExceptionGui("保険情報の取得に失敗しました。"));
            });
        return recordHoken;
    }
}
