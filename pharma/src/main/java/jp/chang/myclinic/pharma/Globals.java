package jp.chang.myclinic.pharma;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Globals {

    private static Logger logger = LoggerFactory.getLogger(Globals.class);

    private Globals() { }

    public static ClinicInfoDTO clinicInfo;
    public static PrinterEnv printerEnv;

}
