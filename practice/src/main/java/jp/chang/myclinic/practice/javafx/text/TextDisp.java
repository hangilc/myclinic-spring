package jp.chang.myclinic.practice.javafx.text;

import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TextDisp extends TextFlow {

    private String content;
    private String rep;

    public TextDisp(String content) {
        this.content = content;
        this.rep = contentToRep(content);
        getChildren().add(new Text(rep));
    }

    public String getContent(){
        return content;
    }

    public String getRep(){
        return rep;
    }

    public void simulateMouseEvent(MouseEvent event){
        this.fireEvent(event);
    }

    private String contentToRep(String content){
        if( content.isEmpty() ){
            return "(空白)";
        } else {
            return content;
        }
    }

}
