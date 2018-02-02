package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EnterDrugForm extends VBox {

    public EnterDrugForm(){
        getStyleClass().add("form");
        getChildren().addAll(
                createTitle(),
                createDisp()
        );
    }

    private Node createTitle(){
        Label title = new Label("新規処方の入力");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    private Node createDisp(){
        return new DrugInput();
    }

}
