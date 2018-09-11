package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.javafx.drug2.Input;
import jp.chang.myclinic.practice.javafx.drug2.SearchInput;
import jp.chang.myclinic.practice.javafx.drug2.SearchResult;

public class NewPrescExampleDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(NewPrescExampleDialog.class);

    public NewPrescExampleDialog() {
        setTitle("処方例の新規入力");
        Parent mainPane = createMainPane();
        mainPane.getStyleClass().add("new-presc-example-dialog");
        mainPane.getStylesheets().add("css/Practice.css");
        setScene(new Scene(mainPane));
    }

    private Parent createMainPane(){
        VBox vbox = new VBox(4);
        Input input = new Input();
        SearchInput searchInput = new SearchInput();
        SearchResult searchResult = new SearchResult();
        vbox.getChildren().addAll(
                input,
                searchInput,
                searchResult
        );
        return vbox;
    }

}
