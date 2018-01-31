package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.PracticeEnv;

public class DiseasesPane extends VBox {

    DiseasesPane() {
        super(4);
        getStyleClass().add("diseases-pane");
        setFillWidth(true);
        getStyleClass().add("diseases-pane");
        getChildren().addAll(
                createTitle(),
                createDiseases()
        );
    }

    private Node createTitle() {
        Label label = new Label("病名");
        label.getStyleClass().add("diseases-pane-title");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private Node createDiseases() {
        VBox box = new VBox(2);
        PracticeEnv.INSTANCE.currentDiseasesProperty().addListener((obs, oldValue, newValue) -> {
            box.getChildren().clear();
            if( newValue != null ){
                newValue.forEach(d -> {
                    box.getChildren().add(new Disease(d));
                });
            }
        });
        return box;
    }

}
