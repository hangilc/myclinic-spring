package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;

public class DrugSearch extends VBox {

    private RadioButtonGroup<DrugSearchMode> modeGroup;

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
        searchButton.setOnAction(event -> doSearch());
        hbox.getChildren().addAll(searchTextInput, searchButton);
        return hbox;
    }

    private Node createMode(){
        HBox hbox = new HBox(4);
        modeGroup = new RadioButtonGroup<>();
        modeGroup.createRadioButton("マスター", DrugSearchMode.Master);
        modeGroup.createRadioButton("約束処方", DrugSearchMode.Example);
        modeGroup.createRadioButton("過去の処方", DrugSearchMode.Previous);
        modeGroup.setValue(DrugSearchMode.Example);
        hbox.getChildren().addAll(modeGroup.getButtons());
        return hbox;
    }

    private void doSearch(){
        DrugSearchMode mode = modeGroup.getValue();
        if( mode != null ){
            switch(mode){
                case Master: doMasterSearch(); break;
                case Example: doExampleSearch(); break;
                case Previous: doPreviousSearch(); break;
            }
        }
    }

    private void doMasterSearch(){

    }

    private void doExampleSearch(){

    }

    private void doPreviousSearch(){

    }

}
