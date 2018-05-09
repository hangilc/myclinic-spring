package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckShoshinSaishin extends Base {

    @Test
    public void missing(){
        Clinic clinic = new Clinic();
        clinic.startVisit();
        syncScope(clinic);
        new CheckShoshinSaishin(scope).check();
        assertEquals(1, nerror);
    }

    @Test
    public void duplicates(){
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.初診);
        clinic.addShinryou(shinryouMap.再診);
        syncScope(clinic);
        new CheckShoshinSaishin(scope).check();
        assertEquals(1, nerror);
    }

}
