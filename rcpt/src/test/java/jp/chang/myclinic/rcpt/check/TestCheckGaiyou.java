package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class TestCheckGaiyou extends Base {

    private static Logger logger = LoggerFactory.getLogger(TestCheckGaiyou.class);

    @Test
    public void gaiyouChouzaiMissing(){
        Clinic clinic = new Clinic();
        int visitId = clinic.startVisit();
        clinic.addGaiyouDrug();
        scope.visits = clinic.getVisits();
        new CheckGaiyou(scope).check();
        assertEquals(1, nerror);
        assertEnterShinryou(visitId, shinryouMap.外用調剤);
    }

    @Test
    public void tooManyGaiyouChouzai(){
        Clinic clinic = new Clinic();
        clinic.addGaiyouDrug();
        clinic.addShinryou(shinryouMap.外用調剤);
        int shinryouId = clinic.addShinryou(shinryouMap.外用調剤);
        scope.visits = clinic.getVisits();
        new CheckGaiyou(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

    @Test
    public void wrongGaiyouChouzai(){
        Clinic clinic = new Clinic();
        int shinryouId = clinic.addShinryou(shinryouMap.外用調剤);
        scope.visits = clinic.getVisits();
        new CheckGaiyou(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

}
