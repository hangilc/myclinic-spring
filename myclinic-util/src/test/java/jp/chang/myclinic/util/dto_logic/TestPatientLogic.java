package jp.chang.myclinic.util.dto_logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPatientLogic extends LogicTestBase {

    //private static Logger logger = LoggerFactory.getLogger(TestPatientLogic.class);

    @Test
    public void testBirthdayRep(){
        String rep = PatientLogic.birthdaySqldateToRep("1957-06-02");
        assertTrue(em.hasNoError());
        assertEquals("昭和32年6月2日", rep);
    }

}
