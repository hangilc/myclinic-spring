package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    Optional<TextEnterForm> findTextEnterForm(){
        for(Node node: getChildren()){
            if( node instanceof TextEnterForm ){
                return Optional.of((TextEnterForm)node);
            }
        }
        return Optional.empty();
    }

    Optional<RecordText> findRecordText(int textId){
        for(Node node: textsArea.getChildren()){
            if( node instanceof  RecordText ){
                RecordText t = (RecordText)node;
                if( t.getTextId() == textId ){
                    return Optional.of(t);
                }
            }
        }
        return Optional.empty();
    }

    List<Integer> listTextId(){
        List<Integer> result = new ArrayList<>();
        for(Node node: textsArea.getChildren()){
            if( node instanceof  RecordText ){
                result.add(((RecordText)node).getTextId());
            } else if( node instanceof TextEditForm ){
                result.add(((TextEditForm)node).getTextId());
            }
        }
        return result;
    }

    private RecordText createRecordText(TextDTO text){
        RecordText recordText = new RecordText(text);
        recordText.setOnMouseClicked(event -> {
            TextEditForm editForm = new TextEditForm(text);
            editForm.setCallback(new TextEditForm.Callback(){
                private void replace(Node oldNode, Node newNode){
                    int i = textsArea.getChildren().indexOf(oldNode);
                    if( i >= 0 ){
                        textsArea.getChildren().set(i, newNode);
                    }
                }

                @Override
                public void onUpdate(TextDTO updated) {
                    RecordText newRecordText = createRecordText(updated);
                    replace(editForm, newRecordText);
                }

                @Override
                public void onCancel() {
                    replace(editForm, recordText);
                }

                @Override
                public void onDelete() {
                    textsArea.getChildren().remove(editForm);
                }

                @Override
                public void onDone() {
                    replace(editForm, recordText);
                }
            });
            int index = textsArea.getChildren().indexOf(recordText);
            if( index >= 0 ){
                textsArea.getChildren().set(index, editForm);
            }
        });
        return recordText;
    }

    private void addText(TextDTO text){
        RecordText recordText = createRecordText(text);
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
