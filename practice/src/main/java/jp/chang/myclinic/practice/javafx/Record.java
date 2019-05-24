package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import jp.chang.myclinic.practice.javafx.hoken.HokenSelectForm;
import jp.chang.myclinic.practice.javafx.shinryou.AddRegularForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static jp.chang.myclinic.utilfx.GuiUtil.alertExceptionGui;

public class Record extends VBox {

    private int visitId;
    private RecordTitle recordTitle;
    private RecordTextsPane textPane;
    private RecordDrugsPane drugsPane;
    private RecordShinryouPane shinryouPane;
    private RecordConductsPane conductsPane;
    private StackPane hokenArea = new StackPane();
    private ObjectProperty<ShoukiDTO> shouki;
    private Runnable onDeletedHandler = () -> {};

    Record(VisitFull2DTO visit, Map<Integer, ShinryouAttrDTO> shinryouAttrMap,
                  Map<Integer, DrugAttrDTO> drugAttrMap, ShoukiDTO shouki) {
        this.visitId = visit.visit.visitId;
        this.shouki = new SimpleObjectProperty<>(shouki);
        this.recordTitle = new RecordTitle(visit.visit, this.shouki);
        this.getStyleClass().add("visit-record");
        recordTitle.setOnDeletedHandler(() -> onDeletedHandler.run());
        getChildren().addAll(
                recordTitle,
                createBody(visit, shinryouAttrMap, drugAttrMap)
        );
        setPrefWidth(400);
    }

    public void setOnDeletedHandler(Runnable onDeletedHandler) {
        this.onDeletedHandler = onDeletedHandler;
    }

    void styleAsCurrentVisit(){
        recordTitle.styleAsCurrentVisit();
    }

    void styleAsTempVisit(){
        recordTitle.styleAsTempVisit();
    }

    void styleAsRegular(){ recordTitle.styleAsRegular(); }

    public int getVisitId() {
        return visitId;
    }

    public double getTextsPaneWidth(){
        return textPane.getWidth();
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

    public void simulateAddRegularShinryouClick(){
        shinryouPane.simulateEnterRegularShinryouClick();
    }

    public RecordShinryouPane getShinryouPane(){
        return shinryouPane;
    }

    public Optional<AddRegularForm> findAddRegularForm(){
        return shinryouPane.findAddRegularForm();
    }

    public List<RecordShinryou> listShinryou(){
        return shinryouPane.listShinryou();
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
        textPane = new RecordTextsPane(visit.visit.visitId, visit.texts);
        left.getChildren().add(textPane);
        drugsPane = new RecordDrugsPane(visit.drugs, visit.visit, drugAttrMap);
        shinryouPane = new RecordShinryouPane(visit.shinryouList, visit.visit, shinryouAttrMap);
        shinryouPane.setOnConductsEnteredHandler(newConducts -> {
            newConducts.forEach(this::addConduct);
        });
        conductsPane = new RecordConductsPane(visit.conducts, visit.visit.visitId,
                DateTimeUtil.parseSqlDateTime(visit.visit.visitedAt).toLocalDate());
        hokenArea.getChildren().add(new RecordHoken(visit.hoken, visit.visit));
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

    void addText(TextDTO text){
        textPane.appendText(text);
    }

    void addDrug(DrugFullDTO drug, DrugAttrDTO attr) {
        drugsPane.addDrug(drug, attr);
    }

    void addShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr) {
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

}
