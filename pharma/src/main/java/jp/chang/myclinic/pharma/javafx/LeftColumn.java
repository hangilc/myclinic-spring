package jp.chang.myclinic.pharma.javafx;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jp.chang.myclinic.pharma.Scope;
import jp.chang.myclinic.pharma.tracker.model.PharmaQueue;
import jp.chang.myclinic.pharma.tracker.model.Wqueue;

class LeftColumn extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(LeftColumn.class);
    private static Callback<PatientList.Model, Observable[]> modelExtractor = model -> new Observable[]{
            model.wqueue.waitStateProperty(),
            model.patient.lastNameProperty(),
            model.patient.firstNameProperty(),
            model.patient.lastNameYomiProperty(),
            model.patient.firstNameYomiProperty()
    };

    private PatientList patientList;
    private CheckBox includeAllCheckBox;
    private ObservableList<PatientList.Model> todaysList = FXCollections.observableArrayList(modelExtractor);
    private ObservableList<PatientList.Model> pharmaQueueList = FXCollections.observableArrayList(modelExtractor);

    LeftColumn(Scope scope) {
        super(4);
        getStyleClass().add("left-column");
        getChildren().addAll(
                new Label("患者リスト"),
                createPatientList(),
                createImageExamples(),
                createCommands(scope)
        );
        patientList.setItems(pharmaQueueList);
    }

    void addWqueue(Wqueue wqueue){
        todaysList.add(new PatientList.Model(wqueue.getVisit().getPatient(), wqueue));
    }

    void addPharmaQueue(PharmaQueue pharmaQueue){
        pharmaQueueList.add(new PatientList.Model(pharmaQueue.getVisit().getPatient(), pharmaQueue.getWqueue()));
    }

    void deletePharmaQueue(int visitId){
        todaysList.forEach(model -> {
            if( model.wqueue != null && model.wqueue.getVisit().getVisitId() == visitId ){
                model.wqueue = null;
            }
        });
        pharmaQueueList.removeIf(model -> model.wqueue.getVisit().getVisitId() == visitId);
    }

    private void selectTodaysList(){
        patientList.itemsProperty().set(todaysList);
    }

    private void selectPharmaQueue(){
        patientList.itemsProperty().set(pharmaQueueList);
    }

    void clearSelection(){
        patientList.getSelectionModel().clearSelection();
    }

//    void reloadPatientList(){
//        onReload();
//    }

    private Node createPatientList(){
        patientList = new PatientList();
        return patientList;
    }

    private Node createImageExamples(){
        HBox hbox = new HBox(4);
        Label waitCashierLabel = new Label("会計待ち");
        waitCashierLabel.setGraphic(loadImage("/wait_cashier.bmp"));
        Label waitPharmaLabel = new Label("薬渡待ち");
        waitPharmaLabel.setGraphic(loadImage("/wait_drug.bmp"));
        hbox.getChildren().addAll(waitCashierLabel, waitPharmaLabel);
        return hbox;
    }

    private ImageView loadImage(String file){
        Image image = new Image(file);
        return new ImageView(image);
    }

    private Node createCommands(Scope scope){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        includeAllCheckBox = new CheckBox("処方済の患者も含める");
        includeAllCheckBox.setSelected(false);
        includeAllCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue ){
                selectTodaysList();
            } else {
                selectPharmaQueue();
            }
        });
        Button reloadButton = new Button("更新");
        Button startPrescButton = new Button("調剤開始");
        reloadButton.setOnAction(evt -> scope.reloadPatientList());
        startPrescButton.setOnAction(evt -> doStartPresc());
        hbox.getChildren().addAll(includeAllCheckBox, reloadButton, startPrescButton);
        return hbox;
    }

//    private boolean getIncludeAllPatients(){
//        return includeAllCheckBox.isSelected();
//    }
//
//    private void doReload(){
//        if( getIncludeAllPatients() ){
//            Service.api.listPharmaQueueForToday()
//                    .thenAccept(list -> Platform.runLater(() -> {
//                        patientList.itemsProperty().getValue().setAll(list);
//                    }))
//                    .exceptionally(HandlerFX::exceptionally);
//        } else {
//            Service.api.listPharmaQueueForPrescription()
//                    .thenAccept(list -> Platform.runLater(() -> {
//                        patientList.itemsProperty().getValue().setAll(list);
//                    }))
//                    .exceptionally(HandlerFX::exceptionally);
//        }
//    }

    private void doStartPresc(){
        PatientList.Model item = patientList.getSelectionModel().getSelectedItem();
        if( item != null ){
            onStartPresc(item.visit.getVisitId());
        }
    }

    protected void onStartPresc(int visitId){

    }

}
