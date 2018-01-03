package jp.chang.myclinic.reception.javafx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.PatientDTO;

public class PatientWithHoken extends VBox {

    private PatientInfo patientInfo = new PatientInfo();
    private Button editPatientButton = new Button("編集");

    public PatientWithHoken(){
        {
            VBox vbox = new VBox(4);
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.getChildren().add(editPatientButton);
            vbox.getChildren().addAll(patientInfo, hbox);
            TitledPane titledPane = new TitledPane("基本情報", vbox);
            titledPane.setCollapsible(false);
            getChildren().add(titledPane);
        }
    }

    public void setPatient(PatientDTO patient){
        patientInfo.setPatient(patient);
    }
}
