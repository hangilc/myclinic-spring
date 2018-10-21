package jp.chang.myclinic.util.dto_logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShahokokuhoLogic extends LogicTestBase {

    @Test
    public void testFormatHokenshaBangou1(){
        String result = ShahokokuhoLogic.formatHokenshaBangou(12345);
        assertEquals("012345", result);
    }

    @Test
    public void testFormatHokenshaBangou2(){
        String result = ShahokokuhoLogic.formatHokenshaBangou(1234567);
        assertEquals("01234567", result);
    }

    @Test
    public void testFormatHokenshaBangou3(){
        String result = ShahokokuhoLogic.formatHokenshaBangou(123456);
        assertEquals("123456", result);
    }

}
