package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductDrugModel;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductKizaiModel;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductModel;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductShinryouModel;

class RecordConduct extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(RecordConduct.class);
    private int conductId;
    private VBox gazouLabelBox = new VBox();
    private VBox shinryouBox = new VBox();
    private VBox drugBox = new VBox();
    private VBox kizaiBox = new VBox();

    RecordConduct(ConductModel conductModel) {
        super(0);
        this.conductId = conductModel.getConductId();
        Text kindText = new Text();
        kindText.textProperty().bind(conductModel.conductKindRepProperty());
        getChildren().addAll(
                new TextFlow(new Text("<"), kindText, new Text(">")),
                gazouLabelBox,
                shinryouBox,
                drugBox,
                kizaiBox
        );
        conductModel.gazouLabelProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue == null ){
                gazouLabelBox.getChildren().clear();
            } else {
                gazouLabelBox.getChildren().setAll(new TextFlow(new Text(newValue)));
            }
        });
        conductModel.getConductShinryouList().addListener((ListChangeListener<ConductShinryouModel>) c -> {
            while(c.next()){
                for(ConductShinryouModel model: c.getRemoved()){
                    int conductShinryouId = model.getConductShinryouId();
                    shinryouBox.getChildren().removeIf(node -> {
                        if( node instanceof RecordConductShinryou ){
                            RecordConductShinryou r = (RecordConductShinryou)node;
                            return r.getConductShinryouId() == conductShinryouId;
                        } else {
                            return false;
                        }
                    });
                }
                for(ConductShinryouModel model: c.getAddedSubList()){
                    RecordConductShinryou rec = new RecordConductShinryou(model);
                    shinryouBox.getChildren().add(rec);
                }
            }
        });
        conductModel.getConductDrugs().addListener((ListChangeListener<ConductDrugModel>) c -> {
            while(c.next()){
                for(ConductDrugModel model: c.getRemoved()){
                    int conductDrugId = model.getConductDrugId();
                    drugBox.getChildren().removeIf(node -> {
                        if( node instanceof RecordConductDrug ){
                            RecordConductDrug r = (RecordConductDrug)node;
                            return r.getConductDrugId() == conductDrugId;
                        } else {
                            return false;
                        }
                    });
                }
                for(ConductDrugModel model: c.getAddedSubList()){
                    RecordConductDrug rec = new RecordConductDrug(model);
                    drugBox.getChildren().add(rec);
                }
            }
        });
        conductModel.getConductKizaiList().addListener((ListChangeListener<ConductKizaiModel>) c -> {
            while(c.next()){

            }
        });
    }

    int getConductId() {
        return conductId;
    }


}
