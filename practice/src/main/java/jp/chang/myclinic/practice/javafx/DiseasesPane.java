package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.disease.*;
import jp.chang.myclinic.practice.javafx.events.CurrentDiseasesChangedEvent;
import jp.chang.myclinic.practice.javafx.events.DiseaseEnteredEvent;

import java.util.ArrayList;
import java.util.List;

public class DiseasesPane extends VBox {

    private StackPane workarea = new StackPane();
    private List<DiseaseFullDTO> currentDiseases = new ArrayList<>();
    private int patientId;

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
            clearWorkarea();
            this.patientId = (newValue == null) ? 0 : newValue.patientId;
            DiseasesPane.this.setVisible(newValue != null);
        });
        PracticeEnv.INSTANCE.currentDiseasesProperty().addListener((obs, oldValue, newValue) -> {
            currentDiseases = newValue;
            showCurrent();
        });
        addEventHandler(DiseaseEnteredEvent.eventType, this::onDiseaseEntered);
        addEventHandler(CurrentDiseasesChangedEvent.eventType, event -> {
            this.currentDiseases = event.getDiseases();
        });
    }

    private Node createTitle() {
        Label label = new Label("病名");
        label.getStyleClass().add("diseases-pane-title");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private void showCurrent(){
        Current current = new Current(currentDiseases){
            @Override
            protected void onMouseClick(DiseaseFullDTO disease) {
                showEdit(disease);
            }
        };
        setWorkarea(current);
    }

    private void showAdd(){
        assert patientId != 0;
        setWorkarea(new Add(patientId));
    }

    private void showEnd(){
        assert patientId != 0;
        setWorkarea(new End(currentDiseases, patientId));
    }

    private void showSelect(){
        assert patientId != 0;
        Service.api.listDiseaseFull(patientId)
                .thenAccept(list -> Platform.runLater(() ->{
                    Select selector = new Select(list);
                    selector.setOnSelectCallback(this::showEdit);
                    setWorkarea(selector);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void showEdit(DiseaseFullDTO disease){
        Edit edit = new Edit(disease){
            @Override
            protected void onComplete() {
                Service.api.listDiseaseFull(patientId)
                        .thenAccept(list -> Platform.runLater(() ->{
                            DiseasesPane.this.currentDiseases = list;
                            showCurrent();
                        }))
                        .exceptionally(HandlerFX::exceptionally);

            }
        };
        setWorkarea(edit);
    }

    private Node createControls(){
        HBox hbox = new HBox(4);
        Hyperlink listLink = new Hyperlink("現行");
        Hyperlink addLink = new Hyperlink("追加");
        Hyperlink endLink = new Hyperlink("転機");
        Hyperlink editLink = new Hyperlink("編集");
        listLink.setOnAction(evt -> showCurrent());
        addLink.setOnAction(evt -> showAdd());
        endLink.setOnAction(evt -> showEnd());
        editLink.setOnAction(evt -> showSelect());
        hbox.getChildren().addAll(listLink, addLink, endLink, editLink);
        return hbox;
    }

    private void setWorkarea(Node content){
        workarea.getChildren().setAll(content);
    }

    private void clearWorkarea(){
        workarea.getChildren().clear();
    }

    private void onDiseaseEntered(DiseaseEnteredEvent event){
        DiseaseFullDTO entered = event.getDisease();
        currentDiseases.add(entered);
    }

}
