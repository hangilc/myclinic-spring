package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.practice.javafx.text.TextRequirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RecordTextsPane extends VBox {

    private int visitId;
    private VBox textsArea = new VBox(4);
    private Hyperlink newTextLink;
    private TextRequirement textRequirement;

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
        recordText.setTextRequirement(textRequirement);
        return recordText;
    }

    private void addText(TextDTO text){
        RecordText recordText = createRecordText(text);
        recordText.setOnDeletedCallback(() -> textsArea.getChildren().remove(recordText));
        textsArea.getChildren().add(recordText);
    }

    private TextRequirement getTextRequirement(){
        return textRequirement;
    }

    void setTextRequirement(TextRequirement textRequirement){
        this.textRequirement = textRequirement;
    }

    private Hyperlink createNewTextLink(){
        Hyperlink link = new Hyperlink("[文章追加]");
        link.setOnAction(event -> {
            TextEnterForm textEnterForm = new TextEnterForm(visitId, getTextRequirement());
            textEnterForm.setOnEntered(entered -> {
                getChildren().remove(textEnterForm);
                addText(entered);
                getChildren().add(link);
            });
            textEnterForm.setOnCancel(() -> {
                getChildren().remove(textEnterForm);
                getChildren().add(link);

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
