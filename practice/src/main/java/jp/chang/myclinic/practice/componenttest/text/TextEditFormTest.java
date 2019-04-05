package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.utilfx.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class TextEditFormTest extends ComponentTestBase {

    public TextEditFormTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private TextEditForm prepareForm(TextDTO text) {
        TextEditForm form = new TextEditForm(text);
        gui(() -> {
            form.setPrefWidth(329);
            form.setPrefHeight(300);
            main.getChildren().setAll(form);
            stage.sizeToScene();
        });
        return form;
    }

    @CompTest(excludeFromBatch = true)
    public void testTextEditFormDisp() {
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.content = "体調いい。";
        prepareForm(text);
    }

    @CompTest
    public void testTextEditFormEnter() {
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.content = "体調いい。";
        String modifiedContent = text.content + "\nたちくらみもない。";
        class Local {
            private boolean confirmUpdateText;
            private boolean confirmCallback;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter() {
            @Override
            public CompletableFuture<Void> updateText(TextDTO modified) {
                local.confirmUpdateText = modified.visitId == text.visitId &&
                        modified.content.equals(modifiedContent);
                return CompletableFuture.completedFuture(null);
            }
        };
        TextEditForm form = prepareForm(text);
        form.setOnUpdated(updated -> {
            local.confirmCallback = updated.visitId == text.visitId &&
                    updated.content.equals(modifiedContent);
        });
        gui(() -> {
            form.simulateSetText(modifiedContent);
            form.simulateClickEnterButton();
        });
        waitForTrue(8, () -> local.confirmUpdateText && local.confirmCallback);
    }

    @CompTest
    public void testTextEditFormDelete() {
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.textId = 100;
        text.content = "削除テスト";
        class Local {
            private boolean confirmDeleteText;
            private boolean confirmCallback;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter() {
            @Override
            public CompletableFuture<Void> deleteText(int textId) {
                confirm(textId == text.textId);
                local.confirmDeleteText = true;
                return CompletableFuture.completedFuture(null);
            }
        };
        TextEditForm form = prepareForm(text);
        form.setOnDeleted(() -> {
            local.confirmCallback = true;
        });
        gui(form::simulateClickDeleteButton);
        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
        gui(confirmDialog::simulateClickOkButton);
        waitForTrue(10, () -> local.confirmDeleteText);
        waitForTrue(10, () -> local.confirmCallback);
    }

}
