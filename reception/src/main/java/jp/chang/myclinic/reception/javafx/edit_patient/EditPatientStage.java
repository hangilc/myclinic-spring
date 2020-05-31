package jp.chang.myclinic.reception.javafx.edit_patient;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.remote.ComponentFinder;
import jp.chang.myclinic.reception.remote.DateFormComponent;
import jp.chang.myclinic.reception.remote.NameProvider;
import jp.chang.myclinic.reception.remote.SexRadioComponent;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicValue;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.util.function.Consumer;

public class EditPatientStage extends Stage implements NameProvider, ComponentFinder {

    //private static Logger logger = LoggerFactory.getLogger(EditPatientStage.class);
    private Consumer<PatientDTO> onEnterCallback = dto -> {};
    private PatientForm form;

    public EditPatientStage(PatientDTO patientDTO) {
        setTitle("患者情報編集");
        Parent root = createRoot(patientDTO);
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "edit-patient-stage");
        setScene(new Scene(root));
    }

    public void setOnEnterCallback(Consumer<PatientDTO> callback){
        this.onEnterCallback = callback;
    }

    private Parent createRoot(PatientDTO orig){
        VBox root = new VBox(4);
        this.form = new PatientForm(true);
        ErrorMessages em = new ErrorMessages();
        PatientFormInputs inputs = new LogicValue<>(orig)
                .convert(PatientFormLogic::patientDTOToPatientFormInputs)
                .getValue(null, em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
        }
        form.setInputs(inputs);
        root.getChildren().add(form);
        HBox commands = new HBox(4);
        commands.setAlignment(Pos.CENTER_RIGHT);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter(orig.patientId, form));
        cancelButton.setOnAction(evt -> close());
        commands.getChildren().addAll(enterButton, cancelButton);
        root.getChildren().add(commands);
        return root;
    }

    private void doEnter(int patientId, PatientForm form){
        PatientFormInputs inputs = form.getInputs();
        ErrorMessages em = new ErrorMessages();
        PatientDTO dto = new LogicValue<>(inputs)
                .convert(PatientFormLogic::patientFormInputsToPatientDTO)
                .getValue(null, em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
            return;
        }
        dto.patientId = patientId;
        onEnterCallback.accept(dto);
    }

    @Override
    public String getNameProviderName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Object findComponent(String selector) {
        if( "Birthday".equals(selector) ) {
            return new DateFormComponent(form.getBirthdayInput());
        } else if( "Sex".equals(selector) ){
            return new SexRadioComponent(form.getSexInput());
        } else {
            return getScene().lookup("#" + selector);
        }
    }
}
