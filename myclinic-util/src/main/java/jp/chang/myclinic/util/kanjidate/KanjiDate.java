package jp.chang.myclinic.util.kanjidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class KanjiDate {

    public static int gengouToYear(Gengou g, int nen){
        return g.getStartDate().getYear() + nen - 1;
    }

    public static GengouNenPair yearToGengou(int year, int month, int day){
        LocalDate d = LocalDate.of(year, month, day);
        for(Gengou g: Gengou.values()){
            if( !d.isBefore(g.getStartDate()) ){
                int nen = year - g.getStartDate().getYear() + 1;
                return new GengouNenPair(g, nen);
            }
        }
        throw new IllegalArgumentException("Cannot convert to gengou.");
    }

    public static GengouNenPair yearToGengou(LocalDate date){
        return yearToGengou(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

}
