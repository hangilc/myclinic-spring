package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;

public class TestCheckGaiyou extends BaseDrugShinryou {

    public TestCheckGaiyou(){
        setShinryoucode(shinryouMap.外用調剤);
    }

    @Override
    void addDrug(Clinic clinic){
        clinic.addGaiyouDrug();
    }

    @Override
    void check(){
        new CheckGaiyou(scope).check();
    }

}
