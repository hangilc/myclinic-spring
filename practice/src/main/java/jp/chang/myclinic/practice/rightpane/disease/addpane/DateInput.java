package jp.chang.myclinic.practice.rightpane.disease.addpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.chrono.JapaneseEra;
import java.util.Collections;
import java.util.List;

public class DateInput extends JPanel {

    public enum Gengou {

        MEIJI(JapaneseEra.MEIJI, "明治"),
        TAISHO(JapaneseEra.TAISHO, "大正"),
        SHOUWA(JapaneseEra.SHOWA, "昭和"),
        HEISEI(JapaneseEra.HEISEI, "平成");

        private JapaneseEra era;
        private String kanjiRep;

        Gengou(JapaneseEra era, String kanjiRep){
            this.kanjiRep = kanjiRep;
        }

        public String getKanjiRep(){
            return kanjiRep;
        }

        public JapaneseEra getEra(){
            return era;
        }
    }

    private JComboBox<String> gengouBox;

    public DateInput(List<Gengou> gengouList){
        setLayout(new MigLayout("insets 0", "", ""));
        setupGengouBox(gengouList);
    }

    public DateInput(Gengou gengou){
        this(Collections.singletonList(gengou));
    }

    private void setupGengouBox(List<Gengou> gengouList){
        
    }
}
