package jp.chang.myclinic.reception;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.myclinicenv.MyclinicEnv;

import java.nio.file.Path;
import java.util.Properties;

public class Globals {

    //private static Logger logger = LoggerFactory.getLogger(Globals.class);

    private Globals() {

    }

    private static MyclinicEnv myclinicEnv = new MyclinicEnv("reception");

    // Clinic Info
    private static ClinicInfoDTO clinicInfo;

    public static ClinicInfoDTO getClinicInfo() {
        return clinicInfo;
    }

    public static void setClinicInfo(ClinicInfoDTO clinicInfo) {
        Globals.clinicInfo = clinicInfo;
    }

    // Printer setting

    private static AppProperties appProps = loadAppProperties();

    private static AppProperties loadAppProperties(){
        Properties props = myclinicEnv.getAppProperties();
        AppProperties appProps = new AppProperties();
        appProps.load(props);
        return appProps;
    }

    private static void saveAppProperties(AppProperties appProps){
        Properties props = appProps.toProperties();
        myclinicEnv.saveAppProperties(props);
    }

    public static String getReceiptPrinterSetting() {
        if( appProps == null ){
            return null;
        } else {
            return appProps.receiptPrinterSetting;
        }
    }

    public static void setReceiptPrinterSetting(String receiptPrinterSetting) {
        if( appProps == null ){
            appProps = new AppProperties();
        }
        appProps.receiptPrinterSetting = receiptPrinterSetting;
        saveAppProperties(appProps);
    }

    public static PrinterEnv getPrinterEnv(){
        return myclinicEnv.getPrinterEnv();
    }

    // Scanner image save path
    private static Path imageSaveDir;

    public static Path getImageSaveDir() {
        if( imageSaveDir == null ) {
            imageSaveDir = myclinicEnv.createTempDir("image-save-dir");
            imageSaveDir.toFile().deleteOnExit();
        }
        return imageSaveDir;
    }

    // Tracking property
    private static BooleanProperty tracking = new SimpleBooleanProperty();

    public static boolean isTracking() {
        return tracking.get();
    }

    public static BooleanProperty trackingProperty() {
        return tracking;
    }

    public static void setTracking(boolean value) {
        tracking.set(value);
    }

}
