package jp.chang.myclinic.util.logic;

import org.junit.Test;

import static jp.chang.myclinic.util.logic.Validators.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestOneOf extends LogicTestBase {

    @Test
    public void testSimple(){
        Integer value = new LogicValue<>(0)
                .validate(isOneOf(0, 1))
                .getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertEquals(0, (int)value);
    }

    @Test
    public void testInvalid(){
        Integer value = new LogicValue<>(3)
                .validate(isOneOf(0, 1))
                .getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }

}
