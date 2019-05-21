package jp.chang.myclinic.practice;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestPracticeConfig {

    @Test
    public void testLoad(){
        PracticeConfig c = new PracticeConfig();
        c.loadFromPropertiesString("#\n" +
                "#Thu Mar 29 17:13:17 JST 2018\n" +
                "shohousen-printer-setting=shohousen\n" +
                "default-printer-setting=shohousen\n" +
                "refer-printer-setting=refer\n" +
                "kouhatsu-kasan=外来後発医薬品使用体制加算２");
        assertEquals("shohousen", c.getShohousenPrinterSetting());
        assertEquals("refer", c.getReferPrinterSetting());
        assertEquals("外来後発医薬品使用体制加算２", c.getKouhatsuKasan());
    }
}
