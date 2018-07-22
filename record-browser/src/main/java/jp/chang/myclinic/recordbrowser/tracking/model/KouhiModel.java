package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jp.chang.myclinic.dto.KouhiDTO;

class KouhiModel {

    //private static Logger logger = LoggerFactory.getLogger(KouhiModel.class);
    private int kouhiId;
    private ObjectProperty<Integer> futanshaBangou = new SimpleObjectProperty<>();

    KouhiModel(KouhiDTO kouhiDTO) {
        this.kouhiId = kouhiDTO.kouhiId;
        this.futanshaBangou.setValue(kouhiDTO.futansha);
    }

    public int getKouhiId() {
        return kouhiId;
    }

    public Integer getFutanshaBangou() {
        return futanshaBangou.get();
    }

    public ObjectProperty<Integer> futanshaBangouProperty() {
        return futanshaBangou;
    }

    public void setFutanshaBangou(Integer futanshaBangou) {
        this.futanshaBangou.set(futanshaBangou);
    }
}
