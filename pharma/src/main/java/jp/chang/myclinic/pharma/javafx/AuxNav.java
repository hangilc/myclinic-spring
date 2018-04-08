package jp.chang.myclinic.pharma.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AuxNav extends StackPane {

    private static Logger logger = LoggerFactory.getLogger(AuxNav.class);

    AuxNav() {
        getStyleClass().add("aux-nav");
    }

    void setContent(Node node){
        getChildren().setAll(node);
    }
}
