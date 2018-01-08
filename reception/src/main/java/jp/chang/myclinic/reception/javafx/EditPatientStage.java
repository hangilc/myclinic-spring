package jp.chang.myclinic.reception.javafx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class EditPatientStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditPatientStage.class);

    private Consumer<PatientDTO> dataProcessor = System.out::println;

    public EditPatientStage(PatientDTO patient){
        VBox root = new VBox(4);
        PatientForm patientForm = new PatientForm(patient);
        {
            root.getChildren().add(patientForm);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Button enterButton = new Button("入力");
            Button cancelButton = new Button("キャンセル");
            enterButton.setOnAction(event -> {
                PatientDTO data = new PatientDTO();
                List<String> errs = patientForm.save(data);
                if( errs.size() > 0 ){
                    GuiUtil.alertError(String.join("\n", errs));
                } else {
                    dataProcessor.accept(data);
                }
            });
            cancelButton.setOnAction(event -> close());
            hbox.getChildren().addAll(enterButton, cancelButton);
            root.getChildren().add(hbox);
        }
        root.setStyle("-fx-padding: 10");
        setScene(new Scene(root));
        sizeToScene();
    }

    public void setDataProcessor(Consumer<PatientDTO> processor){
        dataProcessor = processor;
    }
}
