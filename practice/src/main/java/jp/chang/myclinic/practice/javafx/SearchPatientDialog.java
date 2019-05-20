package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.PracticeHelper;
import jp.chang.myclinic.practice.javafx.parts.searchbox.SimpleSearchBox;
import org.intellij.lang.annotations.RegExp;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        box.simulateSetSearchText(text);
    }

    public void simulateSearchButtonClick(){
        box.simulateSearchButtonClick();
    }

    private Node createSearchBox(){
        this.box =
                new SimpleSearchBox<>(this::doSearch, SearchPatientDialog::converter);
        box.setOnDoubleClickSelectCallback(patientDTO -> Platform.runLater(() -> {
            helper.startPatient(patientDTO);
            SearchPatientDialog.this.close();
        }));
        return box;
    }

    private CompletableFuture<List<PatientDTO>> doSearch(String text){
        if( Pattern.matches("\\d+", text) ){
            int patientId = Integer.parseInt(text);
            return Context.frontend.getPatient(patientId)
                    .thenApply(patient -> {
                        if( patient == null ){
                            return Collections.emptyList();
                        } else {
                            return Collections.singletonList(patient);
                        }
                    });
        } else {
            return Context.frontend.searchPatient(text);
        }
    }


}
