package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EnterDrugForm extends VBox {

    public EnterDrugForm(){
        super(4);
        getStyleClass().add("form");
        getChildren().addAll(
                createTitle(),
                createDisp(),
                createSearch()
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

    private Node createSearch(){
        return new DrugSearch();
    }

}
