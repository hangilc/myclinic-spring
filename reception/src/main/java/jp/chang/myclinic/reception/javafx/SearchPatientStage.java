package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchPatientStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(SearchPatientStage.class);

    private TextField searchTextInput = new TextField();
    private PatientTable tableView = new PatientTable();

    public SearchPatientStage(){
        VBox root = new VBox(4);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            {
                searchTextInput.setOnAction(event -> doSearch());
                Button searchButton = new Button("検索");
                Button recentlyRegisteredButton = new Button("最近の登録");
                searchButton.setOnAction(event -> doSearch());
                recentlyRegisteredButton.setOnAction(event -> doRecent());
                hbox.getChildren().addAll(searchTextInput, searchButton, recentlyRegisteredButton);
            }
            root.getChildren().add(hbox);
        }
        {
            tableView.setPrefWidth(425);
            tableView.setPrefHeight(250);
            root.getChildren().add(tableView);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.TOP_LEFT);
            PatientInfo infoLabel = new PatientInfo();
            tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                if( newValue == null ){
                    infoLabel.clear();
                } else {
                    infoLabel.setPatient(newValue.orig);
                }
            });
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
            tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
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

    private void doRecent() {
        Service.api.listRecentlyRegisteredPatients()
                .thenAccept(this::setSearchResult)
                .exceptionally(ex -> {
                    logger.error("Listing recently registered patient failed.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("Internal error.", ex));
                    return null;
                });
    }

    private void onEdit() {
        PatientTable.Model tableModel = tableView.getSelectionModel().getSelectedItem();
        if( tableModel != null && tableModel.orig != null ){
            PatientDTO patient = tableModel.orig;
            Service.api.listHoken(patient.patientId)
                    .thenAccept(list -> {
                        Platform.runLater(() -> {
                            PatientWithHokenStage stage = new PatientWithHokenStage(patient, list);
                            stage.patientProperty().addListener((obs, oldValue, newValue) -> {
                                tableView.updateData(newValue);
                           });
                            stage.show();
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("Failed to list hoken.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("Internal error.", ex));
                        return null;
                    });
        }
    }

    private void onRegister() {
        PatientTable.Model model = tableView.getSelectionModel().getSelectedItem();
        if( model != null ){
            PatientDTO patient = model.orig;
            RegisterForPracticeDialog confirm = new RegisterForPracticeDialog(patient);
            confirm.showAndWait();
            if( confirm.isOk() ){
                ReceptionService.startVisit(patient.patientId);
            }
        }
    }

    private void setSearchResult(List<PatientDTO> list){
        tableView.setList(list);
//        List<PatientTable.Model> models = list.stream()
//                .map(PatientTable.Model::fromPatient)
//                .collect(Collectors.toList());
//        tableView.itemsProperty().setValue(FXCollections.observableArrayList(models));
    }

    private void doSearch() {
        String text = searchTextInput.getText();
        if( text == null || text.isEmpty() ){
            return;
        }
        Service.api.searchPatient(text)
                .thenAccept(list -> {
                    Platform.runLater(() -> {
                        searchTextInput.setText("");
                        setSearchResult(list);
                    });
                })
                .exceptionally(ex -> {
                    logger.error("Search patient failed.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("Internal error.", ex));
                    return null;
                });
    }
}
