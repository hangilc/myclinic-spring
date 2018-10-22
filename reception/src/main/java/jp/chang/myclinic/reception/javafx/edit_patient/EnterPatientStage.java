package jp.chang.myclinic.reception.javafx.edit_patient;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicValue;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.util.function.Consumer;

public class EnterPatientStage extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(EnterPatientStage.class);
    private Consumer<PatientDTO> onEnterCallback = dto -> {};

    public EnterPatientStage() {
        setTitle("新規患者入力");
        Parent root = createRoot();
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "enter-patient-stage");
        setScene(new Scene(root));
    }

    public void setOnEnterCallback(Consumer<PatientDTO> callback){
        this.onEnterCallback = callback;
    }

    private Parent createRoot(){
        VBox root = new VBox(4);
        PatientForm form = new PatientForm();
        root.getChildren().add(form);
        HBox commands = new HBox(4);
        commands.setAlignment(Pos.CENTER_RIGHT);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter(form));
        cancelButton.setOnAction(evt -> close());
        commands.getChildren().addAll(enterButton, cancelButton);
        root.getChildren().add(commands);
        return root;
    }

    private void doEnter(PatientForm form){
        PatientFormInputs inputs = form.getInputs();
        ErrorMessages em = new ErrorMessages();
        PatientDTO dto = new LogicValue<>(inputs)
                .convert(PatientFormLogic::patientFormInputsToPatientDTO)
                .getValue(null, em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
            return;
        }
        onEnterCallback.accept(dto);
    }

}
