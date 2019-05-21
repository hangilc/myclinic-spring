package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.PracticeHelper;
import jp.chang.myclinic.practice.javafx.parts.searchbox.SimpleSearchBox;

public class SearchPatientDialog extends Stage {

    private PracticeHelper helper = PracticeHelper.getInstance();
    private SimpleSearchBox<PatientDTO> box;

    private static String converter(PatientDTO patient){
        return String.format("[%04d] %s%s", patient.patientId, patient.lastName, patient.firstName);
    }

    SearchPatientDialog() {
        setTitle("患者検索");
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        root.getStyleClass().add("search-patient-dialog");
        root.getChildren().addAll(
                createSearchBox()
        );
        setScene(new Scene(root));
    }

    public void simulateSetSearchText(String text){
        box.simulateSearchTextInsert(text);
    }

    public void simulateSearchButtonClick(){
        box.simulateSearchButtonClick();
    }

    private Node createSearchBox(){
        this.box =
                new SimpleSearchBox<>(helper::searchPatient, SearchPatientDialog::converter);
        box.setOnDoubleClickSelectCallback(patientDTO -> Platform.runLater(() -> {
            helper.startPatient(patientDTO);
            SearchPatientDialog.this.close();
        }));
        return box;
    }

}
