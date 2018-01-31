package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;

import java.util.List;

public class RecordTextsPane extends VBox {

    private int visitId;

    public RecordTextsPane(List<TextDTO> texts, int visitId){
        this.visitId = visitId;
        texts.forEach(this::addText);
        getChildren().add(createNewTextLink());
    }

    private void addText(TextDTO text){
        getChildren().add(new RecordText(text));
    }

    private Node createNewTextLink(){
        Hyperlink link = new Hyperlink("[文章追加]");
        link.setOnAction(event -> {
            TextForm textForm = new TextForm();
            textForm.setCallback(new TextForm.Callback() {
                @Override
                public void onEnter(String content) {
                    PracticeLib.enterText(visitId, content, newText ->{
                        getChildren().remove(textForm);
                        addText(newText);
                        getChildren().add(link);
                    });
                }

                @Override
                public void onCancel() {
                    getChildren().remove(textForm);
                    getChildren().add(link);
                }
            });
            getChildren().remove(link);
            getChildren().add(textForm);
        });
        return link;
    }

}
