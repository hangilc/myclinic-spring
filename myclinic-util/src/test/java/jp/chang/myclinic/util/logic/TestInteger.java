package jp.chang.myclinic.util.logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
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

}
