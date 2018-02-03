package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;

public class DrugSearch extends VBox {

    public DrugSearch(){
        getChildren().addAll(
                createSearchInput(),
                createMode()
        );
    }

    private Node createSearchInput(){
        HBox hbox = new HBox(4);
        TextField searchTextInput = new TextField();
        Button searchButton = new Button("検索");
        hbox.getChildren().addAll(searchTextInput, searchButton);
        return hbox;
    }

    private Node createMode(){
        HBox hbox = new HBox(4);
        RadioButtonGroup<DrugSearchMode> group = new RadioButtonGroup<>();
        group.createRadioButton("マスター", DrugSearchMode.Master);
        group.createRadioButton("約束処方", DrugSearchMode.Example);
        group.createRadioButton("過去の処方", DrugSearchMode.Previous);
        group.setValue(DrugSearchMode.Example);
        hbox.getChildren().addAll(group.getButtons());
        return hbox;
    }

}
