package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.pharma.javafx.event.ReloadTrackingEvent;
import jp.chang.myclinic.pharma.javafx.event.StartPrescEvent;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.List;
import java.util.stream.Collectors;

class LeftColumn extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(LeftColumn.class);
    private static Callback<PatientList.Model, Observable[]> modelExtractor = model -> new Observable[]{
            model.nameProperty(),
            model.waitStateProperty()
    };

    private PatientList patientList;
    private ObservableList<PatientList.Model> todaysList;
    private ObservableList<PatientList.Model> pharmaQueueList;
    private ObservableList<PatientList.Model> noTrackingList = FXCollections.observableArrayList();
    private SimpleBooleanProperty isTracking;
    private ObjectProperty<Boolean> listAllVisitsFlag = new SimpleObjectProperty<Boolean>(false);

    LeftColumn(ObservableList<PatientList.Model> trackingVisitList, ObservableList<PatientList.Model> pharmaList,
               SimpleBooleanProperty tracking) {
        super(4);
        this.todaysList = trackingVisitList;
        this.pharmaQueueList = pharmaList;
        this.isTracking = tracking;
        getStyleClass().add("left-column");
        postConstruct();
    }

    private void postConstruct() {
        getChildren().addAll(
                new HBox(4, new Label("患者リスト"), createNoTrackingNotice()),
                createPatientList(),
                createImageExamples(),
                createCommands()
        );
        isTracking.addListener((obs, oldValue, newValue) -> {
            updateListSource();
        });
        listAllVisitsFlag.addListener((obs, oldValue, newValue) -> updateListSource());
        updateListSource();
    }

    private Node createNoTrackingNotice(){
        Label label = new Label("（非同期中）");
        label.getStyleClass().add("no-tracking-notice");
        label.visibleProperty().bind(Bindings.not(isTracking));
        label.managedProperty().bind(Bindings.not(isTracking));
        return label;
    }

    private void updateListSource() {
        if (isTracking.getValue()) {
            if (listAllVisitsFlag.getValue()) {
                patientList.setItems(todaysList);
            } else {
                patientList.setItems(pharmaQueueList);
            }
        } else {
            noTrackingList.clear();
            patientList.setItems(noTrackingList);
            doNoTrackingReload();
        }
    }

    void clearSelection() {
        patientList.getSelectionModel().clearSelection();
    }

    void onPrescDone(){
        clearSelection();
        if( !isTracking.getValue() ){
            doNoTrackingReload();
        }
    }

    void onPrescCancel(){
        clearSelection();
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
        includeAllCheckBox.selectedProperty().bindBidirectional(listAllVisitsFlag);
        Button reloadButton = new Button("更新");
        Button startPrescButton = new Button("調剤開始");
        reloadButton.setOnAction(evt -> doReload());
        startPrescButton.setOnAction(evt -> doStartPresc());
        hbox.getChildren().addAll(includeAllCheckBox, reloadButton, startPrescButton);
        return hbox;
    }

    private void doReload() {
        if (isTracking.getValue()) {
            doTrackingReload();
        } else {
            doNoTrackingReload();
        }
    }

    private void doTrackingReload() {
        fireEvent(new ReloadTrackingEvent());
    }

    private void doNoTrackingReload() {
        if (listAllVisitsFlag.getValue()) {
            doNoTrackingAllVisitsReload();
        } else {
            doNoTrackingPharmaQueueReload();
        }
    }

    private void doNoTrackingAllVisitsReload() {
        Service.api.listPharmaQueueForToday()
                .thenAccept(list -> Platform.runLater(() -> {
                    List<PatientList.Model> models = list.stream().map(ModelImpl::fromPharmaQueueFullDTO)
                            .collect(Collectors.toList());
                    noTrackingList.setAll(models);
                }))
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doNoTrackingPharmaQueueReload() {
        Service.api.listPharmaQueueForPrescription()
                .thenAccept(list -> Platform.runLater(() -> {
                    List<PatientList.Model> models = list.stream().map(ModelImpl::fromPharmaQueueFullDTO)
                            .collect(Collectors.toList());
                    noTrackingList.setAll(models);
                }))
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doStartPresc() {
        PatientList.Model item = patientList.getSelectionModel().getSelectedItem();
        if (item != null) {
            int visitId = item.getVisitId();
            fireEvent(new StartPrescEvent(visitId));
        }
    }

}
