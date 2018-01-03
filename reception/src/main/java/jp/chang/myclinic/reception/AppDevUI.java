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
import jp.chang.myclinic.reception.javafx.PatientInfo;

import java.util.List;

public class AppDevUI extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox vbox = new VBox();
        Button testButton = new Button("テスト");
        vbox.getChildren().addAll(hokenTable(testButton), testButton);
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

    private static Pane patientInfoPane(Button testButton){
        PatientInfo patientInfo = new PatientInfo();
        PatientDTO patient = new PatientDTO();
        patient.lastName = "田中";
        patient.firstName = "太郎";
        patient.lastNameYomi = "たなか";
        patient.firstNameYomi = "たろう";
        patient.birthday = "1957-06-02";
        patient.sex = "M";
        patient.address = "東京都東京都東京都東京都東京都東京都東京都東京都東京都東京都";
        patient.phone = "03-1234-5678";
        patientInfo.setPatient(patient);
        return patientInfo;
    }

//    private static Pane patientWithHokenPane(Button testButton){
//        PatientDTO patient = new PatientDTO();
//        patient.lastName = "田中";
//        patient.firstName = "太郎";
//        patient.lastNameYomi = "たなか";
//        patient.firstNameYomi = "たろう";
//        patient.birthday = "1957-06-02";
//        patient.sex = "M";
//        patient.address = "東京都東京都東京都東京都東京都東京都東京都東京都東京都東京都";
//        patient.phone = "03-1234-5678";
//        return new PatientWithHokenStage(patient, new HokenListDTO());
//    }

    private static Pane hokenTable(Button testButton){
//        HokenTable table = new HokenTable();
//        HokenTable.Model model1 = new HokenTable.Model();
//        model1.setName("国保");
//        model1.setValidFrom("平成13年04月01日");
//        model1.setValidUpto("平成14年03月31日");
//        model1.setHonninKazoku("本人");
//        model1.setFutanWari("2割");
//        HokenTable.Model model2 = new HokenTable.Model();
//        model2.setName("組合保険");
//        model2.setValidFrom("平成13年04月01日");
//        model2.setValidUpto("平成14年03月31日");
//        model2.setHonninKazoku("家族");
//        model2.setFutanWari("");
//        table.getItems().addAll(model1, model2);
//        VBox vbox = new VBox();
//        vbox.getChildren().add(table);
//        return vbox;
        return new VBox(4);
    }

}
