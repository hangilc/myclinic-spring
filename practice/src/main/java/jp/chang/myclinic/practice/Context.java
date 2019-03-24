package jp.chang.myclinic.practice;

import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.javafx.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context {

    public static Context instance = new Context();

    public static Context getInstance(){
        return instance;
    }

    private Context(){}

    private CmdOpts cmdsOpts;

    public CmdOpts getCmdsOpts() {
        return cmdsOpts;
    }

    public void setCmdsOpts(CmdOpts cmdsOpts) {
        this.cmdsOpts = cmdsOpts;
    }

    private Frontend frontend;

    public Frontend getFrontend() {
        return frontend;
    }

    public void setFrontend(Frontend frontend) {
        this.frontend = frontend;
    }

    private MainPane mainPane;

    public MainPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(MainPane mainPane) {
        this.mainPane = mainPane;
    }

    private CurrentPatientService currentPatientService = new CurrentPatientService();

    public CurrentPatientService getCurrentPatientService() {
        return currentPatientService;
    }

    private MainStageService mainStageService = new MainStageService();

    public MainStageService getMainStageService() {
        return mainStageService;
    }

    private IntegrationService integrationService = new IntegrationService();

    public IntegrationService getIntegrationService() {
        return integrationService;
    }

    private PracticeConfigService practiceConfigService;

    public PracticeConfigService getPracticeConfigService() {
        return practiceConfigService;
    }
}
