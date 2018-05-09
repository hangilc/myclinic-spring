package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

class BaseDrugShinryou extends Base {

    //private static Logger logger = LoggerFactory.getLogger(BaseDrugShinryou.class);
    private int shinryoucode;

    void setShinryoucode(int shinryoucode){
        this.shinryoucode = shinryoucode;
    }

    void addDrug(Clinic clinic){

    }

    void check(){

    }

    @Test
    public void missing(){
        Clinic clinic = new Clinic();
        int visitId = clinic.startVisit();
        addDrug(clinic);
        scope.visits = clinic.getVisits();
        check();
        assertEquals(1, nerror);
        assertEnterShinryou(visitId, shinryoucode);
    }

    @Test
    public void tooMany(){
        Clinic clinic = new Clinic();
        addDrug(clinic);
        clinic.addShinryou(shinryoucode);
        int shinryouId = clinic.addShinryou(shinryoucode);
        scope.visits = clinic.getVisits();
        check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

    @Test
    public void inappropriate(){
        Clinic clinic = new Clinic();
        int shinryouId = clinic.addShinryou(shinryoucode);
        scope.visits = clinic.getVisits();
        check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

}
