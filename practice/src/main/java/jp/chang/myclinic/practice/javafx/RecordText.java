package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.practice.javafx.text.TextLib;

import java.util.Optional;

public class RecordText extends StackPane {

    private int textId;
    private int visitId;
    private TextLib textLib;

    RecordText(TextDTO text){
        this.textId = text.textId;
        this.visitId = text.visitId;
        TextDisp disp = new TextDisp(text.content);
        disp.setOnMouseClicked(event -> onDispClicked(disp));
        getChildren().add(disp);
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

    public void setTextLib(TextLib textLib){
        this.textLib = textLib;
    }

    private TextLib getTextLib(){
        return textLib != null ? textLib : Globals.getInstance().getTextLib();
    }

    private void onDispClicked(TextDisp disp){
        TextEnterForm form = new TextEnterForm(visitId, getTextLib());
        getChildren().setAll(form);
    }

}


