package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.practice.Context;

import java.time.LocalDate;

public class ShohousenUtil {

    private ShohousenUtil() {

    }

    static String composeFullKikanCode(ClinicInfoDTO clinicInfo){
        return clinicInfo.todoufukencode + clinicInfo.tensuuhyoucode + clinicInfo.kikancode;
    }

    public static void setClinicInfo(ShohousenDrawer drawer, ClinicInfoDTO clinicInfo){
        String addr = clinicInfo.postalCode + " " + clinicInfo.address;
        String name = clinicInfo.name;
        String phone = "電話 " + clinicInfo.tel;
        String kikancode = composeFullKikanCode(clinicInfo);
        drawer.setHakkouKikan(addr, name, phone, kikancode);
        drawer.setDoctorName(clinicInfo.doctorName);
        LocalDate today = LocalDate.now();
        drawer.setKoufuDate(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
    }

    static void changeDefaultPrinterSetting(String newSetting){
        Context.setShohousenPrinterSetting(newSetting);
    }

}
