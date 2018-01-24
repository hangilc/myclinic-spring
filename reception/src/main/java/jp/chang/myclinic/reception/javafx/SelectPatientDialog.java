package jp.chang.myclinic.reception.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.List;

public class SelectPatientDialog extends Stage {

    private PatientTable patientTable;
    private PatientDTO selected;

    public SelectPatientDialog(String prompt, List<PatientDTO> patients){
        setTitle("患者選択");
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        patientTable = new PatientTable(patients);
        root.getChildren().addAll(
            new Label(prompt),
                patientTable,
                makeButtons()
        );
        setScene(new Scene(root));
    }

    public PatientDTO getSelection(){
        return selected;
    }

    private Node makeButtons(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        Button okButton = new Button("決定");
        Button cancelButton = new Button("キャンセル ");
        okButton.setOnAction(event -> doEnter());
        cancelButton.setOnAction(event -> close());
        hbox.getChildren().addAll(okButton, cancelButton);
        return hbox;
    }

    private void doEnter(){
        selected = patientTable.getSelectionModel().getSelectedItem().orig;
        close();
    }
}
