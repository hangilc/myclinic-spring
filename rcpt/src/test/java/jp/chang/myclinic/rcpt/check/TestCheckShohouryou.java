package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckShohouryou extends Base {

    @Test
    public void missing(){
        Clinic clinic = new Clinic();
        int visitId = clinic.startVisit();
        clinic.addNaifukuDrug();
        scope.visits = clinic.getVisits();
        new CheckShohouryou(scope).check();
        assertEquals(1, nerror);
        assertEnterShinryou(visitId, shinryouMap.処方料);
    }

    @Test
    public void inappropriate7(){
        Clinic clinic = new Clinic();
        int visitId = clinic.startVisit();
        clinic.addNaifukuDrug();
        int shinryouId = clinic.addShinryou(shinryouMap.処方料７);
        scope.visits = clinic.getVisits();
        new CheckShohouryou(scope).check();
        assertEquals(2, nerror);
        assertEnterShinryou(visitId, shinryouMap.処方料);
        assertBatchDeleteShinryou(shinryouId);
    }

    @Test
    public void missing7(){
        Clinic clinic = new Clinic();
        int visitId = clinic.startVisit();
        clinic.addChoukiNaifukuDrug(7);
        scope.visits = clinic.getVisits();
        new CheckShohouryou(scope).check();
        assertEquals(1, nerror);
        assertEnterShinryou(visitId, shinryouMap.処方料７);
    }

    @Test
    public void inappropriate(){
        Clinic clinic = new Clinic();
        clinic.addChoukiNaifukuDrug(7);
        int shinryouId = clinic.addShinryou(shinryouMap.処方料);
        clinic.addShinryou(shinryouMap.処方料７);
        scope.visits = clinic.getVisits();
        new CheckShohouryou(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

    @Test
    public void inappropriateWithoutDrugs(){
        Clinic clinic = new Clinic();
        int shinryouId = clinic.addShinryou(shinryouMap.処方料);
        syncScope(clinic);
        new CheckShohouryou(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

}
