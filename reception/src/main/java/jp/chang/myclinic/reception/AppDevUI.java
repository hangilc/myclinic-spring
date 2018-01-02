package jp.chang.myclinic.reception;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.javafx.MainPane;
import jp.chang.myclinic.reception.javafx.PatientForm;

import java.util.List;

public class AppDevUI extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox vbox = new VBox();
        Button testButton = new Button("テスト");
        vbox.getChildren().addAll(mainPane(testButton), testButton);
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private static Pane devDateInput(Button testButton) {
        return new jp.chang.myclinic.reception.javafx.DateInput();
    }

    private static Pane patientForm(Button testButton) {
        PatientForm patientForm =  new PatientForm();
        testButton.setOnAction(event -> {
            PatientDTO patient = new PatientDTO();
            List<String> errs = patientForm.save(patient);
            if( errs.size() > 0 ){
                System.out.println(errs);
            } else {
                System.out.println(patient);
            }
        });
        return patientForm;
    }

    private static Pane mainPane(Button testButton){
        MainPane pane = new MainPane();
        return pane;
    }


}
