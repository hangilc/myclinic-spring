package jp.chang.myclinic.pharma.javafx.prevtechou;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PrevTechouRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(PrevTechouRoot.class);

    PrevTechouRoot() {
        super(4);
        getChildren().addAll(
            createSearchInput()
        );
    }

    private Node createSearchInput(){
        HBox hbox = new HBox(4);
        TextField inputField = new TextField();
        Button searchButton = new Button("検索");
        hbox.getChildren().addAll(inputField, searchButton);
        return hbox;
    }

}
