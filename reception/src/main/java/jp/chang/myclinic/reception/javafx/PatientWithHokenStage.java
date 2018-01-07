package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.HokenListDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.Service;
import jp.chang.myclinic.util.KoukikoureiUtil;
import jp.chang.myclinic.util.ShahokokuhoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PatientWithHokenStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(PatientWithHokenStage.class);

    private int patientId;
    private BooleanProperty currentActiveOnly = new SimpleBooleanProperty(true);
    private HokenListDTO hokenList;
    private ObjectProperty<ObservableList<HokenTable.Model>> tableModels =
            new SimpleObjectProperty<>(FXCollections.emptyObservableList());
    private ObjectProperty<HokenTable.Model> tableSelection = new SimpleObjectProperty<>();

    public PatientWithHokenStage(PatientDTO patient, HokenListDTO hokenList) {
        this.patientId = patient.patientId;
        this.hokenList = hokenList;
        HokenTable hokenTable = new HokenTable();
        VBox root = new VBox(4);
        root.setFillWidth(true);
        {
            VBox vbox = new VBox(4);
            vbox.setMaxWidth(360);
            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatient(patient);
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Button editPatientButton = new Button("編集");
            hbox.getChildren().add(editPatientButton);
            vbox.getChildren().addAll(patientInfo, hbox);
            TitledPane titledPane = new TitledPane("基本情報", vbox);
            titledPane.setCollapsible(false);
            root.getChildren().add(titledPane);
        }
        {
            VBox vbox = new VBox(4);
            vbox.setFillWidth(true);
            hokenTable.itemsProperty().bind(tableModels);
            tableSelection.bind(hokenTable.getSelectionModel().selectedItemProperty());
            {
                HBox hbox = new HBox(4);
                hbox.setMaxWidth(Double.MAX_VALUE);
                updateHokenTable();
                hbox.getChildren().add(hokenTable);
                {
                    VBox buttons = new VBox(4);
                    Button editButton = new Button("編集");
                    Button deleteButton = new Button("削除");
                    editButton.setDisable(true);
                    deleteButton.setDisable(true);
                    tableSelection.addListener((obs, oldValue, newValue) -> {
                        editButton.setDisable(newValue == null);
                        deleteButton.setDisable(newValue == null);
                    });
                    deleteButton.setOnAction(event -> doDeleteHoken(tableSelection.get()));
                    buttons.getChildren().addAll(editButton, deleteButton);
                    hbox.getChildren().add(buttons);
                }
                HBox.setHgrow(hokenTable, Priority.ALWAYS);
                vbox.getChildren().add(hbox);
            }
            {
                CheckBox checkBox = new CheckBox("現在有効のみ");
                checkBox.selectedProperty().bindBidirectional(currentActiveOnly);
                currentActiveOnly.addListener((Observable observable) -> {
                    updateHokenTable();
                });
                vbox.getChildren().add(checkBox);
            }
            {
                HBox row = new HBox(4);
                Button newShahokokuhoButton = new Button("新規社保国保");
                Button newKoukikoureiButton = new Button("新規後期高齢");
                Button newKouhiButton = new Button("新規公費負担");
                newShahokokuhoButton.setOnAction(event -> doNewShahokokuho());
                newKoukikoureiButton.setOnAction(event -> doNewKoukikourei());
                newKouhiButton.setOnAction(event -> doNewKouhi());
                row.getChildren().addAll(newShahokokuhoButton, newKoukikoureiButton, newKouhiButton);
                vbox.getChildren().add(row);
            }
            TitledPane titledPane = new TitledPane("保険情報", vbox);
            titledPane.setCollapsible(false);
            root.getChildren().add(titledPane);
        }
        {
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_RIGHT);
            Button closeButton = new Button("閉じる");
            closeButton.setOnAction(event -> close());
            row.getChildren().add(closeButton);
            root.getChildren().add(row);
        }
        root.setStyle("-fx-padding: 10");
        Scene scene = new Scene(root, 500, 660);
        setScene(scene);
        sizeToScene();
    }

    private HokenListDTO currentActiveHokenList() {
        HokenListDTO filtered = new HokenListDTO();
        String curr = LocalDate.now().toString();
        if (hokenList.shahokokuhoListDTO != null) {
            filtered.shahokokuhoListDTO = hokenList.shahokokuhoListDTO.stream()
                    .filter(h -> isCurrent(h.validFrom, h.validUpto, curr)).collect(Collectors.toList());
        }
        if (hokenList.koukikoureiListDTO != null) {
            filtered.koukikoureiListDTO = hokenList.koukikoureiListDTO.stream()
                    .filter(h -> isCurrent(h.validFrom, h.validUpto, curr)).collect(Collectors.toList());
        }
        if (hokenList.roujinListDTO != null) {
            filtered.roujinListDTO = hokenList.roujinListDTO.stream()
                    .filter(h -> isCurrent(h.validFrom, h.validUpto, curr)).collect(Collectors.toList());
        }
        if (hokenList.kouhiListDTO != null) {
            filtered.kouhiListDTO = hokenList.kouhiListDTO.stream()
                    .filter(h -> isCurrent(h.validFrom, h.validUpto, curr)).collect(Collectors.toList());
        }
        return filtered;
    }

    private boolean isCurrent(String validFrom, String validUpto, String current) {
        return validFrom.compareTo(current) <= 0 &&
                (validUpto == null || validUpto.equals("0000-00-00") || validUpto.compareTo(current) >= 0);
    }

    private void doNewShahokokuho() {
        EditShahokokuhoStage stage = new EditShahokokuhoStage();
        stage.setOnEnter(data -> {
            data.patientId = patientId;
            Service.api.enterShahokokuho(data)
                    .thenAccept(shahokokuhoId -> {
                        Platform.runLater(() -> {
                            data.shahokokuhoId = shahokokuhoId;
                            fetchAndUpdateHokenList();
                            stage.close();
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("Failed to enter shahokokuho.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("社保・国保の新規登録に失敗しました。", ex));
                        return null;
                    });
        });
        stage.showAndWait();
    }

    private void doNewKoukikourei() {
        EditKoukikoureiStage stage = new EditKoukikoureiStage();
        stage.setOnEnter(data -> {
            data.patientId = patientId;
            Service.api.enterKoukikourei(data)
                    .thenAccept(koukikoureiId -> {
                        Platform.runLater(() -> {
                            data.koukikoureiId = koukikoureiId;
                            fetchAndUpdateHokenList();
                            stage.close();
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("Failed to enter koukikourei.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("後期高齢保険の新規登録に失敗しました。", ex));
                        return null;
                    });
        });
        stage.showAndWait();
    }

    private void doNewKouhi() {
        EditKouhiStage stage = new EditKouhiStage();
        stage.showAndWait();
    }

    private List<HokenTable.Model> hokenListToModelList(HokenListDTO hokenList) {
        List<HokenTable.Model> models = new ArrayList<>();
        if (hokenList.shahokokuhoListDTO != null) {
            models.addAll(hokenList.shahokokuhoListDTO.stream().map(HokenTable.ShahokokuhoModel::new).collect(Collectors.toList()));
        }
        if (hokenList.koukikoureiListDTO != null) {
            models.addAll(hokenList.koukikoureiListDTO.stream().map(HokenTable.KoukikoureiModel::new).collect(Collectors.toList()));
        }
        if (hokenList.roujinListDTO != null) {
            models.addAll(hokenList.roujinListDTO.stream().map(HokenTable.RoujinModel::new).collect(Collectors.toList()));
        }
        if (hokenList.kouhiListDTO != null) {
            models.addAll(hokenList.kouhiListDTO.stream().map(HokenTable.KouhiModel::new).collect(Collectors.toList()));
        }
        return models;
    }

    private ObservableList<HokenTable.Model> composeTableModels(){
        List<HokenTable.Model> models = hokenListToModelList(currentActiveOnly.get() ? currentActiveHokenList() : hokenList);
        return FXCollections.observableArrayList(models);
    }

    private void updateHokenTable(){
        tableModels.setValue(composeTableModels());
    }

    private void fetchAndUpdateHokenList() {
        Service.api.listHoken(patientId)
                .thenAccept(newHokenList -> {
                    Platform.runLater(() -> {
                        hokenList = newHokenList;
                        updateHokenTable();
                    });
                })
                .exceptionally(ex -> {
                    logger.error("Failed to list hoken.", ex);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "保険情報の取得に失敗しました。" + ex,
                            ButtonType.OK);
                    alert.showAndWait();
                    return null;
                });
    }

    private void doDeleteHoken(HokenTable.Model model){
        if( model instanceof HokenTable.ShahokokuhoModel ){
            HokenTable.ShahokokuhoModel shahokokuhoModel = (HokenTable.ShahokokuhoModel)model;
            String rep = ShahokokuhoUtil.rep(shahokokuhoModel.orig);
            if( GuiUtil.confirm("この保険情報を削除しますか？\n" + rep) ){
                Service.api.deleteShahokokuho(shahokokuhoModel.orig)
                        .thenAccept(ok -> {
                            if( ok ){
                                Platform.runLater(this::fetchAndUpdateHokenList);
                            }
                        })
                        .exceptionally(ex -> {
                            logger.error("Failed to delete shahokokuho.", ex);
                            Platform.runLater(() -> GuiUtil.alertException(ex));
                            return null;
                        });
            }
        }
        else if( model instanceof HokenTable.KoukikoureiModel ){
            HokenTable.KoukikoureiModel koukikoureiModel = (HokenTable.KoukikoureiModel)model;
            String rep = KoukikoureiUtil.rep(koukikoureiModel.orig);
            if( GuiUtil.confirm("この保険情報を削除しますか？\n" + rep) ){
                Service.api.deleteKoukikourei(koukikoureiModel.orig)
                        .thenAccept(ok -> {
                            if( ok ){
                                Platform.runLater(this::fetchAndUpdateHokenList);
                            }
                        })
                        .exceptionally(ex -> {
                            logger.error("Failed to delete koukikourei.", ex);
                            Platform.runLater(() -> GuiUtil.alertException(ex));
                            return null;
                        });
            }
        }
    }

}
