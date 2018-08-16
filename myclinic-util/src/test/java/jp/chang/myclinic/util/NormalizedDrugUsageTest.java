package jp.chang.myclinic.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test void partsTest(){
        NormalizedDrugUsage nu = new NormalizedDrugUsage("分３　毎食後");
        assertEquals(3, (Object)nu.getTimes());
        assertEquals(List.of("朝食後", "昼食後", "夕食後"), nu.getParts());
    }

}
