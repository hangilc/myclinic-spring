package jp.chang.myclinic.practice;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenConfigRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PracticeConfigService extends ShohousenConfigRequirement {

    String getShohousenPrinterSetting();
    void setShohousenPrinterSetting(String settingName);
    PrinterEnv getPrinterEnv();
    ClinicInfoDTO getClinicInfo();

}
