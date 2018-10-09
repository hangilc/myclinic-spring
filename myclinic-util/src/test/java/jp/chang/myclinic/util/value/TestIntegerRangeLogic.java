package jp.chang.myclinic.util.value;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestIntegerRangeLogic extends LogicTestBase {

    @Test
    public void testInRange(){
        ImmediateLogic<Integer> src = new ImmediateLogic<>(6);
        Logic<Integer> logic = src.chain(Converters.integerRangeConverter(1, 12));
        Integer value = logic.getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertEquals(6, (int)value);
    }

    @Test
    public void testOutOfRange(){
        ImmediateLogic<Integer> src = new ImmediateLogic<>(0);
        Logic<Integer> logic = src.chain(Converters.integerRangeConverter(1, 12));
        Integer value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }
}
