package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class SearchByoumeiDialog extends Stage {

    private enum SearchMode {
        Current,
        All
    }

    private static Logger logger = LoggerFactory.getLogger(SearchByoumeiDialog.class);
    private PatientDTO currentPatient;
    private Text patientLabel = new Text();
    private DiseaseTable diseaseTable = new DiseaseTable();

    SearchByoumeiDialog() {
        setTitle("病名検索");
        VBox root = new VBox(4);
        root.getStyleClass().addAll("dialog", "search-byoumei-dialog");
        root.getStylesheets().add("Main.css");
        diseaseTable.getStyleClass().add("disease-table");
        root.getChildren().addAll(
                createInput(),
                new TextFlow(patientLabel),
                diseaseTable
        );
        setScene(new Scene(root));
    }

    private Node createInput() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        TextField inputField = new TextField();
        inputField.getStyleClass().add("search-text-input");
        Button searchButton = new Button("検索");
        RadioButtonGroup<SearchMode> group = new RadioButtonGroup<>();
        RadioButton currentRadio = group.createRadioButton("継続のみ", SearchMode.Current);
        RadioButton allRadio = group.createRadioButton("過去も", SearchMode.All);
        group.setValue(SearchMode.Current);
        group.valueProperty().addListener((obs, oldValue, newValue) -> {
            if( currentPatient!= null ){
                search(currentPatient.patientId, newValue);
            }
        });
        inputField.setOnAction(evt -> doSearch(inputField.getText(), group.getValue()));
        searchButton.setOnAction(evt -> doSearch(inputField.getText(), group.getValue()));
        hbox.getChildren().addAll(
                new Label("患者番号："),
                inputField,
                searchButton,
                currentRadio,
                allRadio
        );
        return hbox;
    }

    private void search(int patientId, SearchMode searchMode) {
        CompletableFuture<List<DiseaseFullDTO>> diseases;
        if (searchMode == SearchMode.Current) {
            diseases = Service.api.listCurrentDiseaseFull(patientId);
        } else if (searchMode == SearchMode.All) {
            diseases = Service.api.listDiseaseFull(patientId);
        } else {
            diseases = CompletableFuture.completedFuture(Collections.emptyList());
        }
        diseases.thenAccept(result -> Platform.runLater(() -> diseaseTable.setRows(result)))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doSearch(String text, SearchMode searchMode) {
        try {
            int patientId = Integer.parseInt(text);
            Service.api.getPatient(patientId)
                    .thenAccept(patientDTO -> Platform.runLater(() -> {
                        this.currentPatient = patientDTO;
                        updatePatientLabel();
                        search(patientId, searchMode);
                    }));
        } catch (NumberFormatException ex) {
            GuiUtil.alertError("患者番号の入力が不適切です。");
        }
    }

    private void updatePatientLabel() {
        String s = "";
        if (currentPatient != null) {
            s = String.format("(%d) %s %s", currentPatient.patientId,
                    currentPatient.lastName, currentPatient.firstName);
        }
        patientLabel.setText(s);
    }

}
