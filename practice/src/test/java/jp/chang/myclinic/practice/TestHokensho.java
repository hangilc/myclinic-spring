package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.shohousen.ShohousenData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestHokensho {

    @Test
    public void testHokenshaBangouRep(){
        ShohousenData data = new ShohousenData();
        HokenDTO hoken = new HokenDTO();
        ShahokokuhoDTO shaho = new ShahokokuhoDTO();
        shaho.hokenshaBangou = 1130012;
        hoken.shahokokuho = shaho;
        data.setHoken(hoken);
        assertEquals("01130012", data.hokenshaBangou);
    }

}
