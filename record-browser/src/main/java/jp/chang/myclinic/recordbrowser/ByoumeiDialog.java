package jp.chang.myclinic.recordbrowser;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.RadioButtonGroup;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class ByoumeiDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(ByoumeiDialog.class);
    enum SearchMode {
        Current,
        All
    }
    private DiseaseTable diseaseTable = new DiseaseTable();

    ByoumeiDialog(PatientDTO patient, List<DiseaseFullDTO> diseases, SearchMode searchMode) {
        setTitle(String.format("病名（%s%s）", patient.lastName, patient.firstName));
        VBox root = new VBox(4);
        root.getStyleClass().addAll("dialog", "byoumei-dialog");
        root.getStylesheets().add("Main.css");
        diseaseTable.setRows(diseases);
        root.getChildren().addAll(
                createPatientInfo(patient),
                createSearchModeSwitch(patient.patientId, searchMode),
                diseaseTable
        );
        setScene(new Scene(root));
    }

    private Node createPatientInfo(PatientDTO patient){
        String s = String.format("(%d) %s %s", patient.patientId,
                patient.lastName, patient.firstName);
        return new TextFlow(new Text(s));
    }

    private Node createSearchModeSwitch(int patientId, SearchMode initial){
        HBox hbox = new HBox(4);
        RadioButtonGroup<SearchMode> searchModeGroup = new RadioButtonGroup<>();
        RadioButton currentRadio = searchModeGroup.createRadioButton("継続のみ", SearchMode.Current);
        RadioButton allRadio = searchModeGroup.createRadioButton("過去も", SearchMode.All);
        searchModeGroup.setValue(initial);
        searchModeGroup.valueProperty().addListener((obs, oldValue, newValue) -> {
            CompletableFuture<List<DiseaseFullDTO>> diseases;
            if (newValue == SearchMode.Current) {
                diseases = Service.api.listCurrentDiseaseFull(patientId);
            } else if (newValue == SearchMode.All) {
                diseases = Service.api.listDiseaseFull(patientId);
            } else {
                diseases = CompletableFuture.completedFuture(Collections.emptyList());
            }
            diseases.thenAccept(diseaseTable::setRows)
                    .exceptionally(HandlerFX::exceptionally);

        });
        hbox.getChildren().addAll(
                currentRadio,
                allRadio
        );
        return hbox;
    }

}
