package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.disease.*;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiseasesPane extends VBox {

    private StackPane workarea = new StackPane();
    private List<DiseaseFullDTO> currentDiseases = new ArrayList<>();
    private int patientId;

    DiseasesPane() {
        super(4);
        getStyleClass().add("diseases-pane");
        setFillWidth(true);
        getChildren().addAll(
                createTitle(),
                workarea,
                createControls()
        );
        setVisible(false);
        PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) -> {
            clearWorkarea();
            currentDiseases = Collections.emptyList();
            if( newValue == null ){
                setVisible(false);
                this.patientId = 0;
            } else {
                this.patientId = newValue.patientId;
                Service.api.listCurrentDiseaseFull(patientId)
                        .thenAccept(result -> Platform.runLater(() -> {
                            this.currentDiseases = result;
                            showCurrent();
                            setVisible(true);
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }
        });
    }

    private void setCurrentDiseases(List<DiseaseFullDTO> diseases){
        this.currentDiseases = diseases;
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
            protected void onSelect(DiseaseFullDTO disease) {
                showEdit(disease);
            }
        };
        ScrollPane scroll = new ScrollPane(current);
        scroll.getStyleClass().add("current-diseases-scroll");
        scroll.prefViewportHeightProperty().bind(current.heightProperty());
        scroll.setFitToWidth(true);
        setWorkarea(scroll);
    }

    private void showAdd(){
        assert patientId != 0;
        Add add = new Add(patientId){
            @Override
            protected void onEntered(DiseaseFullDTO entered) {
               appendDisease(entered);
            }
        };
        setWorkarea(add);
    }

    private void appendDisease(DiseaseFullDTO disease){
        currentDiseases.add(disease);
    }

    private void showEnd(){
        assert patientId != 0;
        End endPane = new End(currentDiseases, patientId){
            @Override
            protected void onModified(List<DiseaseFullDTO> newCurrentDiseases) {
                setCurrentDiseases(newCurrentDiseases);
            }
        };
        setWorkarea(endPane);
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
                Service.api.listCurrentDiseaseFull(patientId)
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

}
