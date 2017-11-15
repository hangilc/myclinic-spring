package jp.chang.myclinic.practice.shohousen;

import jp.chang.myclinic.dto.*;

import java.util.ArrayList;
import java.util.List;

public class ShohousenData {

    private String clinicAddress = "";
    private String clinicName = "";
    private String clinicPhone = "";
    private String kikancode = "";
    private String doctorName = "";
    private String hokenshaBangou = "";
    private String hihokensha = "";
    private String futansha = "";
    private String jukyuusha = "";
    private String futansha2 = "";
    private String jukyuusha2 = "";
    private String shimei = "";

    public void applyTo(ShohousenDrawer drawer){
        drawer.setHakkouKikan(clinicAddress, clinicName, clinicPhone, kikancode);
        drawer.setDoctorName(doctorName);
        drawer.setHokenshaBangou(hokenshaBangou);
        drawer.setHihokensha(hihokensha);
        drawer.setKouhi1Futansha(futansha);
        drawer.setKouhi1Jukyuusha(jukyuusha);
        drawer.setKouhi2Futansha(futansha2);
        drawer.setKouhi2Jukyuusha(jukyuusha2);
        drawer.setShimei(shimei);
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
                hihokensha = composeHihokensha(hoken.shahokokuho);
            }
            List<KouhiDTO> kouhiList = new ArrayList<>();
            if( hoken.kouhi1 != null ){
                kouhiList.add(hoken.kouhi1);
                if( hoken.kouhi2 != null ){
                    kouhiList.add(hoken.kouhi2);
                    if( hoken.kouhi3 != null ){
                        kouhiList.add(hoken.kouhi3);
                    }
                }
            }
            if( kouhiList.size() > 0 ){
                KouhiDTO kouhi = kouhiList.get(0);
                futansha = kouhi.futansha + "";
                jukyuusha = kouhi.jukyuusha + "";
                if( kouhiList.size() > 1 ){
                    KouhiDTO kouhi2 = kouhiList.get(1);
                    futansha2 = kouhi2.futansha + "";
                    jukyuusha2 = kouhi2.jukyuusha + "";
                }
            }
        }
    }

    public void setPatient(PatientDTO patient){
        shimei = patient.lastName + patient.firstName;
    }

    private String composeHihokensha(ShahokokuhoDTO shahokokuho){
        String kigou = shahokokuho.hihokenshaKigou;
        String bangou = shahokokuho.hihokenshaBangou;
        if( kigou == null ){
            if( bangou == null ){
                return "";
            } else {
                return bangou;
            }
        } else {
            if( bangou == null ){
                return kigou;
            } else {
                return kigou + " ・ " + bangou;
            }
        }
    }
}
