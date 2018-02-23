package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchPatientDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(SearchPatientDialog.class);

    public SearchPatientDialog() {
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        root.getStyleClass().add("search-patient-dialog");
        root.getChildren().addAll(
                createSeachBox()
        );
        setScene(new Scene(root));
    }

    private Node createSearchBox(){

        //SearchBox<PatientDTO> searchBox = new SearchBox<>();
        return new Label("temp");
    }

}
