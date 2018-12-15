package jp.chang.myclinic.reception.javafx.edit_koukikourei;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.util.logic.LogicValue;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestKoukikoureiFormLogic extends LogicTestBase {

    @Test
    public void testValid(){
        KoukikoureiFormInputs inputs = new KoukikoureiFormInputs();
        inputs.hokenshaBangou = "39131156";
        inputs.hihokenshaBangou = "87654323";
        inputs.validFromInputs = new DateFormInputs(Gengou.Heisei, "29", "8", "1");
        inputs.validUptoInputs = new DateFormInputs(Gengou.Heisei, "30", "7", "31");
        inputs.futanwari = 1;
        KoukikoureiDTO dto = new LogicValue<>(inputs)
                .convert(KoukikoureiFormLogic::koukikoureiFormInputsToKoukikoureiDTO)
                .getValue(null, em);
        if( em.hasError() ){
            System.out.println(em.getMessage());
        }
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testInputToHihokenshaBangou(){
        String result = KoukikoureiFormLogic.inputToHihokenshaBangou("87654323", null, em);
        assertTrue(em.hasNoError());
        assertEquals("87654323", result);
    }

    @Test
    public void testInputToHihokenshaBangou07(){
        String result = KoukikoureiFormLogic.inputToHihokenshaBangou("07654320", null, em);
        assertTrue(em.hasNoError());
        assertEquals("07654320", result);
    }

}
