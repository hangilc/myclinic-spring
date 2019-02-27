package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;

public interface ShohousenConfigRequirement {

    String getShohousenPrinterSetting();
    void setShohousenPrinterSetting(String settingName);
    PrinterEnv getPrinterEnv();
    ClinicInfoDTO getClinicInfo();

}
