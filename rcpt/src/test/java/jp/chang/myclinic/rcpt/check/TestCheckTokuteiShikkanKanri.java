package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckTokuteiShikkanKanri extends Base {

    @Test
    public void tooMany(){
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.特定疾患管理);
        clinic.addShinryou(shinryouMap.特定疾患管理);
        int shinryouId = clinic.addShinryou(shinryouMap.特定疾患管理);
        syncScope(clinic);
        new CheckTokuteiShikkanKanri(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

}
