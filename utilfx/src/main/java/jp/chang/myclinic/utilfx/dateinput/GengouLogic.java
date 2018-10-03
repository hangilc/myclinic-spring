package jp.chang.myclinic.utilfx.dateinput;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;

class GengouLogic implements Logic<Gengou>  {

    //private static Logger logger = LoggerFactory.getLogger(GengouLogic.class);
    private ObjectProperty<Gengou> gengou = new SimpleObjectProperty<>();

    public Gengou getGengou() {
        return gengou.get();
    }

    public ObjectProperty<Gengou> gengouProperty() {
        return gengou;
    }

    public void setGengou(Gengou gengou) {
        this.gengou.set(gengou);
    }

    @Override
    public Gengou getValue(ErrorMessages em) {
        Gengou g = gengou.getValue();
        if( g == null ){
            em.add("元号が設定されていません。");
            return null;
        } else {
            return g;
        }
    }

    @Override
    public void setValue(Gengou value, ErrorMessages em) {
        gengou.setValue(value);
    }

}
