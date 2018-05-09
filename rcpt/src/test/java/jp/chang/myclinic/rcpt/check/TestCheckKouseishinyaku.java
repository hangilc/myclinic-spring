package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;

public class TestCheckKouseishinyaku extends TestBaseDrugShinryou {

    //private static Logger logger = LoggerFactory.getLogger(TestCheckKouseishinyaku.class);

    public TestCheckKouseishinyaku() {
        setShinryoucode(shinryouMap.向精神薬);
    }

    void addDrug(Clinic clinic){
        clinic.addMadokuDrug();
    }

    void check(){
        new CheckKouseishinyaku(scope).check();
    }

}
