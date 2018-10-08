package jp.chang.myclinic.util.logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestIntegerChoiceLogic extends LogicBase {

    @Test
    public void testSimple(){
        IntegerChoiceLogic logic = new IntegerChoiceLogic("TEST", 0, 1);
        logic.valueProperty().setValue(0);
        Integer value = logic.getValue(em);
        assertTrue(em.hasNoError());
        assertEquals(0, (int)value);
    }

    @Test
    public void testWrong(){
        IntegerChoiceLogic logic = new IntegerChoiceLogic("TEST", 0, 1);
        logic.valueProperty().setValue(3);
        Integer value = logic.getValue(em);
        assertTrue(em.hasError());
        assertNull(value);
    }

    @Test
    public void testNull(){
        IntegerChoiceLogic logic = new IntegerChoiceLogic("TEST", 0, 1);
        logic.valueProperty().setValue(null);
        Integer value = logic.getValue(em);
        assertTrue(em.hasError());
        assertNull(value);
    }

}
