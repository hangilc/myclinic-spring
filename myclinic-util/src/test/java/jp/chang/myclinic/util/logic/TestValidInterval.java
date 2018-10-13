package jp.chang.myclinic.util.logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestValidInterval extends LogicTestBase {

    @Test
    public void testSimple(){
        Validators.verifyValidInterval("2018-04-01", "2019-03-31", "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testOutOfOrder(){
        Validators.verifyValidInterval("2019-03-31", "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }

    @Test
    public void testEqualDate(){
        Validators.verifyValidInterval("2018-04-01", "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testNullFrom(){
        Validators.verifyValidInterval(null, "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }

    @Test
    public void testNullRight(){
        Validators.verifyValidInterval("2018-04-01", null, "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testBothNull(){
        Validators.verifyValidInterval(null, null, "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }


}
