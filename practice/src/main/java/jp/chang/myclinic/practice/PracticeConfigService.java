package jp.chang.myclinic.practice;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.practice.javafx.MainPaneRequirement;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenRequirement;

public interface PracticeConfigService extends ShohousenRequirement.ShohousenConfigService,
        MainPaneRequirement.MainPaneConfigService {

    String getShohousenPrinterSetting();

    void setShohousenPrinterSetting(String settingName);

    PrinterEnv getPrinterEnv();

    ClinicInfoDTO getClinicInfo();

}
