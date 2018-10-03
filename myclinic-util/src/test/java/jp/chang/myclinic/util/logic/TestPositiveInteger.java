package jp.chang.myclinic.util.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestPositiveInteger {
    private ErrorMessages em;

    @Before
    public void setupErrorMessages(){
        this.em = new ErrorMessages();
    }

    @Test
    public void testNull(){
        PositiveIntegerLogic logic = new PositiveIntegerLogic("テスト");
        Integer value = logic.getValue(em);
        assertNull(value);
        assertTrue(em.hasError());
    }
}
