package jp.chang.myclinic.reception.javafx.edit_kouhi;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicValue;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.util.function.Consumer;

public class EnterKouhiStage extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(EnterKouhiStage.class);
    private Consumer<KouhiDTO> callback = dto -> {};

    public EnterKouhiStage(int patientId) {
        setTitle("新規公費負担の入力");
        Parent root = createRoot(patientId);
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "enter-kouhi-stage");
        setScene(new Scene(root));
    }

    public void setCallback(Consumer<KouhiDTO> callback){
        this.callback = callback;
    }

    private Parent createRoot(int patientId){
        VBox root = new VBox(4);
        KouhiForm form = new KouhiForm();
        form.setInputs(KouhiFormInputs.createForEnter());
        root.getChildren().add(form);
        HBox commands = new HBox(4);
        commands.setAlignment(Pos.CENTER_RIGHT);
        Button enterButton = new Button("入力");
        enterButton.setOnAction(evt -> doEnter(patientId, form));
        Button cancelButton = new Button("キャンセル");
        cancelButton.setOnAction(evt -> close());
        commands.getChildren().addAll(enterButton, cancelButton);
        root.getChildren().add(commands);
        return root;
    }

    private void doEnter(int patientId, KouhiForm form){
        ErrorMessages em = new ErrorMessages();
        KouhiFormInputs inputs = form.getInputs();
        KouhiDTO dto = new LogicValue<>(inputs)
                .convert(KouhiFormLogic::kouhiFormInputsToKouhiDTO)
                .getValue(null, em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
            return;
        }
        dto.patientId = patientId;
        callback.accept(dto);
    }

}
