package ${package};

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MainRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(MainRoot.class);

    MainRoot() {
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        getChildren().add(new Label("Hello, world"));
    }

}
