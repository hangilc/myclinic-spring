package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDTO;

public class Conduct {

    private int conductId;
    private int visitId;
    private StringProperty kind = new SimpleStringProperty();
    private StringProperty gazouLabel = new SimpleStringProperty("");
    private ObservableList<ConductShinryou> shinryouList = FXCollections.observableArrayList();
    private ObservableList<ConductDrug> drugs = FXCollections.observableArrayList();
    private ObservableList<ConductKizai> kizaiList = FXCollections.observableArrayList();

    public Conduct(ConductDTO conductDTO) {
        this.conductId = conductDTO.conductId;
        this.visitId = conductDTO.visitId;
        this.kind.setValue(getConductKindRep(conductDTO));
    }

    public int getConductId() {
        return conductId;
    }

    public int getVisitId() {
        return visitId;
    }

    public String getKind() {
        return kind.get();
    }

    public StringProperty kindProperty() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind.set(kind);
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

    public ObservableList<ConductShinryou> getShinryouList() {
        return shinryouList;
    }

    public ObservableList<ConductDrug> getDrugs() {
        return drugs;
    }

    public ObservableList<ConductKizai> getKizaiList() {
        return kizaiList;
    }

    private String getConductKindRep(ConductDTO conductDTO){
        ConductKind conductKind = ConductKind.fromCode(conductDTO.kind);
        if( conductKind == null ){
            return "??";
        } else {
            return conductKind.getKanjiRep();
        }
    }

}
