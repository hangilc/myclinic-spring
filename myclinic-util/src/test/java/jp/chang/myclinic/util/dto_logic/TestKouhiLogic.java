package jp.chang.myclinic.util.dto_logic;

import jp.chang.myclinic.util.logic.LogicValue;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestKouhiLogic extends LogicTestBase {

    @Test
    public void testJukyuushaBangou(){
        new LogicValue<>(4089140)
                .validate(KouhiLogic::isValidKouhiJukyuushaBangou)
                .verify(null, em);
        System.err.println(em.getMessage());
        assertTrue(em.hasNoError());
    }
}
