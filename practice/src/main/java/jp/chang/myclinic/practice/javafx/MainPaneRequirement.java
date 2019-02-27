package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenRequirement;
import jp.chang.myclinic.practice.javafx.text.TextRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MainPaneRequirement {

    public interface MainPaneRestService extends TextRequirement.TextRestService,
            ShohousenRequirement.ShohousenRestService, RecordRequirement.RecordRestService {
        CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds);
        CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds);
        CompletionStage<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds);
    }

    public interface MainPaneConfigService extends ShohousenRequirement.ShohousenConfigService {

    }

    public MainPaneRestService restService;
    public MainPaneConfigService configService;
    public MainStageService mainStageService;

    public MainPaneRequirement() {
    }

    public MainPaneRequirement(MainPaneRestService restService, MainPaneConfigService configService,
                               MainStageService mainStageService) {
        this.restService = restService;
        this.configService = configService;
        this.mainStageService = mainStageService;
    }
}
