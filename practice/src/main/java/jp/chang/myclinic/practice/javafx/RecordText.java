package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.text.TextDisp;

import java.util.Optional;

public class RecordText extends StackPane {

    private int textId;

    RecordText(TextDTO text){
        this.textId = text.textId;
        getChildren().add(new TextDisp(text.content));
    }

    int getTextId(){
        return textId;
    }

    public boolean isDisplaying(){
        return findTextDisp().isPresent();
    }

    public Optional<TextDisp> findTextDisp(){
        for(Node n: getChildren()){
            if( n instanceof TextDisp ){
                return Optional.of((TextDisp)n);
            }
        }
        return Optional.empty();
    }

}


