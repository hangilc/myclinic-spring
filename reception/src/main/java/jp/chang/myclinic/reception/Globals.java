package jp.chang.myclinic.reception;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Window;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.myclinicenv.MyclinicEnv;
import jp.chang.myclinic.reception.javafx.MainPane;

import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Function;

public class Globals implements AppVars {

    private static Globals INSTANCE = new Globals();

    public static AppVars getAppVars(){
        return INSTANCE;
    }

    private Globals() {
        this.myclinicEnv = new MyclinicEnv("reception");
        this.appProps = loadAppProperties();
    }

    public static Globals getInstance(){
        return Globals.INSTANCE;
    }

    private MyclinicEnv myclinicEnv;

    // Clinic Info
    private ClinicInfoDTO clinicInfo;

    @Override
    public ClinicInfoDTO getClinicInfo() {
        return clinicInfo;
    }

    static void setClinicInfo(ClinicInfoDTO clinicInfo) {
        Globals.INSTANCE.clinicInfo = clinicInfo;
    }

    // Printer setting

    private AppProperties appProps;

    private AppProperties loadAppProperties(){
        Properties props = myclinicEnv.getAppProperties();
        AppProperties appProps = new AppProperties();
        appProps.load(props);
        return appProps;
    }

    @Override
    public void modifyAppProperties(Function<AppProperties, AppProperties> modifier){
        AppProperties newProps = modifier.apply(appProps);
        if( newProps != null ){
            this.appProps = newProps;
            Properties props = newProps.toProperties();
            myclinicEnv.saveAppProperties(props);
        }
    }

    @Override
    public String getReceiptPrinterSetting() {
        if( appProps == null ){
            return null;
        } else {
            return appProps.receiptPrinterSetting;
        }
    }

    @Override
    public Integer getDefaultKoukikoureiHokenshaBangou() {
        if( appProps == null ){
            return null;
        } else {
            return appProps.defaultKoukikoureiHokenshaBangou;
        }
    }

    @Override
    public String getDefaultKoukikoureiValidFrom() {
        if( appProps == null ){
            return null;
        } else {
            return appProps.defaultKoukikoureiValidFrom;
        }
    }

    @Override
    public String getDefaultKoukikoureiValidUpto() {
        if( appProps == null ){
            return null;
        } else {
            return appProps.defaultKoukikoureiValidUpto;
        }
    }

    @Override
    public PrinterEnv getPrinterEnv(){
        return myclinicEnv.getPrinterEnv();
    }

    // Scanner image save path
    private Path imageSaveDir;

    @Override
    public Path getImageSaveDir() {
        if( imageSaveDir == null ) {
            imageSaveDir = myclinicEnv.createTempDir("image-save-dir");
            imageSaveDir.toFile().deleteOnExit();
        }
        return imageSaveDir;
    }

    // Tracking property
    private BooleanProperty tracking = new SimpleBooleanProperty();

    @Override
    public boolean isTracking() {
        return tracking.get();
    }

    @Override
    public BooleanProperty trackingProperty() {
        return tracking;
    }

    @Override
    public void setTracking(boolean value) {
        tracking.set(value);
    }

    // MainPane
    private MainPane mainPane;

    static void setMainPane(MainPane mainPane){
        Globals.INSTANCE.mainPane = mainPane;
    }

    public static MainPane getMainPane(){
        return Globals.INSTANCE.mainPane;
    }

    // Window
    private Integer nextWindowId = 1;

    public Window findNewWindow(Class<? extends Window> target){
        for(Window win: Window.getWindows()){
            if( target.isInstance(win) && win.getUserData() == null ){
                win.setUserData(nextWindowId++);
                return win;
            }
        }
        return null;
    }

    public Window findWindow(int id){
        for(Window win: Window.getWindows()){
            Object data = win.getUserData();
            if( data instanceof Integer ){
                Integer winId = (Integer)data;
                if( winId.equals(id) ){
                    return win;
                }
            }
        }
        return null;
    }

}
