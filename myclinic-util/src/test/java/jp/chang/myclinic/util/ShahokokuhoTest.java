package jp.chang.myclinic.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ShahokokuhoTest {

    @Test
    public void testHokenshaBangouRep(){
        int bangou = 1130012;
        assertEquals("01130012", ShahokokuhoUtil.hokenshaBangouRep(bangou));
    }

}
