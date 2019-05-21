package jp.chang.myclinic.practice;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoProvider;
import jp.chang.myclinic.support.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Collections;

public class Context {

    public static CmdOpts cmdOpts;
    public static Frontend frontend;
    public static CurrentPatientService currentPatientService;
    public static MainStageService mainStageService;
    public static IntegrationService integrationService;
    private static ConfigService configService;
    public static PrinterEnv printerEnv = new PrinterEnv(Paths.get(
            System.getProperty("user.home"),
            "myclinic-env",
            "printer-settings"));
    public static ReferService referService = new ReferServiceData(Collections.emptyList());
    public static PracticeConfig practiceConfig;

//    public static void setConfigService(ConfigService configService){
//        Context.configService = configService;
//    }

    private static String shohousenPrinterSettingKey = "shohousen-printer-setting";

    public static String getShohousenPrinterSetting(){
        return configService.getValue(shohousenPrinterSettingKey);
    }

    public static void setShohousenPrinterSetting(String settingName){
        configService.setValue(shohousenPrinterSettingKey, settingName);
    }

    private static String referPrinterSettingKey = "refer-printer-setting";

    public static String getReferPrinterSetting(){
        return configService.getValue(referPrinterSettingKey);
    }

    public static void setReferPrinterSetting(String settingName){
        configService.setValue(referPrinterSettingKey, settingName);
    }

    private static String kouhatsuKasanKey = "kouhatsu-kasan";

    public static String getKouhatsuKasanSetting(){
        return configService.getValue(kouhatsuKasanKey);
    }

    public static void setKouhatsuKasanSetting(String newSetting){
        configService.setValue(kouhatsuKasanKey, newSetting);
    }

}
