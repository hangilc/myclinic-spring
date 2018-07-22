package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDTO;

public class ConductModel {

    //private static Logger logger = LoggerFactory.getLogger(ConductModel.class);
    private int conductId;
    private int visitId;
    private StringProperty conductKindRep;
    private StringProperty gazouLabel = new SimpleStringProperty();
    private ObservableList<ConductShinryouModel> conductShinryouList = FXCollections.observableArrayList();
    private ObservableList<ConductDrugModel> conductDrugs = FXCollections.observableArrayList();
    private ObservableList<ConductKizaiModel> conductKizaiList = FXCollections.observableArrayList();

    ConductModel(ConductDTO conductDTO) {
        this.conductId = conductDTO.conductId;
        this.visitId = conductDTO.visitId;
        this.conductKindRep = new SimpleStringProperty(ConductKind.fromCode(conductDTO.kind).getKanjiRep());
    }

    public int getConductId() {
        return conductId;
    }

    public int getVisitId() {
        return visitId;
    }

    public String getConductKindRep() {
        return conductKindRep.get();
    }

    public StringProperty conductKindRepProperty() {
        return conductKindRep;
    }

    public void setConductKindRep(String conductKindRep) {
        this.conductKindRep.set(conductKindRep);
    }

    public String getGazouLabel() {
        return gazouLabel.get();
    }

    public StringProperty gazouLabelProperty() {
        return gazouLabel;
    }

    public void setGazouLabel(String gazouLabel) {
        this.gazouLabel.set(gazouLabel);
    }

    public ObservableList<ConductShinryouModel> getConductShinryouList() {
        return conductShinryouList;
    }

    public ObservableList<ConductDrugModel> getConductDrugs() {
        return conductDrugs;
    }

    public ObservableList<ConductKizaiModel> getConductKizaiList() {
        return conductKizaiList;
    }
}
