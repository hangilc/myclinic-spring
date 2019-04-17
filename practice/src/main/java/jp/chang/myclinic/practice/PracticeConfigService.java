package jp.chang.myclinic.practice;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;

public interface PracticeConfigService {

    String getShohousenPrinterSetting();
    void setShohousenPrinterSetting(String settingName);
    String getKouhatsuKasan();
    PrinterEnv getPrinterEnv();

}
