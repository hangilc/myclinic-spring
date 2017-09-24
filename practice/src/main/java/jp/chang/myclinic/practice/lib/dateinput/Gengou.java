package jp.chang.myclinic.practice.lib.dateinput;

import java.time.chrono.JapaneseEra;

public enum Gengou {

    MEIJI(JapaneseEra.MEIJI, "明治"),
    TAISHO(JapaneseEra.TAISHO, "大正"),
    SHOUWA(JapaneseEra.SHOWA, "昭和"),
    HEISEI(JapaneseEra.HEISEI, "平成");

    private JapaneseEra era;
    private String kanjiRep;

    Gengou(JapaneseEra era, String kanjiRep){
        this.era = era;
        this.kanjiRep = kanjiRep;
    }

    public String getKanjiRep(){
        return kanjiRep;
    }

    public JapaneseEra getEra(){
        return era;
    }

}
