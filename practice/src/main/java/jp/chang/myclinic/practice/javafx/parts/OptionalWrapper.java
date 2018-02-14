package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class OptionalWrapper extends StackPane {

    public OptionalWrapper(){
        hide();
    }

    public void show(Node content){
        getChildren().clear();
        getChildren().add(content);
        setManaged(true);
        setVisible(true);
    }

    public void hide(){
        setVisible(false);
        setManaged(false);
    }
}
