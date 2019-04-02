package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.searchbox.SimpleSearchBox;
import jp.chang.myclinic.practice.lib.PracticeLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchPatientDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(SearchPatientDialog.class);

    private static String converter(PatientDTO patient){
        return String.format("[%04d] %s%s", patient.patientId, patient.lastName, patient.firstName);
    }

    public SearchPatientDialog() {
        setTitle("患者検索");
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        root.getStyleClass().add("search-patient-dialog");
        root.getChildren().addAll(
                createSearchBox()
        );
        setScene(new Scene(root));
    }

    private Node createSearchBox(){
        SimpleSearchBox<PatientDTO> box = new SimpleSearchBox<>(Context.getInstance().getFrontend()::searchPatient, SearchPatientDialog::converter);
        box.setOnDoubleClickSelectCallback(patientDTO -> Platform.runLater(() -> {
            PracticeLib.startPatient(patientDTO);
            SearchPatientDialog.this.close();
        }));
        return box;
    }


}
