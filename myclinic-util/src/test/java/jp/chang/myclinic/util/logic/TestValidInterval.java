package jp.chang.myclinic.util.logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestValidInterval extends LogicTestBase {

    @Test
    public void testSimple(){
        BiValidators.verifyValidIntervalSqldate("2018-04-01", "2019-03-31", "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testOutOfOrder(){
        BiValidators.verifyValidIntervalSqldate("2019-03-31", "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }

    @Test
    public void testEqualDate(){
        BiValidators.verifyValidIntervalSqldate("2018-04-01", "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testNullFrom(){
        BiValidators.verifyValidIntervalSqldate(null, "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }

    @Test
    public void testNullRight(){
        BiValidators.verifyValidIntervalSqldate("2018-04-01", null, "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testBothNull(){
        BiValidators.verifyValidIntervalSqldate(null, null, "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }


}
