package jp.chang.myclinic.pharma;

import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.myclinicenv.MyclinicEnv;
import jp.chang.myclinic.pharma.javafx.PatientList;
import jp.chang.myclinic.pharma.tracking.ModelDispatchAction;
import jp.chang.myclinic.pharma.tracking.ModelRegistry;

public class Globals {

    //private static Logger logger = LoggerFactory.getLogger(Globals.class);

    private Globals() { }

    private static MyclinicEnv myclinicEnv = new MyclinicEnv("pharma");

    // Application variables ///////////////////////////////////////////////////////////////////

    private static Callback<PatientList.Model, Observable[]> modelExtractor = model -> new Observable[]{
            model.nameProperty(),
            model.waitStateProperty()
    };

    //@Bean(name="tracking-visit-list")
    private static ObservableList<PatientList.Model> trackingVisitList = FXCollections.observableArrayList(modelExtractor);

    public static ObservableList<PatientList.Model> getTrackingVisitList() {
        return trackingVisitList;
    }

    public static void setTrackingVisitList(ObservableList<PatientList.Model> trackingVisitList) {
        Globals.trackingVisitList = trackingVisitList;
    }

    //@Bean(name="tracking-pharma-list")
    private static ObservableList<PatientList.Model> trackingPharmaList = FXCollections.observableArrayList(modelExtractor);

    public static ObservableList<PatientList.Model> getTrackingPharmaList() {
        return trackingPharmaList;
    }

    public static void setTrackingPharmaList(ObservableList<PatientList.Model> trackingPharmaList) {
        Globals.trackingPharmaList = trackingPharmaList;
    }

    //@Bean(name="tracking-flag")
    private static SimpleBooleanProperty tracking = new SimpleBooleanProperty(true);

    public static boolean isTracking() {
        return tracking.get();
    }

    public static SimpleBooleanProperty trackingProperty() {
        return tracking;
    }

    public static void setTracking(boolean value) {
        tracking.set(value);
    }

    // ClinicInfo ////////////////////////////////////////////////////////////////////////////
    private static ClinicInfoDTO clinicInfo;

    public static ClinicInfoDTO getClinicInfo() {
        return clinicInfo;
    }

    public static void setClinicInfo(ClinicInfoDTO clinicInfo) {
        Globals.clinicInfo = clinicInfo;
    }

    // PrinterEnv ////////////////////////////////////////////////////////////////////////////
    public static PrinterEnv getPrinterEnv() {
        return myclinicEnv.getPrinterEnv();
    }

    // ModelDispatchAction ////////////////////////////////////////////////////////////////////
    public static ModelDispatchAction createModelDispatchAction(){
        ModelRegistry modelRegistry = new ModelRegistry(trackingVisitList, trackingPharmaList);
        return new ModelDispatchAction(modelRegistry);
    }


}
