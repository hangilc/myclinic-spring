package jp.chang.myclinic.reception.javafx.edit_shahokokuho;

import javafx.application.Platform;
import javafx.geometry.Pos;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnterShahokokuhoStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EnterShahokokuhoStage.class);
    private Runnable onEnterCallback;
    private ShahokokuhoForm form;

    public EnterShahokokuhoStage(int patientId, Runnable onEnterCallback) {
        this.onEnterCallback = onEnterCallback;
        setTitle("新規社保国保入力");
        Parent root = createMainPane(patientId);
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "enter-shahokokuho-stage");
        setScene(new Scene(root));
    }

    public void setInputs(ShahokokuhoFormInputs inputs){
        this.form.setInputs(inputs);
    }

    private Parent createMainPane(int patientId) {
        VBox root = new VBox(4);
        this.form = new ShahokokuhoForm();
        ShahokokuhoFormLogic.EnterProc enterProc = ShahokokuhoFormLogic.createEnterProc(patientId, form::setInputs);
        HBox commands = new HBox(4);
        commands.setAlignment(Pos.CENTER_RIGHT);
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

    private void doEnter(ShahokokuhoDTO dto) {
        Service.api.enterShahokokuho(dto)
                .thenAcceptAsync(shahokokuhoId -> {
                    onEnterCallback.run();
                    this.close();
                }, Platform::runLater)
                .exceptionally(ex -> {
                    logger.error("Failed to enter shahokokuho.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("社保・国保の新規登録に失敗しました。", ex));
                    return null;
                });
    }

}
