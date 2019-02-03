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
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.HokenListDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.javafx.edit_kouhi.EditKouhiStage;
import jp.chang.myclinic.reception.javafx.edit_kouhi.EnterKouhiStage;
import jp.chang.myclinic.reception.javafx.edit_koukikourei.EditKoukikoureiStage;
import jp.chang.myclinic.reception.javafx.edit_koukikourei.EnterKoukikoureiStage;
import jp.chang.myclinic.reception.javafx.edit_shahokokuho.EditShahokokuhoStage;
import jp.chang.myclinic.reception.javafx.edit_shahokokuho.EnterShahokokuhoStage;
import jp.chang.myclinic.reception.javafx.edit_patient.EditPatientStage;
import jp.chang.myclinic.util.*;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PatientWithHokenStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(PatientWithHokenStage.class);
    private static Pattern scannedDateTimePattern = Pattern.compile(
            "(\\d{8}+)-(\\d{6}+)-\\d+\\.[^.]+$"
    ); // (yyyymmdd)-(hhmmss)

    private ObjectProperty<PatientDTO> thePatient = new SimpleObjectProperty<>();
    private BooleanProperty currentActiveOnly = new SimpleBooleanProperty(true);
    private HokenListDTO hokenList;
    private ObjectProperty<ObservableList<HokenTable.Model>> tableModels =
            new SimpleObjectProperty<>(FXCollections.emptyObservableList());
    private ObjectProperty<HokenTable.Model> tableSelection = new SimpleObjectProperty<>();
    private Button newShahokokuhoButton;
    private Button newKoukikoureiButton;
    private Button newKouhiButton;

    PatientWithHokenStage(PatientDTO patient, HokenListDTO hokenList) {
        setTitle("患者情報編集（" + patient.patientId + ")");
        thePatient.setValue(patient);
        this.hokenList = hokenList;
        HokenTable hokenTable = new HokenTable();
        VBox root = new VBox(4);
        root.getStylesheets().add("/css/Main.css");
        root.getStyleClass().add("dialog-root");
        root.setFillWidth(true);
        {
            VBox vbox = new VBox(4);
            vbox.setMaxWidth(360);
            PatientInfo patientInfo = new PatientInfo(patient);
            thePatient.addListener((obs, oldValue, newValue) -> {
                patientInfo.setPatient(newValue);
            });
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Button editPatientButton = new Button("編集");
            editPatientButton.setOnAction(event -> {
                EditPatientStage editStage = new EditPatientStage(thePatient.getValue());
                editStage.setOnEnterCallback(edited -> {
                    Service.api.updatePatient(edited)
                            .thenAccept(ok -> {
                                Platform.runLater(() -> {
                                    editStage.close();
                                    thePatient.setValue(edited);
                                });
                            })
                            .exceptionally(ex -> {
                                logger.error("Failed to update patient.", ex);
                                Platform.runLater(() -> GuiUtil.alertException("Internal error.", ex));
                                return null;
                            });
                });
                editStage.showAndWait();
            });
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
                    editButton.setOnAction(event -> doEdit(hokenTable));
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
                row.setAlignment(Pos.CENTER_LEFT);
                this.newShahokokuhoButton = new Button("新規社保国保");
                this.newKoukikoureiButton = new Button("新規後期高齢");
                this.newKouhiButton = new Button("新規公費負担");
                Hyperlink hokenshoLink = new Hyperlink("保険証画像");
                newShahokokuhoButton.setOnAction(event -> doNewShahokokuho());
                newKoukikoureiButton.setOnAction(event -> doNewKoukikourei());
                newKouhiButton.setOnAction(event -> doNewKouhi());
                hokenshoLink.setOnAction(event -> doHokenshoImage());
                row.getChildren().addAll(
                        newShahokokuhoButton,
                        newKoukikoureiButton,
                        newKouhiButton,
                        hokenshoLink);
                vbox.getChildren().add(row);
            }
            TitledPane titledPane = new TitledPane("保険情報", vbox);
            titledPane.setCollapsible(false);
            root.getChildren().add(titledPane);
        }
        {
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_RIGHT);
            Button registerButton = new Button("診療受付");
            Button closeButton = new Button("閉じる");
            registerButton.setOnAction(event -> doRegister());
            closeButton.setOnAction(event -> close());
            row.getChildren().addAll(registerButton, closeButton);
            root.getChildren().add(row);
        }
        Scene scene = new Scene(root, 500, 660);
        setScene(scene);
        sizeToScene();
    }

    public void simulateNewShahokokuhoButtonClick(){
        newShahokokuhoButton.fire();
    }

    public void simulateNewKoukikoureiButtonClick(){
        newKoukikoureiButton.fire();
    }

    public void simulateNewKouhiButtonClick(){
        newKouhiButton.fire();
    }

    private void doHokenshoImage() {
        final int patientId = thePatient.getValue().patientId;
        Service.api.listHokensho(patientId)
                .thenAccept(files -> Platform.runLater(() -> {
                    List<HokenshoListStage.Model> models = files.stream()
                            .map(file -> {
                                String label = extractHokenshoDateTime(file);
                                if (label == null) {
                                    label = file;
                                }
                                return new HokenshoListStage.Model(label, file);
                            })
                            .collect(Collectors.toList());
                    HokenshoListStage stage = new HokenshoListStage(patientId, models);
                    stage.initOwner(PatientWithHokenStage.this);
                    stage.show();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private String extractHokenshoDateTime(String file) {
        Matcher matcher = scannedDateTimePattern.matcher(file);
        if (matcher.find()) {
            String datePart = matcher.group(1);
            String timePart = matcher.group(2);
            String dt = String.format("%s-%s-%sT%s:%s:%s",
                    datePart.substring(0, 4), datePart.substring(4, 6), datePart.substring(6, 8),
                    timePart.substring(0, 2), timePart.substring(2, 4), timePart.substring(4, 6));
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dt);
                return DateTimeUtil.toKanji(dateTime,
                        DateTimeUtil.kanjiFormatter2,
                        DateTimeUtil.kanjiFormatter4,
                        " "
                );
            } catch (DateTimeParseException ex) {
                logger.error("Invalid scanner file: {}", file);
                return null;
            }
        } else {
            return null;
        }
    }

    private void doEdit(HokenTable hokenTable) {
        HokenTable.Model model = hokenTable.getSelectionModel().getSelectedItem();
        if (model == null) {
            return;
        }
        if (model instanceof HokenTable.ShahokokuhoModel) {
            HokenTable.ShahokokuhoModel shahoModel = (HokenTable.ShahokokuhoModel) model;
            EditShahokokuhoStage editor = new EditShahokokuhoStage(shahoModel.orig, this::fetchAndUpdateHokenList);
            editor.showAndWait();
        } else if (model instanceof HokenTable.KoukikoureiModel) {
            HokenTable.KoukikoureiModel koukiModel = (HokenTable.KoukikoureiModel) model;
            EditKoukikoureiStage editor = new EditKoukikoureiStage(koukiModel.orig);
            editor.setEnterCallback(data -> {
                Service.api.updateKoukikourei(data)
                        .thenAccept(ok -> Platform.runLater(() -> {
                            fetchAndUpdateHokenList();
                            editor.close();
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            });
            editor.showAndWait();
        } else if (model instanceof HokenTable.KouhiModel) {
            HokenTable.KouhiModel kouhiModel = (HokenTable.KouhiModel) model;
            EditKouhiStage editor = new EditKouhiStage(kouhiModel.orig);
            editor.setCallback(dto -> {
                Service.api.updateKouhi(dto)
                        .thenAccept(ok -> Platform.runLater(() -> {
                            fetchAndUpdateHokenList();
                            editor.close();
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            });
            editor.showAndWait();
        } else if (model instanceof HokenTable.RoujinModel ){
            GuiUtil.alertError("老人保険は編集できません。");
        } else {
            GuiUtil.alertError("Unknown hokentable model.");
        }
    }

    public ObjectProperty<PatientDTO> patientProperty() {
        return thePatient;
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
        PatientDTO patient = thePatient.getValue();
        if (patient != null) {
            EnterShahokokuhoStage stage = new EnterShahokokuhoStage(patient.patientId, this::fetchAndUpdateHokenList);
            stage.showAndWait();
        } else {
            logger.error("Null patient in doNewShahokokuho().");
        }
    }

    private void doNewKoukikourei() {
        EnterKoukikoureiStage stage = new EnterKoukikoureiStage(thePatient.getValue().patientId);
        stage.setEnterCallback(dto -> {
            Service.api.enterKoukikourei(dto)
                    .thenAcceptAsync(koukikoureiId -> {
                        fetchAndUpdateHokenList();
                        stage.close();
                    }, Platform::runLater)
                    .exceptionally(ex -> {
                        logger.error("Failed to enter koukikourei.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("後期高齢保険の新規登録に失敗しました。", ex));
                        return null;
                    });
        });
        stage.showAndWait();
    }

    private void doNewKouhi() {
        EnterKouhiStage stage = new EnterKouhiStage(thePatient.getValue().patientId);
        stage.setCallback(dto -> {
                Service.api.enterKouhi(dto)
                        .thenAccept(kouhiId -> {
                            Platform.runLater(() -> {
                                fetchAndUpdateHokenList();
                                stage.close();
                            });
                        })
                        .exceptionally(ex -> {
                            logger.error("Failed to enter kouhi.", ex);
                            Platform.runLater(() -> GuiUtil.alertException("公費負担の新規登録に失敗しました。", ex));
                            return null;
                        });
        });
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

    private ObservableList<HokenTable.Model> composeTableModels() {
        List<HokenTable.Model> models = hokenListToModelList(currentActiveOnly.get() ? currentActiveHokenList() : hokenList);
        return FXCollections.observableArrayList(models);
    }

    private void updateHokenTable() {
        tableModels.setValue(composeTableModels());
    }

    private void fetchAndUpdateHokenList() {
        Service.api.listHoken(thePatient.getValue().patientId)
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

    private void doDeleteHoken(HokenTable.Model model) {
        if (model instanceof HokenTable.ShahokokuhoModel) {
            HokenTable.ShahokokuhoModel shahokokuhoModel = (HokenTable.ShahokokuhoModel) model;
            String rep = ShahokokuhoUtil.rep(shahokokuhoModel.orig);
            if (GuiUtil.confirm("この保険情報を削除しますか？\n" + rep)) {
                Service.api.deleteShahokokuho(shahokokuhoModel.orig)
                        .thenAccept(ok -> {
                            if (ok) {
                                Platform.runLater(this::fetchAndUpdateHokenList);
                            }
                        })
                        .exceptionally(ex -> {
                            logger.error("Failed to delete shahokokuho.", ex);
                            Platform.runLater(() -> GuiUtil.alertException("Internal error.", ex));
                            return null;
                        });
            }
        } else if (model instanceof HokenTable.KoukikoureiModel) {
            HokenTable.KoukikoureiModel koukikoureiModel = (HokenTable.KoukikoureiModel) model;
            String rep = KoukikoureiUtil.rep(koukikoureiModel.orig);
            if (GuiUtil.confirm("この保険情報を削除しますか？\n" + rep)) {
                Service.api.deleteKoukikourei(koukikoureiModel.orig)
                        .thenAccept(ok -> {
                            if (ok) {
                                Platform.runLater(this::fetchAndUpdateHokenList);
                            }
                        })
                        .exceptionally(ex -> {
                            logger.error("Failed to delete koukikourei.", ex);
                            Platform.runLater(() -> GuiUtil.alertException("Internal error.", ex));
                            return null;
                        });
            }
        } else if (model instanceof HokenTable.RoujinModel) {
            HokenTable.RoujinModel roujinModel = (HokenTable.RoujinModel) model;
            String rep = RoujinUtil.rep(roujinModel.orig);
            if (GuiUtil.confirm("この保険情報を削除しますか？\n" + rep)) {
                Service.api.deleteRoujin(roujinModel.orig)
                        .thenAccept(ok -> {
                            if (ok) {
                                Platform.runLater(this::fetchAndUpdateHokenList);
                            }
                        })
                        .exceptionally(ex -> {
                            logger.error("Failed to delete roujin.", ex);
                            Platform.runLater(() -> GuiUtil.alertException("Internal error.", ex));
                            return null;
                        });
            }
        } else if (model instanceof HokenTable.KouhiModel) {
            HokenTable.KouhiModel kouhiModel = (HokenTable.KouhiModel) model;
            String rep = KouhiUtil.rep(kouhiModel.orig);
            if (GuiUtil.confirm("この保険情報を削除しますか？\n" + rep)) {
                Service.api.deleteKouhi(kouhiModel.orig)
                        .thenAccept(ok -> {
                            if (ok) {
                                Platform.runLater(this::fetchAndUpdateHokenList);
                            }
                        })
                        .exceptionally(ex -> {
                            logger.error("Failed to delete kouhi.", ex);
                            Platform.runLater(() -> GuiUtil.alertException("Internal error.", ex));
                            return null;
                        });
            }
        }
    }

    private void doRegister() {
        System.out.println("enter doRegister");
        PatientDTO patient = thePatient.getValue();
        System.out.println("register patient: " + patient);
        if (patient == null) {
            return;
        }
        RegisterForPracticeDialog confirmStage = new RegisterForPracticeDialog(patient);
        confirmStage.showAndWait();
        if (confirmStage.isOk()) {
            ReceptionService.startVisit(thePatient.getValue().patientId);
        }
    }

}
