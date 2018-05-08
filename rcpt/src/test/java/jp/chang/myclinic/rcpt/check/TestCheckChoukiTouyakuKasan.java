package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestCheckChoukiTouyakuKasan extends Base {

    //private static Logger logger = LoggerFactory.getLogger(TestCheckChoukiTouyakuKasan.class);
    @Test
    public void checkConflict() {
        Clinic clinic = new Clinic();
        clinic.startVisit();
        int shinryouId = clinic.addShinryou(shinryouMap.特定疾患処方);
        clinic.addShinryou(shinryouMap.長期処方);
        scope.visits = clinic.getVisits();
        new CheckChoukiTouyakuKasan(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

    @Test
    public void tooManyChoukishohou() {
        Clinic clinic = new Clinic();
        clinic.startVisit();
        clinic.addShinryou(shinryouMap.長期処方);
        int shinryouId = clinic.addShinryou(shinryouMap.長期処方);
        scope.visits = clinic.getVisits();
        new CheckChoukiTouyakuKasan(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

    @Test
    public void tooManyTokutei() throws Exception {
        Clinic clinic = new Clinic();
        clinic.startVisit();
        clinic.addShinryou(shinryouMap.特定疾患処方);
        clinic.addShinryou(shinryouMap.特定疾患処方);
        int shinryouId1 = clinic.addShinryou(shinryouMap.特定疾患処方);
        int shinryouId2 = clinic.addShinryou(shinryouMap.特定疾患処方);
        scope.visits = clinic.getVisits();
        new CheckChoukiTouyakuKasan(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(Set.of(shinryouId1, shinryouId2));
    }

}
