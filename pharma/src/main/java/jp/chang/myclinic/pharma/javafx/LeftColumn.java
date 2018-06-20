package jp.chang.myclinic.pharma.javafx;

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
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.tracker.model.PharmaQueue;
import jp.chang.myclinic.pharma.tracker.model.Wqueue;

class LeftColumn extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(LeftColumn.class);
    private PatientList patientList;
    private CheckBox includeAllCheckBox;
    private ObservableList<PatientList.Model> todaysList = FXCollections.observableArrayList();
    private ObservableList<PatientList.Model> pharmaQueueList = FXCollections.observableArrayList();

    LeftColumn() {
        super(4);
        getStyleClass().add("left-column");
        getChildren().addAll(
                new Label("患者リスト"),
                createPatientList(),
                createImageExamples(),
                createCommands()
        );
        patientList.setItems(pharmaQueueList);
    }

    void addWqueue(Wqueue wqueue){
        todaysList.add(new PatientList.Model(wqueue.getVisit().getPatient(), wqueue));
    }

    void addPharmaQueue(PharmaQueue pharmaQueue){
        pharmaQueueList.add(new PatientList.Model(pharmaQueue.getVisit().getPatient(), null));
    }

    void clearSelection(){
        patientList.getSelectionModel().clearSelection();
    }

    void reloadPatientList(){
        doReload();
    }

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

    private Node createCommands(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        includeAllCheckBox = new CheckBox("処方済の患者も含める");
        includeAllCheckBox.setSelected(false);
        Button reloadButton = new Button("更新");
        Button startPrescButton = new Button("調剤開始");
        reloadButton.setOnAction(evt -> doReload());
        startPrescButton.setOnAction(evt -> doStartPresc());
        hbox.getChildren().addAll(includeAllCheckBox, reloadButton, startPrescButton);
        return hbox;
    }

    private boolean getIncludeAllPatients(){
        return includeAllCheckBox.isSelected();
    }

    private void doReload(){
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
    }

    private void doStartPresc(){
//        PharmaQueueFullDTO item = patientList.getSelectionModel().getSelectedItem();
//        if( item != null ){
//            onPatientSelected(item);
//        }
    }

    protected void onPatientSelected(PharmaQueueFullDTO item){

    }

}
