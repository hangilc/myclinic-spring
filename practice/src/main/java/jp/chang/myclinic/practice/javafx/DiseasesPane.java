package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.disease.Add;
import jp.chang.myclinic.practice.javafx.disease.Current;
import jp.chang.myclinic.practice.javafx.events.DiseaseEnteredEvent;

import java.util.Collections;
import java.util.List;

public class DiseasesPane extends VBox {

    private StackPane workarea = new StackPane();
    private List<DiseaseFullDTO> currentDiseases = Collections.emptyList();

    DiseasesPane() {
        super(4);
        getStyleClass().add("diseases-pane");
        setFillWidth(true);
        getStyleClass().add("diseases-pane");
        getChildren().addAll(
                createTitle(),
                workarea,
                createControls()
        );
        setVisible(false);
        PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) -> {
            DiseasesPane.this.setVisible(newValue != null);
        });
        PracticeEnv.INSTANCE.currentDiseasesProperty().addListener((obs, oldValue, newValue) -> {
            currentDiseases = newValue;
            showCurrent();
        });
        addEventHandler(DiseaseEnteredEvent.eventType, this::onDiseaseEntered);
    }

    private Node createTitle() {
        Label label = new Label("病名");
        label.getStyleClass().add("diseases-pane-title");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private void showCurrent(){
        setWorkarea(new Current(currentDiseases));
    }

    private void showAdd(){
        PatientDTO currentPatient = PracticeEnv.INSTANCE.getCurrentPatient();
        if( currentPatient != null ) {
            setWorkarea(new Add(currentPatient.patientId));
        }
    }

    private Node createControls(){
        HBox hbox = new HBox(4);
        Hyperlink listLink = new Hyperlink("現行");
        Hyperlink addLink = new Hyperlink("追加");
        Hyperlink endLink = new Hyperlink("転機");
        Hyperlink editLink = new Hyperlink("編集");
        listLink.setOnAction(evt -> showCurrent());
        addLink.setOnAction(evt -> showAdd());
        hbox.getChildren().addAll(listLink, addLink, endLink, editLink);
        return hbox;
    }

    private void setWorkarea(Node content){
        workarea.getChildren().setAll(content);
    }

    private void onDiseaseEntered(DiseaseEnteredEvent event){
        DiseaseFullDTO entered = event.getDisease();
        currentDiseases.add(entered);
    }

}
