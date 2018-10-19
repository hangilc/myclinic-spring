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

public class EditKouhiStage extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(EditKouhiStage.class);
    private Consumer<KouhiDTO> callback = dto -> {};

    public EditKouhiStage(KouhiDTO dto) {
        setTitle("公費負担の編集");
        Parent root = createRoot(dto);
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "edit-kouhi-stage");
        setScene(new Scene(root));

    }

    public void setCallback(Consumer<KouhiDTO> callback){
        this.callback = callback;
    }

    private Parent createRoot(KouhiDTO dto){
        VBox root = new VBox(4);
        KouhiForm form = new KouhiForm();
        ErrorMessages em = new ErrorMessages();
        KouhiFormInputs inputs = new LogicValue<>(dto)
                .convert(KouhiFormLogic::kouhiDTOToKouhiFormInputs)
                .getValue(null, em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
        }
        if( inputs != null ){
            form.setInputs(inputs);
        }
        root.getChildren().add(form);
        HBox commands = new HBox(4);
        commands.setAlignment(Pos.CENTER_RIGHT);
        Button enterButton = new Button("入力");
        enterButton.setOnAction(evt -> doEnter(dto, form));
        Button cancelButton = new Button("キャンセル");
        cancelButton.setOnAction(evt -> close());
        commands.getChildren().addAll(enterButton, cancelButton);
        root.getChildren().add(commands);
        return root;
    }

    private void doEnter(KouhiDTO orig, KouhiForm form){
        ErrorMessages em = new ErrorMessages();
        KouhiFormInputs inputs = form.getInputs();
        KouhiDTO dto = new LogicValue<>(inputs)
                .convert(KouhiFormLogic::kouhiFormInputsToKouhiDTO)
                .getValue(null, em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
            return;
        }
        dto.kouhiId = orig.kouhiId;
        dto.patientId = orig.patientId;
        callback.accept(dto);
    }

}
