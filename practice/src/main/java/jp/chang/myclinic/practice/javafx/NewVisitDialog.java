package jp.chang.myclinic.practice.javafx;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewVisitDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(NewVisitDialog.class);

    public NewVisitDialog() {
        VBox root = new VBox(4);
        root.getStyleClass().add("new-visit-dialog");
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        setScene(new Scene(root));
    }

}
