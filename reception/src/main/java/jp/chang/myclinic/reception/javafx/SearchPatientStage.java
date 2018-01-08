package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.Service;
import jp.chang.myclinic.reception.model.PatientModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class SearchPatientStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(SearchPatientStage.class);

    private StringProperty searchText = new SimpleStringProperty();
    private ObjectProperty<ObservableList<PatientModel>> searchResult =
            new SimpleObjectProperty<>(FXCollections.emptyObservableList());
    private ObjectProperty<PatientModel> selectedItem = new SimpleObjectProperty<>();

    public SearchPatientStage(){
        VBox root = new VBox(4);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            {
                TextField searchTextInput = new TextField();
                searchTextInput.textProperty().bindBidirectional(searchText);
                searchTextInput.setOnAction(event -> doSearch());
                Button searchButton = new Button("検索");
                Button recentlyRegisteredButton = new Button("最近の登録");
                searchButton.setOnAction(event -> doSearch());
                hbox.getChildren().addAll(searchTextInput, searchButton, recentlyRegisteredButton);
            }
            root.getChildren().add(hbox);
        }
        {
            PatientTable tableView = new PatientTable();
            tableView.itemsProperty().bindBidirectional(searchResult);
            selectedItem.bind(tableView.getSelectionModel().selectedItemProperty());
            tableView.setPrefWidth(425);
            tableView.setPrefHeight(250);
            root.getChildren().add(tableView);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.TOP_LEFT);
            PatientInfo infoLabel = new PatientInfo();
            infoLabel.modelProperty().bindBidirectional(selectedItem);
            infoLabel.setPrefWidth(314);
            infoLabel.setPrefHeight(182);
            VBox commandBox = new VBox(4);
            commandBox.setAlignment(Pos.TOP_CENTER);
            commandBox.setFillWidth(true);
            Button editButton = new Button("編集");
            editButton.setMaxWidth(300);
            Button registerButton = new Button("診療受付");
            registerButton.setMaxWidth(300);
            editButton.setDisable(true);
            registerButton.setDisable(true);
            selectedItem.addListener((obs, oldValue, newValue) -> {
                boolean disable = newValue == null;
                editButton.setDisable(disable);
                registerButton.setDisable(disable);
            });
            editButton.setOnAction(event -> onEdit());
            registerButton.setOnAction(event -> onRegister());
            commandBox.getChildren().addAll(editButton, registerButton);
            hbox.getChildren().addAll(infoLabel, commandBox);
            HBox.setHgrow(infoLabel, Priority.ALWAYS);
            root.getChildren().add(hbox);
        }
        root.setStyle("-fx-padding: 10");
        setScene(new Scene(root));
        sizeToScene();
    }

    private void onEdit() {
        PatientModel model = selectedItem.getValue();
        if( model != null ){
            PatientDTO patient = model.orig;
            Service.api.listHoken(patient.patientId)
                    .thenAccept(list -> {
                        Platform.runLater(() -> {
                            PatientWithHokenStage stage = new PatientWithHokenStage(patient, list);
                            stage.show();
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("Failed to list hoken.", ex);
                        Platform.runLater(() -> GuiUtil.alertException(ex));
                        return null;
                    });
        }
    }

    private void onRegister() {

    }

    private void doSearch() {
        String text = searchText.getValue();
        if( text == null || text.isEmpty() ){
            return;
        }
        Service.api.searchPatient(text)
                .thenAccept(list -> {
                    Platform.runLater(() -> {
                        List<PatientModel> models = list.stream()
                                .map(PatientModel::fromPatient)
                                .collect(Collectors.toList());
                        searchResult.setValue(FXCollections.observableArrayList(models));
                    });
                })
                .exceptionally(ex -> {
                    logger.error("Search patient failed.", ex);
                    Platform.runLater(() -> GuiUtil.alertException(ex));
                    return null;
                });
    }
}
