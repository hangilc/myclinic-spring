package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.recordbrowser.tracking.model.Conduct;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductShinryou;

class RecordConduct extends VBox {

    private VBox shinryouBox = new VBox();

    RecordConduct(Conduct conduct) {
        getChildren().addAll(
                createKindLabel(conduct),
                createGazouLabel(conduct),
                shinryouBox
        );
        conduct.getShinryouList().addListener((ListChangeListener<ConductShinryou>) c -> {
            while(c.next()){
                for(ConductShinryou s: c.getRemoved()){

                }
                for(ConductShinryou s: c.getAddedSubList()){
                    RecordConductShinryou rcs = new RecordConductShinryou(s);
                    shinryouBox.getChildren().add(rcs);
                }
            }
        });
    }

    private Node createKindLabel(Conduct conduct) {
        Label label = new Label();
        label.textProperty().bind(Bindings.concat("[", conduct.kindProperty(), "]"));
        return label;
    }

    private Node createGazouLabel(Conduct conduct) {
        Label label = new Label();
        label.textProperty().bind(conduct.gazouLabelProperty());
        if( label.getText().isEmpty() ){
            label.setVisible(false);
            label.setManaged(false);
        }
        conduct.gazouLabelProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue.isEmpty() ){
                label.setVisible(false);
                label.setManaged(false);
            } else {
                label.setVisible(true);
                label.setManaged(true);
            }
        });
        return label;
    }

}
