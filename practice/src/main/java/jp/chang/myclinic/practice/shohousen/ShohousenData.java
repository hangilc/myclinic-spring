package jp.chang.myclinic.practice.shohousen;

import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.HokenDTO;

public class ShohousenData {

    private String clinicAddress = "";
    private String clinicName = "";
    private String clinicPhone = "";
    private String kikancode = "";
    private String doctorName = "";
    private String hokenshaBangou = "";

    public void applyTo(ShohousenDrawer drawer){
        drawer.setHakkouKikan(clinicAddress, clinicName, clinicPhone, kikancode);
        drawer.setDoctorName(doctorName);
        drawer.setHokenshaBangou(hokenshaBangou);
    }

    public void setClinicInfo(ClinicInfoDTO clinicInfo){
        if( clinicInfo != null ){
            clinicAddress = clinicInfo.postalCode + " " + clinicInfo.address;
            clinicName = clinicInfo.name;
            clinicPhone = "電話 " + clinicInfo.tel;
            kikancode = clinicInfo.kikancode;
            doctorName = clinicInfo.doctorName;
        }
    }

    public void setHoken(HokenDTO hoken){
        if( hoken != null ){
            if( hoken.shahokokuho != null ){
                hokenshaBangou = hoken.shahokokuho.hokenshaBangou + "";
            }
        }
    }
}
