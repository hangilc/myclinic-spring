package jp.chang.myclinic.util.logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestValidInterval extends LogicTestBase {

    @Test
    public void testSimple(){
        BiValidators.isValidIntervalSqldate("2018-04-01", "2019-03-31", "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testOutOfOrder(){
        BiValidators.isValidIntervalSqldate("2019-03-31", "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }

    @Test
    public void testEqualDate(){
        BiValidators.isValidIntervalSqldate("2018-04-01", "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testNullFrom(){
        BiValidators.isValidIntervalSqldate(null, "2018-04-01", "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }

    @Test
    public void testNullRight(){
        BiValidators.isValidIntervalSqldate("2018-04-01", null, "FROM", "UPTO", em);
        assertTrue(em.hasNoError());
    }

    @Test
    public void testBothNull(){
        BiValidators.isValidIntervalSqldate(null, null, "FROM", "UPTO", em);
        assertTrue(em.hasError());
    }


}
