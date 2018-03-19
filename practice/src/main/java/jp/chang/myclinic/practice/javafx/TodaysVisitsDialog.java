package jp.chang.myclinic.practice.javafx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TodaysVisitsDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(TodaysVisitsDialog.class);
    private PatientTable table = new PatientTable();

    public TodaysVisitsDialog(List<VisitPatientDTO> list) {
        setTitle("本日の診察");
        VBox root = new VBox(4);
        root.getStylesheets().add("css/Practice.css");
        root.getStyleClass().add("todays-visits-dialog");
        {
            List<PatientDTO> patients = list.stream().map(vp -> vp.patient).collect(Collectors.toList());
            table.getItems().setAll(patients);
            root.getChildren().add(table);
        }
        {
            HBox hbox = new HBox(4);
            Button selectButton = new Button("選択");
            Button closeButton = new Button("閉じる");
            selectButton.setOnAction(evt -> doSelect());
            closeButton.setOnAction(evt -> close());
            hbox.getChildren().addAll(selectButton, closeButton);
            root.getChildren().add(hbox);
        }
        table.setOnMouseClicked(event -> {
            if( event.getClickCount() == 2 ){
                doSelect();
            }
        });
        setScene(new Scene(root));
    }

    private void doSelect(){
        PatientDTO patient = table.getSelectionModel().getSelectedItem();
        if( patient != null ){
            PracticeLib.startPatient(patient);
        }
    }

}
