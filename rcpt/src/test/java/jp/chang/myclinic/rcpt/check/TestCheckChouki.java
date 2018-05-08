package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckChouki extends Base {

    private int nerror;

    @After
    public void doAfter(){
        nerror = 0;
    }

    @Test
    public void passOK() {
        FixerLog log = new FixerLog();
        Scope scope = createScope(log);
        Clinic clinic = new Clinic();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("1 chouki", 0, nerror);
    }

    @Test
    public void shohouryouChoukiDuplicate() {
        FixerLog log = new FixerLog();
        Scope scope = createScope(log);
        Clinic clinic = new Clinic();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.調基);
        clinic.addShinryou(shinryouMap.処方せん料);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("shohouryu chouki dupliate", 1, nerror);
    }

    @Test
    public void choukiWithNoDrug() throws Exception {
        FixerLog log = new FixerLog();
        Scope scope = createScope(log);
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.再診);
        int shinryouId = clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId, log);
    }

    @Test
    public void tooManyChouki() throws Exception {
        FixerLog log = new FixerLog();
        Scope scope = createScope(log);
        Clinic clinic = new Clinic();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.再診);
        clinic.addShinryou(shinryouMap.調基);
        int shinryouId = clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId, log);
    }

    @Test
    public void choukiMissing() throws Exception {
        FixerLog log = new FixerLog();
        Scope scope = createScope(log);
        Clinic clinic = new Clinic();
        int visitId = clinic.startVisit();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.再診);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        assertEquals(1, nerror);
        assertEnterShinryou(visitId, shinryouMap.調基, log);
    }

}
