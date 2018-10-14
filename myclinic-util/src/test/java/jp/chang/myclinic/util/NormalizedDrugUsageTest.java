package jp.chang.myclinic.util;


import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class NormalizedDrugUsageTest {

    @Test
    public void emptyArgTest(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage(null);
        assertEquals(0, nu.getParts().size());
    }

    @Test
    public void parseTimesTest(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("");
        assertNull(nu.getTimes());
        assertEquals("", nu.getUsageWithoutTimes());
    }

    @Test
    public void parseTimes2Test(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("　分３　毎食後 　");
        assertEquals(3, (Object)nu.getTimes());
        assertEquals("毎食後", nu.getUsageWithoutTimes());
    }

    @Test
    public void partsTest(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("分３　毎食後");
        assertEquals(3, (Object)nu.getTimes());
        assertEquals(List.of("朝食後", "昼食後", "夕食後"), nu.getParts());
    }

    @Test
    public void parts2Test(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("分1　寝る前");
        assertEquals(1, (Object)nu.getTimes());
        assertEquals(List.of("寝る前"), nu.getParts());
    }

    @Test
    public void parts3Test(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("分1　眠前");
        assertEquals(1, (Object)nu.getTimes());
        assertEquals(List.of("寝る前"), nu.getParts());
    }

    @Test
    public void unevenTest(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("分３ (1-1-2) 毎食後");
        assertEquals(3, (Object)nu.getTimes());
        assertEquals("(1-1-2)", nu.getUnevenText());
        assertEquals("毎食後", nu.getUsageWithoutTimes());
        assertEquals(List.of("朝食後", "昼食後", "夕食後"), nu.getParts());
        assertEquals(List.of(1.0, 1.0, 2.0), nu.getWeights());
    }

    @Test
    public void uneven2Test(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("分３  毎食後(1-1-2)");
        assertEquals(3, (Object)nu.getTimes());
        assertEquals("(1-1-2)", nu.getUnevenText());
        assertEquals("毎食後", nu.getUsageWithoutTimes());
        assertEquals(List.of("朝食後", "昼食後", "夕食後"), nu.getParts());
        assertEquals(List.of(1.0, 1.0, 2.0), nu.getWeights());
    }

    @Test(expected = DrugUsageFormatException.class)
    public void formatErrorTest(){
        new NormalizedDrugUsage("分１  毎食後");
    }

    @Test
    public void timesTest(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("分３　朝昼食後、眠前");
        assertEquals(3, (Object)nu.getTimes());
        assertEquals(List.of("朝食後", "昼食後", "寝る前"), nu.getParts());
    }

}
