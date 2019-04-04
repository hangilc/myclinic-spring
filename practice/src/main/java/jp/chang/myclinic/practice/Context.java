package jp.chang.myclinic.practice;

import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.javafx.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context {

    public static CmdOpts cmdOpts;
    public static Frontend frontend;
    public static MainPane mainPane;
    public static CurrentPatientService currentPatientService;
    public static MainStageService mainStageService;
    public static PracticeConfigService practiceConfigService;
    public static IntegrationService integrationService;
}
