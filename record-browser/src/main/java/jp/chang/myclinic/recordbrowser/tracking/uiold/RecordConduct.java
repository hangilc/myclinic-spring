package jp.chang.myclinic.recordbrowser.tracking.uiold;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.recordbrowser.tracking.modelold.Conduct;
import jp.chang.myclinic.recordbrowser.tracking.modelold.ConductDrug;
import jp.chang.myclinic.recordbrowser.tracking.modelold.ConductKizai;
import jp.chang.myclinic.recordbrowser.tracking.modelold.ConductShinryou;

class RecordConduct extends VBox {

    private VBox shinryouBox = new VBox();
    private VBox drugBox = new VBox();
    private VBox kizaiBox = new VBox();

    RecordConduct(Conduct conduct) {
        getChildren().addAll(
                createKindLabel(conduct),
                createGazouLabel(conduct),
                shinryouBox,
                drugBox,
                kizaiBox
        );
        conduct.getShinryouList().addListener((ListChangeListener<ConductShinryou>) c -> {
            while(c.next()){
                for(ConductShinryou s: c.getRemoved()){
                    int conductShinryouId = s.getConductShinryouId();
                    shinryouBox.getChildren().removeIf(
                            rec -> ((RecordConductShinryou)rec).getConductShinryouId() == conductShinryouId
                    );
                }
                for(ConductShinryou s: c.getAddedSubList()){
                    RecordConductShinryou rcs = new RecordConductShinryou(s);
                    shinryouBox.getChildren().add(rcs);
                }
            }
        });
        conduct.getDrugs().addListener((ListChangeListener<ConductDrug>) c -> {
            while(c.next()){
                for(ConductDrug d: c.getRemoved()){
                    int conductDrugId = d.getConductDrugId();
                    drugBox.getChildren().removeIf(
                            rec -> ((RecordConductDrug)rec).getConductDrugId() == conductDrugId
                    );
                }
                for(ConductDrug d: c.getAddedSubList()){
                    RecordConductDrug rcd = new RecordConductDrug(d);
                    drugBox.getChildren().add(rcd);
                }
            }
        });
        conduct.getKizaiList().addListener((ListChangeListener<ConductKizai>) c -> {
            while(c.next()){
                for(ConductKizai k: c.getRemoved()){
                    int conductKizaiId = k.getConductKizaiId();
                    kizaiBox.getChildren().removeIf(
                            rec -> ((RecordConductKizai)rec).getConductKizaiId() == conductKizaiId
                    );
                }
                for(ConductKizai k: c.getAddedSubList()){
                    RecordConductKizai rck = new RecordConductKizai(k);
                    kizaiBox.getChildren().add(rck);
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
