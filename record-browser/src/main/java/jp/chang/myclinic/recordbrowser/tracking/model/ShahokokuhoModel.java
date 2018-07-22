package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

class ShahokokuhoModel {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoModel.class);
    private int shahokokuhoId;
    private ObjectProperty<Integer> hokenshaBangou = new SimpleObjectProperty<>();
    private ObjectProperty<Integer> koureiFutanWari = new SimpleObjectProperty<>();

    ShahokokuhoModel(ShahokokuhoDTO shahokokuhoDTO) {
        this.shahokokuhoId = shahokokuhoDTO.shahokokuhoId;
        this.hokenshaBangou.setValue(shahokokuhoDTO.hokenshaBangou);
        this.koureiFutanWari.setValue(shahokokuhoDTO.kourei);
    }

    public int getShahokokuhoId() {
        return shahokokuhoId;
    }

    public Integer getHokenshaBangou() {
        return hokenshaBangou.get();
    }

    public ObjectProperty<Integer> hokenshaBangouProperty() {
        return hokenshaBangou;
    }

    public void setHokenshaBangou(Integer hokenshaBangou) {
        this.hokenshaBangou.set(hokenshaBangou);
    }

    public Integer getKoureiFutanWari() {
        return koureiFutanWari.get();
    }

    public ObjectProperty<Integer> koureiFutanWariProperty() {
        return koureiFutanWari;
    }

    public void setKoureiFutanWari(Integer koureiFutanWari) {
        this.koureiFutanWari.set(koureiFutanWari);
    }
}
