package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.consts.Gengou;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestDateLogic {

    private static Logger logger = LoggerFactory.getLogger(TestDateLogic.class);
    private List<String> errors = new ArrayList<>();

    public TestDateLogic() {

    }

    @Before
    public void clearErrors(){
        errors.clear();
    }

    @Test
    public void testEmpty(){
        DateLogic logic = new DateLogic();
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testEmptyAllowed(){
        DateLogic logic = new DateLogic();
        logic.setNullAllowed(true);
        LocalDate value = logic.getValue(errors::add);
        assertEquals(LocalDate.MAX, value);
        assertEquals(0, errors.size());
    }

    @Test
    public void testDate(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::add);
        assertEquals(LocalDate.of(1957, 6, 2), value);
        assertEquals(0, errors.size());
    }

    @Test
    public void testGengouNull(){
        DateLogic logic = new DateLogic();
        logic.setGengou(null);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testNenNull(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen(null);
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testNenEmpty(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("");
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testNenInvalid(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("12a");
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testMonthNull(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth(null);
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testMonthEmpty(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testMonthInvalid(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("9a");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testDayNull(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay(null);
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testDayEmpty(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay("");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testDayInvalid(){
        DateLogic logic = new DateLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay("12B");
        LocalDate value = logic.getValue(errors::add);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testSetValue(){
        DateLogic logic = new DateLogic();
        LocalDate d = LocalDate.of(2018, 10, 1);
        logic.setValue(d);
        assertEquals(Gengou.Heisei, logic.getGengou());
        assertEquals("30", logic.getNen());
        assertEquals("10", logic.getMonth());
        assertEquals("1", logic.getDay());
        assertEquals(d, logic.getValue(errors::add));
        assertEquals(0, errors.size());
    }

    @Test
    public void testSetValueNull(){
        DateLogic logic = new DateLogic();
        logic.setNen("12");
        logic.setMonth("2");
        logic.setDay("6");
        assertFalse(logic.isEmpty());
        logic.setValue(null);
        assertTrue(logic.isEmpty());
    }

    @Test
    public void testSetStorage(){
        DateLogic logic = new DateLogic();
        String err = logic.setValueFromStorage("1957-06-02", DateLogic.fromStorageConverter);
        assertNull(err);
        LocalDate d = logic.getValue(errors::add);
        assertEquals(0, errors.size());
        assertEquals(LocalDate.of(1957, 6, 2), d);
        String store = logic.getStorageValue(DateLogic.toStorageConverter, errors::add);
        assertEquals(0, errors.size());
        assertEquals("1957-06-02", store);
    }

}
