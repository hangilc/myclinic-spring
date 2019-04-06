package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.RecordText;
import jp.chang.myclinic.practice.javafx.RecordTextsPane;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.utilfx.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TextsPaneTest extends ComponentTestBase {

    public TextsPaneTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private RecordTextsPane createTextsPane(int visitId, List<TextDTO> texts) {
        RecordTextsPane pane = new RecordTextsPane(visitId, texts);
        gui(() -> {
            pane.setPrefWidth(329);
            pane.setPrefHeight(300);
            main.getChildren().setAll(pane);
            stage.sizeToScene();
        });
        return pane;
    }

    @CompTest
    public void testTextsPaneDisp() {
        createTextsPane(1, Collections.emptyList());
    }

    @CompTest
    public void testTextsPaneNewText() {
        RecordTextsPane pane = createTextsPane(1, Collections.emptyList());
        gui(pane::simulateNewTextButtonClick);
        TextEnterForm form = waitFor(pane::findTextEnterForm);
        confirm(form.getVisitId() == 1);
        gui(form::simulateClickCancelButton);
        waitForFail(pane::findTextEnterForm);
        gui(pane::simulateNewTextButtonClick);
        TextEnterForm form2 = waitFor(pane::findTextEnterForm);
        String content = "新規文書のテスト。";
        class Local {
            private TextDTO entered;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter(){
            @Override
            public CompletableFuture<Integer> enterText(TextDTO text) {
                text.textId = 1;
                local.entered = text;
                return value(text.textId);
            }
        };
        gui(() -> {
            form2.simulateSetText(content);
            form2.simulateClickEnterButton();
        });
        waitForFail(pane::findTextEnterForm);
        waitForTrue(() -> local.entered != null);
        confirm(local.entered.content.equals(content));
        waitForTrue(() -> pane.listTextId().size() > 0);
        List<Integer> textIds = pane.listTextId();
        confirm(textIds.size() == 1);
        confirm(textIds.get(0) == local.entered.textId);
    }

    @CompTest
    public void testTextsPaneDelete(){
        TextDTO text1 = new TextDTO();
        text1.textId = 1;
        text1.visitId = 1;
        text1.content = "CONTENT 1";
        TextDTO text2 = new TextDTO();
        text2.textId = 2;
        text2.visitId = 1;
        text2.content = "CONTENT 2";
        TextDTO text3 = new TextDTO();
        text3.textId = 3;
        text3.visitId = 1;
        text3.content = "CONTENT 3";
        RecordTextsPane pane = createTextsPane(1, List.of(text1, text2, text3));
        RecordText rec2 = waitFor(() -> pane.findRecordText(2));
        TextDisp disp2 = waitFor(rec2::findTextDisp);
        gui(() -> disp2.simulateMouseEvent(createMouseClickedEvent(disp2)));
        TextEditForm form2 = waitFor(rec2::findTextEditForm);
        Context.frontend = new FrontendAdapter(){
            @Override
            public CompletableFuture<Void> deleteText(int textId) {
                confirm(textId == text2.textId);
                return value(null);
            }
        };
        Context.currentPatientService = new CurrentPatientService();
        gui(form2::simulateClickDeleteButton);
        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
        gui(confirmDialog::simulateClickOkButton);
        waitForFail(() -> pane.findRecordText(2));
        waitForTrue(() -> pane.listTextId().size() == 2);
        List<Integer> textIds = pane.listTextId();
        confirm(textIds.size() == 2);
        confirm(textIds.equals(List.of(1, 3)));
    }
}
