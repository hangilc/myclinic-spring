package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

public class EditShahokokuhoStage extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(EditShahokokuhoStage.class);
    private Runnable onUpdateCallback;

    public EditShahokokuhoStage(ShahokokuhoDTO dto, Runnable onUpdateCallback) {
        this.onUpdateCallback = onUpdateCallback;
        setTitle("社保国保編集");
        Parent root = createMainPane(dto);
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "edit-shahokokuho-stage");
        setScene(new Scene(root));
    }

    private Parent createMainPane(ShahokokuhoDTO orig){
        VBox root = new VBox(4);
        ShahokokuhoForm form = new ShahokokuhoForm();
        ShahokokuhoFormLogic.EnterProc enterProc = ShahokokuhoFormLogic.createUpdateProc(orig, form::setInputs);
        if( enterProc == null ){
            Button closeButton = new Button("閉じる");
            closeButton.setOnAction(evt -> close());
            root.getChildren().add(closeButton);
            return root;
        }
        HBox commands = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> {
            ErrorMessages em = new ErrorMessages();
            ShahokokuhoDTO dto = enterProc.enter(form.getInputs(), em);
            if (em.hasError()) {
                GuiUtil.alertError(em.getMessage());
                return;
            }
            doEnter(dto);
        });
        cancelButton.setOnAction(evt -> close());
        commands.getChildren().addAll(enterButton, cancelButton);
        root.getChildren().addAll(form, commands);
        return root;
    }

    private void doEnter(ShahokokuhoDTO dto){
        Service.api.updateShahokokuho(dto)
                .thenAccept(ok -> Platform.runLater(() -> {
                    onUpdateCallback.run();
                    this.close();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

}
