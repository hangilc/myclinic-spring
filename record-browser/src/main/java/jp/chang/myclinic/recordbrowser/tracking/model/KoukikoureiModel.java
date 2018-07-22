package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jp.chang.myclinic.dto.KoukikoureiDTO;

class KoukikoureiModel {

    //private static Logger logger = LoggerFactory.getLogger(KoukikoureiModel.class);
    private int koukikoureiId;
    private ObjectProperty<Integer> futanWari = new SimpleObjectProperty<>();

    KoukikoureiModel(KoukikoureiDTO koukikoureiDTO) {
        this.koukikoureiId = koukikoureiDTO.koukikoureiId;
        futanWari.setValue(koukikoureiDTO.futanWari);
    }

    public int getKoukikoureiId() {
        return koukikoureiId;
    }

    public Integer getFutanWari() {
        return futanWari.get();
    }

    public ObjectProperty<Integer> futanWariProperty() {
        return futanWari;
    }

    public void setFutanWari(Integer futanWari) {
        this.futanWari.set(futanWari);
    }
}
