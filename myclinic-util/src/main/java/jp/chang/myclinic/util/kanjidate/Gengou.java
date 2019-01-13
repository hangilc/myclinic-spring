package jp.chang.myclinic.util.kanjidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public enum Gengou {

    Heisei(LocalDate.of(1989, 1, 8), "平成", "Heisei"),
    Shouwa(LocalDate.of(1926, 12, 25), "昭和", "Shouwa"),
    Taishou(LocalDate.of(1912, 7, 30), "大正", "Taishou"),
    Meiji(LocalDate.of(1873, 1, 1), "明治", "Meiji");

    private LocalDate startDate;
    private String kanjiRep;
    private String alphaRep;

    Gengou(LocalDate startDate, String kanjiRep, String alphaRep){
        this.startDate = startDate;
        this.kanjiRep = kanjiRep;
        this.alphaRep = alphaRep;
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public String getKanjiRep() {
        return kanjiRep;
    }

    public String getAlphaRep() {
        return alphaRep;
    }
}
