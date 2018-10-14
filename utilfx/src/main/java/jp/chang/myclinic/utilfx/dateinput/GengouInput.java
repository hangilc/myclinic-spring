package jp.chang.myclinic.utilfx.dateinput;

import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import jp.chang.myclinic.consts.Gengou;

import java.util.List;

public class GengouInput extends ChoiceBox<Gengou> {

    public GengouInput(Gengou... gengouList){
        setConverter(makeConverter());
        getItems().addAll(gengouList);
    }

    public GengouInput(){
        this(Gengou.values());
    }

    public void setGengouList(List<Gengou> gengouList){
        getItems().clear();
        getItems().addAll(gengouList);
    }

    private StringConverter<Gengou> makeConverter(){
        return new StringConverter<Gengou>() {
            @Override
            public String toString(Gengou gengou) {
                return gengou.getKanji();
            }

            @Override
            public Gengou fromString(String string) {
                return Gengou.fromKanji(string);
            }
        };
    }

}
