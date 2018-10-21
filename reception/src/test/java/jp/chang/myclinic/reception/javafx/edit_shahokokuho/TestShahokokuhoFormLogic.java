package jp.chang.myclinic.reception.javafx.edit_shahokokuho;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicValue;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestShahokokuhoFormLogic extends LogicTestBase {

    private ShahokokuhoDTO inputsToDTO(ShahokokuhoFormInputs inputs, ErrorMessages em){
        return new LogicValue<>(inputs)
                .convert(ShahokokuhoFormLogic::shahokokuhoFormInputsToShahokokuhoDTO)
                .getValue(null, em);
    }

    @Test
    public void testValid(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testValid06Digits(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "012344";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testValid5Digits(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "12344";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testValid08Digits(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "01234566";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testValid7Digits(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "1234566";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testEmptyHokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testNullHokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = null;
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testNonNumberHokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "abc";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testNegativeHokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "-3";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testSmallHokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "1234";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertEquals("保険者番号の桁数が少なすぎます。", em.getMessage());
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testLargeHokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123456789";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertEquals("保険者番号の桁数が多すぎます。", em.getMessage());
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testInvalidCheckDigitHokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123456";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertEquals("保険者番号の検証番号が正しくありません。", em.getMessage());
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testEmptyHihokenshaInfo(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "";
        ins.hihokenshaBangou = "";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testValidEmptyHihokenshaKigou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testValidEmptyHihokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testNullHihokenshaKigou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = null;
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
        assertEquals("", dto.hihokenshaKigou);
    }

    @Test
    public void testNullHihokenshaBangou(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = null;
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
        assertEquals("", dto.hihokenshaBangou);
    }

    @Test
    public void testOutOfRangeHonnin(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 3;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testNullValidFrom(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = null;
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testEmptyValidFrom(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei);
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testOutOfOrderValidFromValidUpto(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "6", "1");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei, "29", "5", "31");
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }

    @Test
    public void testNonNullValidUpto(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 0;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei, "31", "1", "1");
        ins.kourei = 0;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasNoError());
        assertNotNull(dto);
    }

    @Test
    public void testOutOfRangeKourei(){
        ShahokokuhoFormInputs ins = new ShahokokuhoFormInputs();
        ins.hokenshaBangou = "123455";
        ins.hihokenshaKigou = "a";
        ins.hihokenshaBangou = "1";
        ins.honnin = 1;
        ins.validFromInputs = new DateFormInputs(Gengou.Heisei, "30", "1", "2");
        ins.validUptoInputs = new DateFormInputs(Gengou.Heisei);
        ins.kourei = 4;
        ShahokokuhoDTO dto = inputsToDTO(ins, em);
        assertTrue(em.hasError());
        assertNull(dto);
    }



}
