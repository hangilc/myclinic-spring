package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckChoukiTouyakuKasan extends Base {

    //private static Logger logger = LoggerFactory.getLogger(TestCheckChoukiTouyakuKasan.class);
    private int nerror;

    @After
    public void doAfter(){
        nerror = 0;
    }

    @Test
    public void checkConflict(){
        Clinic clinic = new Clinic();
        clinic.startVisit(1000);
        clinic.addShinryou(shinryouMap.特定疾患処方);
        clinic.addShinryou(shinryouMap.長期処方);
        Scope scope = createScope();
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
        };
        new CheckChoukiTouyakuKasan(scope).check();
        assertEquals(1, nerror);
    }

}
