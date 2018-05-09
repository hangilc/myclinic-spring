package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckDuplicates extends Base {

    @Test
    public void duplicates(){
        Clinic clinic = new Clinic();
        int shinryoucode = clinic.createShinryouMaster();
        clinic.addShinryou(shinryoucode);
        clinic.addShinryou(shinryoucode);
        scope.visits = clinic.getVisits();
        new CheckDuplicates(scope).check();
        assertEquals(1, nerror);
    }

    @Test
    public void allowIgE(){
        Clinic clinic = new Clinic();
        clinic.createShinryouMaster(shinryouMap.非特異的ＩｇＥ);
        clinic.createShinryouMaster(shinryouMap.非特異的ＩｇＥ);
        clinic.createShinryouMaster(shinryouMap.非特異的ＩｇＥ);
        scope.visits = clinic.getVisits();
        new CheckDuplicates(scope).check();
        assertEquals(0, nerror);
    }

}
