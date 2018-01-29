package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitPatientDTO;

import java.util.List;
import java.util.stream.Collectors;

public class RecentVisitsDialog extends Stage {

    public interface Callback {
        void onSelect(PatientDTO patient);
    }

    private PatientTable patientTable;
    private Callback callback;

    public RecentVisitsDialog(List<VisitPatientDTO> list){
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        {
            List<PatientDTO> patients = list.stream().map(vp -> vp.patient).collect(Collectors.toList());
            this.patientTable = createPatientTable(patients);
            patientTable.setOnMouseClicked(event -> {
                if( event.getButton().equals(MouseButton.PRIMARY) ){
                    if( event.getClickCount() == 2 ){
                        doSelect();
                    }
                }
            });
        }
        root.getChildren().addAll(
                patientTable,
                createButtons()
        );
        setScene(new Scene(root));
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private PatientTable createPatientTable(List<PatientDTO> patients){
        PatientTable table = new PatientTable();
        table.getItems().setAll(patients);
        return table;
    }

    private Node createButtons(){
        HBox hbox = new HBox(4);
        Button openButton = new Button("患者選択");
        Button closeButton = new Button("閉じる");
        openButton.setOnAction(event -> doSelect());
        closeButton.setOnAction(event -> close());
        hbox.getChildren().addAll(openButton, closeButton);
        return hbox;
    }

    private void doSelect(){
        PatientDTO patient = patientTable.getSelectionModel().getSelectedItem();
        if( patient != null ){
            if( callback != null ){
                close();
                callback.onSelect(patient);
            }
        }
    }

}
