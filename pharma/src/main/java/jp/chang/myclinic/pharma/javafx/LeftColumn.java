package jp.chang.myclinic.pharma.javafx;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import jp.chang.myclinic.pharma.javafx.event.StartPrescEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class LeftColumn extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(LeftColumn.class);
    private static Callback<PatientList.Model, Observable[]> modelExtractor = model -> new Observable[]{
            model.nameProperty(),
            model.waitStateProperty()
    };

    private PatientList patientList;
    @Autowired
    @Qualifier("tracking-visit-list")
    private ObservableList<PatientList.Model> todaysList;
    @Autowired
    @Qualifier("tracking-pharma-list")
    private ObservableList<PatientList.Model> pharmaQueueList;
    private ObservableList<PatientList.Model> noTrackingList = FXCollections.observableArrayList();
    @Autowired
    @Qualifier("tracking-flag")
    private ObjectProperty<Boolean> isTracking;
    private ObjectProperty<Boolean> listAllVisitsFlag = new SimpleObjectProperty<Boolean>(false);

    LeftColumn() {
        super(4);
        getStyleClass().add("left-column");
    }

    @PostConstruct
    public void postConstruct() {
        getChildren().addAll(
                new Label("患者リスト"),
                createPatientList(),
                createImageExamples(),
                createCommands()
        );
        updateListSource();
    }

    private void updateListSource(){
        if( isTracking.getValue() ){
            if( listAllVisitsFlag.getValue() ){
                patientList.setItems(todaysList);
            } else {
                patientList.setItems(pharmaQueueList);
            }
        } else {
            patientList.setItems(noTrackingList);
        }
    }

    private void selectTodaysList() {
        patientList.itemsProperty().set(todaysList);
    }

    private void selectPharmaQueue() {
        patientList.itemsProperty().set(pharmaQueueList);
    }

    private void selectNonTrackingList() {
        patientList.itemsProperty().set(noTrackingList);
    }

    void clearSelection() {
        patientList.getSelectionModel().clearSelection();
    }

    private Node createPatientList() {
        patientList = new PatientList();
        return patientList;
    }

    private Node createImageExamples() {
        HBox hbox = new HBox(4);
        Label waitCashierLabel = new Label("会計待ち");
        waitCashierLabel.setGraphic(loadImage("/wait_cashier.bmp"));
        Label waitPharmaLabel = new Label("薬渡待ち");
        waitPharmaLabel.setGraphic(loadImage("/wait_drug.bmp"));
        hbox.getChildren().addAll(waitCashierLabel, waitPharmaLabel);
        return hbox;
    }

    private ImageView loadImage(String file) {
        Image image = new Image(file);
        return new ImageView(image);
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        CheckBox includeAllCheckBox = new CheckBox("処方済の患者も含める");
        includeAllCheckBox.setSelected(false);
        includeAllCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                selectTodaysList();
            } else {
                selectPharmaQueue();
            }
        });
        Button reloadButton = new Button("更新");
        Button startPrescButton = new Button("調剤開始");
        reloadButton.setOnAction(evt -> doReload());
        startPrescButton.setOnAction(evt -> doStartPresc());
        hbox.getChildren().addAll(includeAllCheckBox, reloadButton, startPrescButton);
        return hbox;
    }

    private void doReload() {
        if( isTracking.getValue() ){
            doTrackingReload();
        } else {
            doNoTrackingReload();
        }
    }

    private void doTrackingReload(){
        System.out.println("tracking reload");
    }

    private void doNoTrackingReload(){
        System.out.println("no tracking reload");
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

    private void doStartPresc() {
        PatientList.Model item = patientList.getSelectionModel().getSelectedItem();
        if (item != null) {
            int visitId = item.getVisitId();
            fireEvent(new StartPrescEvent(visitId));
        }
    }

}
