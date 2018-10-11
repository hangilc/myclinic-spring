package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.value.ErrorMessages;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;

public class EnterShahokokuhoStage extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(EnterShahokokuhoStage.class);

    public EnterShahokokuhoStage() {
        setTitle("新規社保国保入力");
        Parent root = createMainPane();
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "enter-shahokokuho-stage");
        setScene(new Scene(root));
    }

    private Parent createMainPane(){
        VBox root = new VBox(4);
        ShahokokuhoForm form = new ShahokokuhoForm();
        ShahokokuhoFormInputs inputs = new ShahokokuhoFormInputs();
        inputs.honnin = 0;
        inputs.validFromInputs = new DateFormInputs(Gengou.Current);
        inputs.validUptoInputs = new DateFormInputs(Gengou.Current);
        inputs.kourei = 0;
        form.setInputs(inputs);
        HBox commands = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter(form));
        cancelButton.setOnAction(evt -> close());
        commands.getChildren().addAll(enterButton, cancelButton);
        root.getChildren().addAll(form, commands);
        return root;
    }

    private void doEnter(ShahokokuhoForm form){
        ShahokokuhoFormInputs inputs = form.getInputs();
        ErrorMessages em = new ErrorMessages();
        ShahokokuhoDTO dto = ShahokokuhoFormLogic.inputsToDTO(inputs, em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
            return;
        }
        System.out.println(dto);
    }

}
