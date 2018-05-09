package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSaishinByoumei extends Base {

    //private static Logger logger = LoggerFactory.getLogger(TestSaishinByoumei.class);

    @Test
    public void missingDiseases(){
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.再診);
        scope.visits = clinic.getVisits();
        new CheckSaishinByoumei(scope).check();
        assertEquals(1, nerror);
    }


}
