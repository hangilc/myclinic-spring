package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.logic.ErrorMessages;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestDateLogic {

    private static Logger logger = LoggerFactory.getLogger(TestDateLogic.class);
    private ErrorMessages em;

    public TestDateLogic() {

    }

    @Before
    public void clearErrors(){
        this.em = new ErrorMessages();
    }

    @Test
    public void testEmpty(){
        DateLogic logic = new DateLogic();
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testDate(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("32");
        logic.monthProperty().setValue("6");
        logic.dayProperty().setValue("2");
        LocalDate value = logic.getValue(em);
        assertEquals(LocalDate.of(1957, 6, 2), value);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testGengouNull(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(null);
        logic.nenProperty().setValue("32");
        logic.monthProperty().setValue("6");
        logic.dayProperty().setValue("2");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testNenNull(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue(null);
        logic.monthProperty().setValue("6");
        logic.dayProperty().setValue("2");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testNenEmpty(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("");
        logic.monthProperty().setValue("6");
        logic.dayProperty().setValue("2");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testNenInvalid(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("1a");
        logic.monthProperty().setValue("6");
        logic.dayProperty().setValue("2");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testMonthNull(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("32");
        logic.monthProperty().setValue(null);
        logic.dayProperty().setValue("2");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testMonthEmpty(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("32");
        logic.monthProperty().setValue("");
        logic.dayProperty().setValue("2");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testMonthInvalid(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("32");
        logic.monthProperty().setValue("13");
        logic.dayProperty().setValue("2");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testDayNull(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("32");
        logic.monthProperty().setValue("6");
        logic.dayProperty().setValue(null);
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testDayEmpty(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("32");
        logic.monthProperty().setValue("6");
        logic.dayProperty().setValue("");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testDayInvalid(){
        DateLogic logic = new DateLogic();
        logic.gengouProperty().setValue(Gengou.Shouwa);
        logic.nenProperty().setValue("32");
        logic.monthProperty().setValue("6");
        logic.dayProperty().setValue("-1");
        LocalDate value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }

    @Test
    public void testSetValue(){
        DateLogic logic = new DateLogic();
        LocalDate d = LocalDate.of(2018, 10, 1);
        logic.setValue(d, em);
        assertEquals(Gengou.Heisei, logic.gengouProperty().getValue());
        assertEquals("30", logic.nenProperty().getValue());
        assertEquals("10", logic.monthProperty().getValue());
        assertEquals("1", logic.dayProperty().getValue());
        assertEquals(d, logic.getValue(em));
        assertTrue(em.hasNoError());
    }

//    @Test
//    public void testSetValueNull(){
//        DateLogic logic = new DateLogic();
//        logic.setNen("12");
//        logic.setMonth("2");
//        logic.setDay("6");
//        assertFalse(logic.isEmpty());
//        logic.setValue(null);
//        assertTrue(logic.isEmpty());
//    }
//
//    @Test
//    public void testSetStorage(){
//        DateLogic logic = new DateLogic();
//        String err = logic.setValueFromStorage("1957-06-02", DateLogic.fromStorageConverter);
//        assertNull(err);
//        LocalDate d = logic.getValue(errors::add);
//        assertEquals(0, errors.size());
//        assertEquals(LocalDate.of(1957, 6, 2), d);
//        String store = logic.getStorageValue(DateLogic.toStorageConverter, errors::add);
//        assertEquals(0, errors.size());
//        assertEquals("1957-06-02", store);
//    }

}
