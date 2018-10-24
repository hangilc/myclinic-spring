package jp.chang.myclinic.hotline;

import jp.chang.myclinic.util.logic.LogicValue;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestHotlineLogic extends LogicTestBase {

    @Test
    public void testValidMessageLength(){
        new LogicValue<>("診察室におねがいします。")
                .validate(HotlineLogic::isNotTooLongToEnter)
                .verify(null, em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testTooLongMessage(){
        StringBuilder msg = new StringBuilder();
        for(int i=0;i<15;i++){
            msg.append("あいうえおかきくけこ");
        }
        new LogicValue<>(msg.toString())
                .validate(HotlineLogic::isNotTooLongToEnter)
                .verify(null, em);
        assertTrue(em.hasError());
    }

    @Test
    public void testValidLines(){
        new LogicValue<>("診察室におねがいします。")
                .validate(HotlineLogic::hasNotTooManyLinesToEnter)
                .verify(null, em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testTooManyLines(){
        StringBuilder msg = new StringBuilder();
        for(int i=0;i<11;i++){
            msg.append("あいうえお\n");
        }
        new LogicValue<>(msg.toString())
                .validate(HotlineLogic::hasNotTooManyLinesToEnter)
                .verify(null, em);
        assertTrue(em.hasError());
    }

}
