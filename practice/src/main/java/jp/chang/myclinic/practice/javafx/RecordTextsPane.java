package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;

import java.util.List;

class RecordTextsPane extends VBox {

    private int visitId;
    private VBox textsArea = new VBox(4);
    private Hyperlink newTextLink;

    RecordTextsPane(List<TextDTO> texts, int visitId){
        super(4);
        this.visitId = visitId;
        texts.forEach(this::addText);
        this.newTextLink = createNewTextLink();
        getChildren().addAll(textsArea, newTextLink);
    }

    void simulateNewTextButtonClick(){
        newTextLink.fire();
    }

    TextEnterForm findTextEnterForm(){
        for(Node node: getChildren()){
            if( node instanceof TextEnterForm ){
                return (TextEnterForm)node;
            }
        }
        return null;
    }

    RecordText findRecordText(int textId){
        for(Node node: getChildren()){
            if( node instanceof  RecordText ){
                RecordText t = (RecordText)node;
                if( t.getTextId() == textId ){
                    return t;
                }
            }
        }
        return null;
    }

    private void addText(TextDTO text){
        RecordText recordText = new RecordText(text);
        recordText.setCallback(() -> textsArea.getChildren().remove(recordText));
        textsArea.getChildren().add(recordText);
    }

    private Hyperlink createNewTextLink(){
        Hyperlink link = new Hyperlink("[文章追加]");
        link.setOnAction(event -> {
            TextEnterForm textEnterForm = new TextEnterForm();
            textEnterForm.setCallback(new TextEnterForm.Callback() {
                @Override
                public void onEnter(String content) {
                    PracticeLib.enterText(visitId, content, newText ->{
                        getChildren().remove(textEnterForm);
                        addText(newText);
                        getChildren().add(link);
                    });
                }

                @Override
                public void onCancel() {
                    getChildren().remove(textEnterForm);
                    getChildren().add(link);
                }
            });
            getChildren().remove(link);
            getChildren().add(textEnterForm);
        });
        return link;
    }

    void appendText(TextDTO enteredText) {
        addText(enteredText);
    }
}
