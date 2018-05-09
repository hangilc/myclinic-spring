package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckSaishinByoumei extends Base {

    //private static Logger logger = LoggerFactory.getLogger(TestCheckSaishinByoumei.class);

    @Test
    public void missingDiseases(){
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.再診);
        syncScope(clinic);
        new CheckSaishinByoumei(scope).check();
        assertEquals(1, nerror);
    }


}
