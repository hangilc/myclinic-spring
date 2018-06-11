package jp.chang.myclinic.recordbrowser;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SearchByoumeiDialog extends Stage {

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

    private Node createInput(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        TextField inputField = new TextField();
        inputField.getStyleClass().add("search-text-input");
        Button searchButton = new Button("検索");
        inputField.setOnAction(evt -> doSearch(inputField.getText()));
        searchButton.setOnAction(evt -> doSearch(inputField.getText()));
        hbox.getChildren().addAll(
                new Label("患者番号："),
                inputField,
                searchButton
        );
        return hbox;
    }

    private void doSearch(String text){
        try {
            int patientId = Integer.parseInt(text);
            Service.api.getPatient(patientId)
                    .thenCompose(patientDTO -> {
                        this.currentPatient = patientDTO;
                        return Service.api.listCurrentDiseaseFull(patientId);
                    })
                    .thenAccept(diseases -> {
                        updatePatientLabel();
                        diseaseTable.setRows(diseases);
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } catch(NumberFormatException ex){
            GuiUtil.alertError("患者番号の入力が不適切です。");
        }
    }

    private void updatePatientLabel(){
        String s = "";
        if( currentPatient != null ) {
            s = String.format("(%d) %s %s", currentPatient.patientId,
                    currentPatient.lastName, currentPatient.firstName);
        }
        patientLabel.setText(s);
    }

}
