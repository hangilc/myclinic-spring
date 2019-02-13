package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.disease.select.Disp;
import jp.chang.myclinic.practice.javafx.events.TextEnteredEvent;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

public class RecordText extends TextFlow {
    private int textId;
    private String contentRep;

    RecordText(TextDTO text){
        this.textId = text.textId;
        this.contentRep = text.content;
        if (contentRep.isEmpty()) {
            this.contentRep = "(空白)";
        }
        getChildren().add(new Text(contentRep));
    }

    int getTextId(){
        return textId;
    }

    public String getContentRep(){
        return contentRep;
    }

}

//public class RecordText extends StackPane {
//
//    private static class Disp extends TextFlow {
//        private String content;
//
//        Disp(TextDTO text){
//            this.content = text.content;
//            if( content.isEmpty() ){
//                this.content = "(空白)";
//            }
//            getChildren().add(new Text(content));
//            setOnMouseClicked(event -> {
//                TextEditForm form = createTextEditForm(text);
//                getChildren().clear();
//                getChildren().add(form);
//            });
//        }
//
//        private TextEditForm createTextEditForm(TextDTO text){
//            TextEditForm form = new TextEditForm(text);
//            form.setCallback(new TextEditForm.Callback() {
//                @Override
//                public void onEnter(String content) {
//                    TextDTO newText = text.copy();
//                    newText.content = content;
//                    Parent parent = getParent();
//                    Service.api.updateText(newText)
//                            .thenAcceptAsync(ok -> {
//                                parent.getChildren().replace
//                            }, Platform::runLater)
//                            .exceptionally(GuiUtil.alertExceptionGui("テキストの更新に失敗しました。"));
//                    PracticeLib.updateText(newText, () -> {
//                        getChildren().clear();
//                        getChildren().add(createDisp(newText));
//                    });
//                }
//
//                @Override
//                public void onCancel() {
//                    getChildren().remove(form);
//                    getChildren().add(disp);
//                }
//
//                @Override
//                public void onDelete() {
//                    PracticeLib.deleteText(text, () -> {
//                        if (callback != null) {
//                            Platform.runLater(callback::onDelete);
//                        }
//                    });
//                }
//
//                @Override
//                public void onDone() {
//                    getChildren().remove(form);
//                    getChildren().add(disp);
//                }
//
//                @Override
//                public void onCopy() {
//                    int targetVisitId = PracticeUtil.findCopyTarget(text.visitId);
//                    if( targetVisitId != 0 ){
//                        TextDTO newText = new TextDTO();
//                        newText.visitId = targetVisitId;
//                        newText.content = text.content;
//                        Service.api.enterText(newText)
//                                .thenCompose(Service.api::getText)
//                                .thenAccept(enteredText -> Platform.runLater(() -> {
//                                    TextEnteredEvent textEnteredEvent = new TextEnteredEvent(enteredText);
//                                    RecordText.this.fireEvent(textEnteredEvent);
//                                    getChildren().remove(form);
//                                    getChildren().add(disp);
//                                }))
//                                .exceptionally(HandlerFX::exceptionally);
//                    }
//                }
//            });
//        }
//    }
//
//    public interface Callback {
//        void onDelete();
//    }
//
//    private Callback callback = () -> {};
//
//    private int textId;
//    private String content;
//    private String disp;
//
//    RecordText(TextDTO text) {
//        this.textId = text.textId;
//        getChildren().add(createDisp(text));
//    }
//
//    int getTextId(){
//        return textId;
//    }
//
//    public void setCallback(Callback callback) {
//        this.callback = callback;
//    }
//
//    private Node createDisp(TextDTO text) {
//        this.content = text.content;
//        this.disp = content;
//        if (content.isEmpty()) {
//            this.disp = "(空白)";
//        }
//        TextFlow disp = new TextFlow();
//        disp.getChildren().add(new Text(content));
//        disp.setOnMouseClicked(event -> {
//            TextEditForm form = new TextEditForm(text);
//            form.setCallback(new TextEditForm.Callback() {
//                @Override
//                public void onEnter(String content) {
//                    TextDTO newText = text.copy();
//                    newText.content = content;
//                    PracticeLib.updateText(newText, () -> {
//                        getChildren().clear();
//                        getChildren().add(createDisp(newText));
//                    });
//                }
//
//                @Override
//                public void onCancel() {
//                    getChildren().remove(form);
//                    getChildren().add(disp);
//                }
//
//                @Override
//                public void onDelete() {
//                    PracticeLib.deleteText(text, () -> {
//                        if (callback != null) {
//                            Platform.runLater(callback::onDelete);
//                        }
//                    });
//                }
//
//                @Override
//                public void onDone() {
//                    getChildren().remove(form);
//                    getChildren().add(disp);
//                }
//
//                @Override
//                public void onCopy() {
//                    int targetVisitId = PracticeUtil.findCopyTarget(text.visitId);
//                    if( targetVisitId != 0 ){
//                        TextDTO newText = new TextDTO();
//                        newText.visitId = targetVisitId;
//                        newText.content = text.content;
//                        Service.api.enterText(newText)
//                                .thenCompose(Service.api::getText)
//                                .thenAccept(enteredText -> Platform.runLater(() -> {
//                                    TextEnteredEvent textEnteredEvent = new TextEnteredEvent(enteredText);
//                                    RecordText.this.fireEvent(textEnteredEvent);
//                                    getChildren().remove(form);
//                                    getChildren().add(disp);
//                                }))
//                                .exceptionally(HandlerFX::exceptionally);
//                    }
//                }
//            });
//            getChildren().clear();
//            getChildren().add(form);
//        });
//        return disp;
//    }
//
//}
