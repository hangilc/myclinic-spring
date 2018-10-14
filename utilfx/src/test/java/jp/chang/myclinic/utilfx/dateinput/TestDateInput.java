package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.logic.LogicValue;
import org.junit.Test;

import java.time.LocalDate;

import static jp.chang.myclinic.utilfx.dateinput.DateFormLogic.dateFormInputsToLocalDate;
import static jp.chang.myclinic.utilfx.dateinput.DateFormLogic.localDateToDateFormInputs;
import static org.junit.Assert.*;

public class TestDateInput extends LogicTestBase {

    @Test
    public void testSimple(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "30", "10", "13");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertEquals(LocalDate.of(2018, 10, 13), date);
    }

    @Test
    public void testNull(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, null, null, null);
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertNull(date);
    }

    @Test
    public void testEmpty(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "", "", "");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertNull(date);
    }

    @Test
    public void testNullGengou(){
        DateFormInputs inputs = new DateFormInputs(null, "30", "10", "13");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testNullNen(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, null, "10", "13");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testEmptyNen(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "", "10", "13");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testInvalidNen(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "0", "10", "13");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testNullMonth(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "30", null, "13");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testEmptyMonth(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "30", "", "13");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testInvalidMonth(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "30", "13", "13");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testNullDay(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "30", "10", null);
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testEmptyDay(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "30", "10", "");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testInvalidDay(){
        DateFormInputs inputs = new DateFormInputs(Gengou.Heisei, "30", "10", "32");
        LocalDate date = new LogicValue<DateFormInputs>(inputs)
                .convert(dateFormInputsToLocalDate())
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(date);
    }

    @Test
    public void testDateToInputs(){
        LocalDate date = LocalDate.of(2018, 10, 13);
        DateFormInputs inputs = new LogicValue<>(date)
                .convert(localDateToDateFormInputs())
                .getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertEquals(Gengou.Heisei, inputs.gengou);
        assertEquals("30", inputs.nen);
        assertEquals("10", inputs.month);
        assertEquals("13", inputs.day);
    }

    @Test
    public void testNullDateToInputs(){
        DateFormInputs inputs = new LogicValue<LocalDate>(null)
                .convert(localDateToDateFormInputs())
                .getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertNull(inputs);
    }
}
