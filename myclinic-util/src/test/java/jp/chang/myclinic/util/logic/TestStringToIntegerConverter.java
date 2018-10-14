package jp.chang.myclinic.util.logic;

import org.junit.Test;

import static jp.chang.myclinic.util.logic.Converters.stringToInteger;
import static org.junit.Assert.*;

public class TestStringToIntegerConverter extends LogicTestBase {

    @Test
    public void testSimple(){
        LogicValue<String> src = new LogicValue<>("123");
        Logic<Integer> logic = src.convert(stringToInteger());
        Integer value = logic.getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertEquals(123,(int)value);
    }

    @Test
    public void testInvalid(){
        LogicValue<String> src = new LogicValue<>("abc");
        Logic<Integer> logic = src.convert(stringToInteger());
        Integer value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }

    @Test()
    public void testNull(){
        LogicValue<String> src = new LogicValue<>(null);
        Logic<Integer> logic = src.convert(stringToInteger());
        Integer value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
   }
}
