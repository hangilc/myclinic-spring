package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.backendasync.BackendAsync;
import jp.chang.myclinic.practice.PracticeConfigService;

public class ExecEnv {

    public BackendAsync restService;
    public MainPaneService mainPaneService;
    public PracticeConfigService configService;

    public ExecEnv(BackendAsync restService, MainPaneService mainPaneService, PracticeConfigService configService) {
        this.restService = restService;
        this.mainPaneService = mainPaneService;
        this.configService = configService;
    }
}
