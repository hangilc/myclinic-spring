package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;

public class RecordText extends StackPane {

    public interface Callback {
        void onDelete();
    }

    private Callback callback;

    public RecordText(TextDTO text) {
        getChildren().add(createDisp(text));
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private Node createDisp(TextDTO text) {
        String content = text.content;
        if (content.isEmpty()) {
            content = "(空白)";
        }
        TextFlow disp = new TextFlow();
        disp.getChildren().add(new Text(content));
        disp.setOnMouseClicked(event -> {
            TextEditForm form = new TextEditForm(text);
            form.setCallback(new TextEditForm.Callback() {
                @Override
                public void onEnter(String content) {
                    TextDTO newText = text.copy();
                    newText.content = content;
                    PracticeLib.updateText(newText, () -> {
                        getChildren().clear();
                        getChildren().add(createDisp(newText));
                    });
                }

                @Override
                public void onCancel() {
                    getChildren().remove(form);
                    getChildren().add(disp);
                }

                @Override
                public void onDelete() {
                    PracticeLib.deleteText(text, () -> {
                        if (callback != null) {
                            callback.onDelete();
                        }
                    });
                }

                @Override
                public void onShohousen() {

                }

                @Override
                public void onCopy() {

                }
            });
            getChildren().clear();
            getChildren().add(form);
        });
        return disp;
    }

}
