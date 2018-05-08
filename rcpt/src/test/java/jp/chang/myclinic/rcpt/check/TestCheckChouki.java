package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckChouki extends Base {

    @Test
    public void passOK() {
        Clinic clinic = new Clinic();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        new CheckChouki(scope).check();
        assertEquals("1 chouki", 0, nerror);
    }

    @Test
    public void shohouryouChoukiDuplicate() {
        Clinic clinic = new Clinic();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.調基);
        clinic.addShinryou(shinryouMap.処方せん料);
        scope.visits = clinic.getVisits();
        new CheckChouki(scope).check();
        assertEquals("shohouryu chouki dupliate", 1, nerror);
    }

    @Test
    public void choukiWithNoDrug() {
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.再診);
        int shinryouId = clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        new CheckChouki(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

    @Test
    public void tooManyChouki() {
        Clinic clinic = new Clinic();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.再診);
        clinic.addShinryou(shinryouMap.調基);
        int shinryouId = clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        new CheckChouki(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

    @Test
    public void choukiMissing() {
        Clinic clinic = new Clinic();
        int visitId = clinic.startVisit();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.再診);
        scope.visits = clinic.getVisits();
        new CheckChouki(scope).check();
        assertEquals(1, nerror);
        assertEnterShinryou(visitId, shinryouMap.調基);
    }

}
