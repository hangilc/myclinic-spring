package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WorkForm extends VBox {

    public WorkForm(String title){
        super(4);
        getStyleClass().add("form");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        getChildren().add(titleLabel);
    }

    public void add(Node node){
        getChildren().add(node);
    }

}
