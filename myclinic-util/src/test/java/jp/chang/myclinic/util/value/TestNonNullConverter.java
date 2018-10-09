package jp.chang.myclinic.util.value;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class TestNonNullConverter extends LogicTestBase {

    @Test
    public void testNull(){
        ImmediateLogic<Integer> src = new ImmediateLogic<>(null);
        Logic<Integer> logic = src.chain(Converters.nonNullConverter());
        Integer value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }

    @Test
    public void testNonNull(){
        ImmediateLogic<Integer> src = new ImmediateLogic<>(12);
        Logic<Integer> logic = src.chain(Converters.nonNullConverter());
        Integer value = logic.getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertEquals(12, (int)value);
    }

}
