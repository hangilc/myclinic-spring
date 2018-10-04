package jp.chang.myclinic.util.logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestInteger extends LogicBase {

    @Test
    public void testSimple(){
        IntegerLogic logic = new IntegerLogic("TEST");
        logic.setValue(12, em);
        Integer value = logic.getValue(em);
        assertTrue(em.hasNoError());
        assertEquals(12, (int)value);
    }

    @Test
    public void testNull(){
        IntegerLogic logic = new IntegerLogic("TEST");
        Integer value = logic.getValue(em);
        assertTrue(em.hasError());
        assertNull(value);
    }

    @Test
    public void testInput(){
        IntegerLogic logic = new IntegerLogic("TEST");
        logic.setStorageValue("32", em);
        String store = logic.getStorageValue(em);
        assertTrue(em.hasNoError());
        assertEquals("32", store);
    }

    @Test
    public void testInvalidInput(){
        IntegerLogic logic = new IntegerLogic("TEST");
        logic.setStorageValue("32a", em);
        String store = logic.getStorageValue(em);
        assertTrue(em.hasError());
        assertEquals(null, store);
    }

}
