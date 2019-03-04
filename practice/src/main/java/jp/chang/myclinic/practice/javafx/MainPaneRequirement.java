package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.backendasync.BackendAsync;
import jp.chang.myclinic.practice.MainStageService;
import jp.chang.myclinic.practice.PracticeConfigService;

public class MainPaneRequirement {

    public BackendAsync restService;
    public PracticeConfigService configService;
    public MainStageService mainStageService;

    public MainPaneRequirement() {
    }

    public MainPaneRequirement(BackendAsync restService, PracticeConfigService configService,
                               MainStageService mainStageService) {
        this.restService = restService;
        this.configService = configService;
        this.mainStageService = mainStageService;
    }
}
