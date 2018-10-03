package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.util.logic.ErrorMessages;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

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
        boolean ver = ValidFromLogic.validateRange(validFromValue, validUptoValue, em);
        assertTrue(ver);
        assertTrue(em.hasNoError());
    }
}
