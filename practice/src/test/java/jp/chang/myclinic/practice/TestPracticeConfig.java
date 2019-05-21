package jp.chang.myclinic.practice;

import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

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

    @Test
    public void testSave() throws IOException {
        PracticeConfig c = new PracticeConfig();
        c.loadFromPropertiesString("#\n" +
                "#Thu Mar 29 17:13:17 JST 2018\n" +
                "shohousen-printer-setting=shohousen\n" +
                "default-printer-setting=shohousen\n" +
                "refer-printer-setting=refer\n" +
                "kouhatsu-kasan=外来後発医薬品使用体制加算２");
        String saved = c.saveToPropertiesString();
        Properties props = new Properties();
        ByteArrayInputStream in = new ByteArrayInputStream(saved.getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        props.load(reader);
        assertEquals("shohousen", props.getProperty("shohousen-printer-setting"));
        assertEquals("refer", props.getProperty("refer-printer-setting"));
        assertEquals("外来後発医薬品使用体制加算２", props.getProperty("kouhatsu-kasan"));
        assertEquals("shohousen", props.getProperty("default-printer-setting"));
    }
}
