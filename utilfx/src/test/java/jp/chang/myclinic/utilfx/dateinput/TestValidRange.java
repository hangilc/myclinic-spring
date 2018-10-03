package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.util.logic.ErrorMessages;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestValidRange {

    private ErrorMessages em;

    @Before
    public void resetErrorMessages(){
        this.em = new ErrorMessages();
    }

    @Test
    public void testOrder(){
        ValidFromLogic validFrom = new ValidFromLogic();
        validFrom.setStorageValue("2018-04-01", em);
        ValidUptoLogic validUpto = new ValidUptoLogic();
        validUpto.setStorageValue("2019-03-31", em);
        assertTrue(em.hasNoError());
        LocalDate validFromValue = validFrom.getValue(em);
        LocalDate validUptoValue = validUpto.getValue(em);
        assertTrue(em.hasNoError());
        ValidFromLogic.validateRange(validFrom, validUpto, em, (a, b) -> {
            assertEquals(validFromValue, a);
            assertEquals(validUptoValue, b);
        });
        assertTrue(em.hasNoError());
    }

    @Test
    public void testRangeStorageValues(){
        ValidFromLogic validFrom = new ValidFromLogic();
        String validFromStorage = "2018-04-01";
        validFrom.setStorageValue(validFromStorage, em);
        ValidUptoLogic validUpto = new ValidUptoLogic();
        String validUptoStorage = "2019-03-31";
        validUpto.setStorageValue(validUptoStorage, em);
        assertTrue(em.hasNoError());
        LocalDate validFromValue = validFrom.getValue(em);
        LocalDate validUptoValue = validUpto.getValue(em);
        assertTrue(em.hasNoError());
        ValidFromLogic.validateRangeToStorageValues(validFrom, validUpto, em, (a, b) -> {
            assertEquals(validFromStorage, a);
            assertEquals(validUptoStorage, b);
        });
        assertTrue(em.hasNoError());
    }

}
