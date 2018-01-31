package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.TextDTO;

public class RecordText extends StackPane {

    public RecordText(TextDTO text){
        getChildren().add(createDisp(text));
    }

    private Node createDisp(TextDTO text){
        String content = text.content;
        if( content.isEmpty() ){
            content = "(空白)";
        }
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().add(new Text(content));
        textFlow.setOnMouseClicked(event -> {
            TextEditForm form = new TextEditForm(text);
            form.setCallback(new TextEditForm.Callback() {
                @Override
                public void onEnter(String content) {

                }

                @Override
                public void onCancel() {
                    getChildren().remove(form);
                    getChildren().add(textFlow);
                }

                @Override
                public void onDelete() {

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
        return textFlow;
    }
}
