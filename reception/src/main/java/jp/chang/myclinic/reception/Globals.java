package jp.chang.myclinic.reception;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.myclinicenv.MyclinicEnv;

import java.nio.file.Path;

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
    private static String receiptPrintSettingKey = "receipt-printer-setting";

    private static StringProperty receiptPrinterSetting = new SimpleStringProperty();

    static {
        receiptPrinterSetting.setValue(myclinicEnv.getAppProperty(receiptPrintSettingKey));
        receiptPrinterSetting.addListener((obs, oldValue, newValue) ->
                myclinicEnv.saveAppProperty(receiptPrintSettingKey, newValue)
        );
    }

    public static String getReceiptPrinterSetting() {
        return receiptPrinterSetting.get();
    }

    public static StringProperty receiptPrinterSettingProperty() {
        return receiptPrinterSetting;
    }

    public static void setReceiptPrinterSetting(String receiptPrinterSetting) {
        Globals.receiptPrinterSetting.set(receiptPrinterSetting);
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
