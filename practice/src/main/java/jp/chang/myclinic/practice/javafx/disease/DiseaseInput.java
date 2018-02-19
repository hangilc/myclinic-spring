package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class DiseaseInput extends VBox {

    Text nameText = new Text("");

    public DiseaseInput(){
        super(4);
        getChildren().addAll(
            createName()
        );
    }

    private Node createName(){
        return new TextFlow(new Label("名称："), nameText);
    }
}
