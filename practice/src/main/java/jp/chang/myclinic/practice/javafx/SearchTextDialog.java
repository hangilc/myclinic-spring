package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.javafx.parts.searchbox.BasicSearchTextInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchTextDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(SearchTextDialog.class);

    public SearchTextDialog(int patientId) {
        setTitle("文章検索");
        VBox root = new VBox(4);
        root.getStyleClass().add("search-text-dialog");
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        root.getChildren().addAll(
                createTextInput()
        );
        setScene(new Scene(root));

    }

    private Node createTextInput(){
        return new BasicSearchTextInput();
    }

}
