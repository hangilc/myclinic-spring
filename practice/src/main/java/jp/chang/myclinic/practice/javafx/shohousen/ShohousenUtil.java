package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;

public class ShohousenUtil {

    private static Logger logger = LoggerFactory.getLogger(ShohousenUtil.class);

    private ShohousenUtil() {

    }

    public static String composeFullKikanCode(ClinicInfoDTO clinicInfo){
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

    public static void changeDefaultPrinterSetting(String newSetting){
        try {
            PracticeEnv.INSTANCE.setAppProperty(PracticeEnv.SHOHOUSEN_PRINTER_SETTING_KEY, newSetting);
        } catch (IOException e) {
            logger.error("Failed to set default printer setting of shohousen.", e);
            GuiUtil.alertException("処方箋の既定印刷設定の保存に失敗しました。", e);
        }
    }

}
