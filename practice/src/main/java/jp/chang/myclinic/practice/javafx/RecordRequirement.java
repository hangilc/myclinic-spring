package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.practice.RestService;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenRequirement;
import jp.chang.myclinic.practice.javafx.text.TextRequirement;

public class RecordRequirement {

    interface RecordRestService extends TextRequirement.TextRestService {

    }
    interface RecordMainPaneService extends TextRequirement.TextMainPaneService {
        int getCurrentVisitId();
        int getTempVisitId();
    }

    public RecordRestService restService;
    public RecordMainPaneService mainPaneService;
    public ShohousenRequirement shohousenRequirement;

    public RecordRequirement() {

    }

    public RecordRequirement(RecordRestService restService, RecordMainPaneService mainPaneService,
                             ShohousenRequirement shohousenRequirement) {
        this.restService = restService;
        this.mainPaneService = mainPaneService;
        this.shohousenRequirement = shohousenRequirement;
    }

}
