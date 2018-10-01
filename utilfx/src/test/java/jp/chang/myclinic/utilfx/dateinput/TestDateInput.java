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

public class TestDateInput {

    private static Logger logger = LoggerFactory.getLogger(TestDateInput.class);
    private List<String> errors = new ArrayList<>();

    public TestDateInput() {

    }

    @Before
    public void clearErrors(){
        errors.clear();
    }

    @Test
    public void testEmpty(){
        DateInputLogic logic = new DateInputLogic();
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testEmptyAllowed(){
        DateInputLogic logic = new DateInputLogic();
        logic.setNullAllowed(true);
        LocalDate value = logic.getValue(errors::addAll);
        assertEquals(LocalDate.MAX, value);
        assertEquals(0, errors.size());
    }

    @Test
    public void testDate(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::addAll);
        assertEquals(LocalDate.of(1957, 6, 2), value);
        assertEquals(0, errors.size());
    }

    @Test
    public void testGengouNull(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(null);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testNenNull(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen(null);
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testNenEmpty(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("");
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testNenInvalid(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("12a");
        logic.setMonth("6");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testMonthNull(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth(null);
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testMonthEmpty(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testMonthInvalid(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("9a");
        logic.setDay("2");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testDayNull(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay(null);
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testDayEmpty(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay("");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testDayInvalid(){
        DateInputLogic logic = new DateInputLogic();
        logic.setGengou(Gengou.Shouwa);
        logic.setNen("32");
        logic.setMonth("6");
        logic.setDay("12B");
        LocalDate value = logic.getValue(errors::addAll);
        assertNull(value);
        assertTrue(errors.size() > 0);
    }


}
