package jp.chang.myclinic.reception;

import javafx.beans.property.BooleanProperty;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;

import java.nio.file.Path;
import java.util.function.Function;

public interface AppVars {

    public ClinicInfoDTO getClinicInfo();
    public String getReceiptPrinterSetting();
    public Integer getDefaultKoukikoureiHokenshaBangou();
    public String getDefaultKoukikoureiValidFrom();
    public String getDefaultKoukikoureiValidUpto();
    public PrinterEnv getPrinterEnv();
    public Path getImageSaveDir();
    public boolean isTracking();
    public BooleanProperty trackingProperty();
    public void setTracking(boolean value);
    public void modifyAppProperties(Function<AppProperties, AppProperties> modifier);

}
