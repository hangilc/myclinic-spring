package jp.chang.myclinic.practice;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.support.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class Context {

    public static CmdOpts cmdOpts;
    public static Frontend frontend;
    public static MainPane mainPane;
    public static CurrentPatientService currentPatientService = new CurrentPatientService();
    public static MainStageService mainStageService;
    public static IntegrationService integrationService;
    public static ConfigService configService;
    public static PrinterEnv printerEnv = new PrinterEnv(Paths.get(
            System.getProperty("user.home"),
            "myclinic-env",
            "printer-settings"));

    private static String shohousenPrinterSettingKey = "shohousen-printer-setting";

    public static String getShohousenPrinterSetting(){
        return configService.getValue(shohousenPrinterSettingKey);
    }

    public static void setShohousenPrinterSetting(String settingName){
        configService.setValue(shohousenPrinterSettingKey, settingName);
    }

    private static String defaultPrinterSettingKey = "default-printer-setting";

    public static String getDefaultPrinterSetting(){
        return configService.getValue(defaultPrinterSettingKey);
    }

    public static void setDefaultPrinterSetting(String settingName){
        configService.setValue(defaultPrinterSettingKey, settingName);
    }

    private static String referPrinterSettingKey = "refer-printer-setting";

    public static String getReferPrinterSetting(){
        return configService.getValue(referPrinterSettingKey);
    }

    public static void setReferPrinterSetting(String settingName){
        configService.setValue(referPrinterSettingKey, settingName);
    }

}
